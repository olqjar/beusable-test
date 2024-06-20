package com.beusable.test.roommanager.model;

import java.math.BigDecimal;

public record RoomOccupation(int premiumRooms, int economyRooms, BigDecimal premiumAmount, BigDecimal economyAmount) {
}
