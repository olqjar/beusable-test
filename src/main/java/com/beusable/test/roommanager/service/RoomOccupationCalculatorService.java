package com.beusable.test.roommanager.service;

import com.beusable.test.roommanager.model.RoomConfiguration;
import com.beusable.test.roommanager.model.RoomOccupation;

import java.math.BigDecimal;
import java.util.List;

public interface RoomOccupationCalculatorService {
    RoomOccupation calculateOccupation(RoomConfiguration roomConfiguration, List<BigDecimal> guestOffers);
}
