package com.beusable.test.roommanager.service;

import com.beusable.test.roommanager.model.RoomConfiguration;
import com.beusable.test.roommanager.model.RoomOccupation;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

//@Service
public class RoomOccupationCalculatorServiceImpl implements RoomOccupationCalculatorService {

    final Predicate<BigDecimal> premiumPredicate;
    final Predicate<BigDecimal> economicPredicate;
//@Value("${roommanager.premiumThreshold}") BigDecimal premiumThreshold
    public RoomOccupationCalculatorServiceImpl() {

        var premiumThreshold = BigDecimal.valueOf(100);

        premiumPredicate = offer -> offer.compareTo(premiumThreshold) >= 0;
        economicPredicate = offer -> offer.compareTo(premiumThreshold) < 0;
        //this.premiumThreshold = premiumThreshold;

    }

    @Override
    public RoomOccupation calculateOccupation(RoomConfiguration roomConfiguration, List<BigDecimal> guestOffers) {
        if (roomConfiguration == null || guestOffers == null) {
            throw new IllegalArgumentException("Arguments can't be null");
        }

        var premiumOffers = getOffers(guestOffers, premiumPredicate, roomConfiguration.premiumRooms());
        var economyOffers = getOffers(guestOffers, economicPredicate, roomConfiguration.economyRooms());

        var availablePremiumRooms = roomConfiguration.premiumRooms() - premiumOffers.size();

        if (availablePremiumRooms > 0) {
            premiumOffers = Stream.concat(
                    premiumOffers.stream(),
                    getOffers(guestOffers, economicPredicate, economyOffers.size(), availablePremiumRooms).stream()
            ).toList();
        }

        return new RoomOccupation(
                premiumOffers.size(),
                economyOffers.size(),
                premiumOffers.stream().reduce(BigDecimal.ZERO, BigDecimal::add),
                economyOffers.stream().reduce(BigDecimal.ZERO, BigDecimal::add)
        );
    }

    private List<BigDecimal> getOffers(
            List<BigDecimal> guestOffers,
            Predicate<BigDecimal> predicate,
            int roomsToSkip,
            int availableRooms
    ) {
        return guestOffers.stream()
                .filter(predicate)
                .sorted(Comparator.reverseOrder())
                .skip(roomsToSkip)
                .limit(availableRooms)
                .toList();
    }

    private List<BigDecimal> getOffers(
            List<BigDecimal> guestOffers,
            Predicate<BigDecimal> predicate,
            int availableRooms
    ) {
        return guestOffers.stream()
                .filter(predicate)
                .sorted(Comparator.reverseOrder())
                .limit(availableRooms)
                .toList();
    }
}
