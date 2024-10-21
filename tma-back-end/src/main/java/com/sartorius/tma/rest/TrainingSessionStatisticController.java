package com.sartorius.tma.rest;

import com.sartorius.tma.business.services.TrainingSessionStatisticService;
import com.sartorius.tma.dtos.statics.TrainingSessionStatisticDto;
import com.sartorius.tma.dtos.statics.TrainingRequestCountDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    @GetMapping("/sessions-by-month")
    public ResponseEntity<Map<String, Integer>> getSessionStatsByMonth(
            @RequestParam("year") int year,
            @RequestParam("month") int month) {
        return ResponseEntity.ok(trainingSessionStatisticService.getSessionStatsByMonth(year, month));
    }

}
