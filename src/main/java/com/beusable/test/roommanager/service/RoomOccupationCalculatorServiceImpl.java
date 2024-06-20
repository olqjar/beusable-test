package com.beusable.test.roommanager.service;

import com.beusable.test.roommanager.model.OccupationData;
import com.beusable.test.roommanager.model.RoomOccupation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

@Service
public class RoomOccupationCalculatorServiceImpl implements RoomOccupationCalculatorService {

    final Predicate<BigDecimal> premiumPredicate;
    final Predicate<BigDecimal> economicPredicate;

    public RoomOccupationCalculatorServiceImpl(@Value("${roomManager.premiumThreshold}") BigDecimal premiumThreshold) {
        this.premiumPredicate = offer -> offer.compareTo(premiumThreshold) >= 0;
        this.economicPredicate = offer -> offer.compareTo(premiumThreshold) < 0;
    }

    @Override
    public RoomOccupation calculateOccupation(OccupationData occupationData) {
        if (occupationData == null || occupationData.roomConfiguration() == null || occupationData.customerOffers() == null) {
            throw new IllegalArgumentException("Arguments can't be null");
        }
        var offers = occupationData.customerOffers();
        var roomConfig = occupationData.roomConfiguration();

        var premiumOffers = getOffers(offers, premiumPredicate, roomConfig.premiumRooms());
        var economyOffers = getOffers(offers, economicPredicate, roomConfig.economyRooms());

        var availablePremiumRooms = roomConfig.premiumRooms() - premiumOffers.size();

        if (availablePremiumRooms > 0) {
            premiumOffers = Stream.concat(
                    premiumOffers.stream(),
                    getOffers(offers, economicPredicate, economyOffers.size(), availablePremiumRooms).stream()
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
