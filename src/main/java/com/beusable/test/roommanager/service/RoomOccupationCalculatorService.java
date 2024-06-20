package com.beusable.test.roommanager.service;

import com.beusable.test.roommanager.model.OccupationData;
import com.beusable.test.roommanager.model.RoomOccupation;

public interface RoomOccupationCalculatorService {
    RoomOccupation calculateOccupation(OccupationData occupationData);
}
