package com.sartorius.tma.rest;

import com.sartorius.tma.business.services.TrainingSessionStatisticService;
import com.sartorius.tma.dtos.statics.TrainingSessionStatisticDto;
import com.sartorius.tma.dtos.statics.TrainingRequestCountDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/training-session-static")
@RequiredArgsConstructor
public class TrainingSessionStatisticController {
    private final TrainingSessionStatisticService
            trainingSessionStatisticService;

    @GetMapping
    public TrainingSessionStatisticDto getAdminStatisticData() {
        return this.trainingSessionStatisticService.getStatisticData();
    }

    @GetMapping("/top10")
    public List<TrainingRequestCountDTO> getTop10TrainingRequests() {
        return this.trainingSessionStatisticService.getTop10TrainingRequests();
    }
}
