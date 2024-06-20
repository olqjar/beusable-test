package com.beusable.test.roommanager.service;

import com.beusable.test.roommanager.model.RoomConfiguration;
import com.beusable.test.roommanager.model.RoomOccupation;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

//@Service
public class RoomOccupationCalculatorServiceImpl implements RoomOccupationCalculatorService {

    final BigDecimal premiumThreshold;
//@Value("${roommanager.premiumThreshold}") BigDecimal premiumThreshold
    public RoomOccupationCalculatorServiceImpl() {
        //this.premiumThreshold = premiumThreshold;
        this.premiumThreshold = BigDecimal.valueOf(100);
    }

    @Override
    public RoomOccupation calculateOccupation(RoomConfiguration roomConfiguration, List<BigDecimal> guestOffers) {
        if (roomConfiguration == null || guestOffers == null) {
            throw new IllegalArgumentException("Arguments can't be null");
        }

        var premiumOffers = guestOffers.stream()
                .filter(offer -> offer.compareTo(premiumThreshold) >= 0)
                .sorted(Comparator.reverseOrder())
                .limit(roomConfiguration.premiumRooms())
                .toList();
        
        var economyOffers = guestOffers.stream()
                .filter(offer -> offer.compareTo(premiumThreshold) < 0)
                .sorted(Comparator.reverseOrder())

                .collect(Collectors.toList());

        var premiumRoomsOccupied = premiumOffers.size();
        var premiumRoomsAmount = premiumOffers.stream().reduce(BigDecimal.ZERO, BigDecimal::add);

        var economyRoomsOccupied = economyOffers.size() < roomConfiguration.economyRooms() ? economyOffers.size() : roomConfiguration.economyRooms();
        var economyRoomsAmount = economyOffers.stream().limit(economyRoomsOccupied).reduce(BigDecimal.ZERO, BigDecimal::add);

        if (economyRoomsOccupied < economyOffers.size() && roomConfiguration.premiumRooms() > premiumRoomsOccupied) {
            var economyOffersToUpgrade = economyOffers.stream()
                    .skip(economyRoomsOccupied)
                    .limit(roomConfiguration.premiumRooms() - premiumRoomsOccupied)
                    .toList();
            premiumRoomsOccupied += economyOffersToUpgrade.size();
            premiumRoomsAmount = economyOffersToUpgrade.stream().reduce(premiumRoomsAmount, BigDecimal::add);
        }

        return new RoomOccupation(
                premiumRoomsOccupied,
                economyRoomsOccupied,
                premiumRoomsAmount,
                economyRoomsAmount
        );
    }
}
