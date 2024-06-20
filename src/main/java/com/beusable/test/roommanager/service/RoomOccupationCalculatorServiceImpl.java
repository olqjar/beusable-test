package com.beusable.test.roommanager.service;

import com.beusable.test.roommanager.model.RoomConfiguration;
import com.beusable.test.roommanager.model.RoomOccupation;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

//@Service
public class RoomOccupationCalculatorServiceImpl implements RoomOccupationCalculatorService {
    @Override
    public RoomOccupation calculateOccupation(RoomConfiguration roomConfiguration, List<BigDecimal> guestOffers) {
        return null;
    }
}
