package com.beusable.test.roommanager.model;

import java.math.BigDecimal;
import java.util.List;

public record OccupationData(RoomConfiguration roomConfiguration, List<BigDecimal> customerOffers){
}
