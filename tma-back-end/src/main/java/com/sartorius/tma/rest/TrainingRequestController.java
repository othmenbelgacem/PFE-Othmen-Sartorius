package com.sartorius.tma.rest;

import com.sartorius.tma.business.services.TrainingRequestService;
import com.sartorius.tma.client.dtos.response.TrainingSessionResponse;
import com.sartorius.tma.dtos.PageDto;
import com.sartorius.tma.dtos.UserDetails;
import com.sartorius.tma.dtos.training_request.TrainingRequestDTO;
import com.sartorius.tma.enumeration.TrainingRequestStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/training-request")
@RequiredArgsConstructor
public class TrainingRequestController {

    private final TrainingRequestService trainingRequestService;

    @GetMapping()
    public PageDto<TrainingRequestDTO> getTrainingRequestPage(
            @RequestParam(name = "page",required = false) Integer page,
            @RequestParam(name = "offset",required = false) Integer offset,
            @RequestParam(name = "trainingId",required = false)  String trainingId,
            @RequestParam(name = "subTrainingId",required = false)  String subTrainingId) {
        return this.trainingRequestService.getTrainingRequestPage(page,offset,trainingId, subTrainingId);
    }


    @GetMapping("/requested-operators")
    public List<UserDetails> getTrainingRequestsOpertaors(
            @RequestParam(name = "trainingId",required = false)  String trainingId,
            @RequestParam(name = "subTrainingId",required = false)  String subTrainingId) {
        return this.trainingRequestService.getTrainingRequestsOpertaors(trainingId, subTrainingId);
    }
    @GetMapping("/count-requested")
    public ResponseEntity<Long> getCountRequested() {
        long count = trainingRequestService.countByStatus(TrainingRequestStatus.REQUESTED);
        return ResponseEntity.ok(count);
    }
}
