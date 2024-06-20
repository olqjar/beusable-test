package com.beusable.test.roommanager.contoller;

import com.beusable.test.roommanager.model.OccupationData;
import com.beusable.test.roommanager.model.RoomOccupation;
import com.beusable.test.roommanager.service.RoomOccupationCalculatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class RoomOccupationController {

    private final RoomOccupationCalculatorService roomOccupationCalculatorService;

    @PostMapping("/api/rooms/occupation/calculate")
    public RoomOccupation setOccupation(@RequestBody OccupationData data) {
        var result = roomOccupationCalculatorService.calculateOccupation(data);
        return result;
    }
}
