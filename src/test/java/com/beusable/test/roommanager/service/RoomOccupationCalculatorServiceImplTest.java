package com.beusable.test.roommanager.service;

import com.beusable.test.roommanager.model.OccupationData;
import com.beusable.test.roommanager.model.RoomConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestPropertySource(properties = {
        "roomManager.premiumThreshold=100"
})
class RoomOccupationCalculatorServiceImplTest {

    @Autowired
    private RoomOccupationCalculatorServiceImpl objectUnderTest;

    private final List<BigDecimal> testOffers = Arrays.stream("23, 45, 155, 374, 22, 99.99, 100, 101, 115, 209".split(", "))
            .map(BigDecimal::new)
            .toList();

    @Test
    void test1() {
        //given
        var config = new RoomConfiguration(3, 3);
        //when
        var result = objectUnderTest.calculateOccupation(new OccupationData(config, testOffers));
        //then
        assertEquals(3, result.premiumRooms());
        assertEquals(3, result.economyRooms());
        assertEquals(new BigDecimal("738"), result.premiumAmount());
        assertEquals(new BigDecimal("167.99"), result.economyAmount());
    }

    @Test
    void test2() {
        //given
        var config = new RoomConfiguration(7, 5);
        //when
        var result = objectUnderTest.calculateOccupation(new OccupationData(config, testOffers));
        //then
        assertEquals(6, result.premiumRooms());
        assertEquals(4, result.economyRooms());
        assertEquals(new BigDecimal("1054"), result.premiumAmount());
        assertEquals(new BigDecimal("189.99"), result.economyAmount());
    }


    @Test
    void test3() {
        //given
        var config = new RoomConfiguration(2, 7);
        //when
        var result = objectUnderTest.calculateOccupation(new OccupationData(config, testOffers));
        //then
        assertEquals(2, result.premiumRooms());
        assertEquals(4, result.economyRooms());
        assertEquals(new BigDecimal("583"), result.premiumAmount());
        assertEquals(new BigDecimal("189.99"), result.economyAmount());
    }

    void test4_invalid() {
        //given
        var config = new RoomConfiguration(7,1);
        //when
        var result = objectUnderTest.calculateOccupation(new OccupationData(config, testOffers));
        //then
        assertEquals(7, result.premiumRooms());
        assertEquals(1, result.economyRooms());
        assertEquals(new BigDecimal("1153"), result.premiumAmount());
        assertEquals(new BigDecimal("45.99"), result.economyAmount());
    }
    @Test
    void test4_fixed() {
        //given
        var config = new RoomConfiguration(7,1);
        //when
        var result = objectUnderTest.calculateOccupation(new OccupationData(config, testOffers));
        //then
        assertEquals(7, result.premiumRooms());
        assertEquals(1, result.economyRooms());
        assertEquals(new BigDecimal("1099"), result.premiumAmount());
        assertEquals(new BigDecimal("99.99"), result.economyAmount());
    }
}
