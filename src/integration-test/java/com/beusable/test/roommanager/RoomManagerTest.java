package com.beusable.test.roommanager;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.beusable.test.roommanager.model.OccupationData;
import com.beusable.test.roommanager.model.RoomConfiguration;
import com.beusable.test.roommanager.model.RoomOccupation;
import com.beusable.test.roommanager.service.RoomOccupationCalculatorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class RoomManagerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RoomOccupationCalculatorService roomOccupationCalculatorService;


    @Test
    public void testCalculateOccupation() throws Exception {
        //given
        RoomConfiguration roomConfig = new RoomConfiguration(3, 3);
        List<BigDecimal> offers = List.of(new BigDecimal("100"), new BigDecimal("200"), new BigDecimal("300"));
        OccupationData occupationData = new OccupationData(roomConfig, offers);
        RoomOccupation expectedResponse = new RoomOccupation(3, 3, new BigDecimal("600"), new BigDecimal("0"));
        Mockito.when(roomOccupationCalculatorService.calculateOccupation(occupationData)).thenReturn(expectedResponse);
        //when//then
        mockMvc.perform(post("/api/rooms/occupation/calculate")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(occupationData)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)));
    }
}
