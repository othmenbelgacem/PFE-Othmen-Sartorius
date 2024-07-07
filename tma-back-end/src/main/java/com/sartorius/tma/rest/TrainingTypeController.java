package com.sartorius.tma.rest;

import java.util.List;
import java.util.UUID;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sartorius.tma.business.services.TrainingSubTypeService;
import com.sartorius.tma.business.services.TrainingTypeService;
import com.sartorius.tma.client.dtos.request.TrainingSubTypeRequest;
import com.sartorius.tma.client.dtos.request.TrainingTypeRequest;
import com.sartorius.tma.client.dtos.response.TrainingOperatorResponse;
import com.sartorius.tma.client.dtos.response.TrainingSubTypeResponse;
import com.sartorius.tma.client.dtos.response.TrainingTypeResponse;
import com.sartorius.tma.dtos.PageDto;
import com.sartorius.tma.dtos.TrainingSubTypeDetails;
import com.sartorius.tma.dtos.TrainingTypeDto;
import com.sartorius.tma.dtos.TrainingTypeResponseDto;
import com.sartorius.tma.utils.Constants;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/training-type")
@CrossOrigin
@RequiredArgsConstructor
public class TrainingTypeController {

	private final TrainingTypeService trainingTypeService;
	private final TrainingSubTypeService trainingSubTypeService;

	@GetMapping()
	public List<TrainingTypeDto> getAllTrainingTypes() {
		return this.trainingTypeService.getAllTrainingType();
	}

	@GetMapping(value = "filtred")
	public PageDto<TrainingTypeDto> getAllSkillLevels(
			@RequestParam(name = "skill-area-label", required = false) String TrainingTypeLabel,
			@RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
			@RequestParam(name = "offset", required = false) Integer offset) {
		return trainingTypeService.getAllTrainingType(page, offset, TrainingTypeLabel);
	}

	@PostMapping("/add-or-update")
	public void addOrUpdateTrainingType(@RequestBody TrainingTypeRequest trainingTypeDto) {
		trainingTypeService.addOrUpdate(trainingTypeDto);
	}

	@GetMapping("get-paged-training-types")
	public PageDto<TrainingTypeResponseDto> getPagedTrainingTypes(@RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer offset, @RequestParam(required = false) String text) {
		return trainingTypeService.getPagedTrainingTypes(page, offset, text);
	}

	@GetMapping("/label")
	public String getTrainingTypeLabel(@RequestParam UUID trainingTypeUuid) {
		return trainingTypeService.getTrainingTypeLabel(trainingTypeUuid);
	}
	
	@GetMapping("/{uuid}")
	public TrainingTypeResponseDto getByUUID(@PathVariable("uuid") UUID trainingTypeUuid) {
		return trainingTypeService.getByUuid(trainingTypeUuid);
	}

	@PostMapping("/add-sub-training")
	public void addSubTraining(@RequestBody TrainingSubTypeRequest trainingSubTypeRequest) {
		trainingSubTypeService.add(trainingSubTypeRequest);
	}

	@PostMapping("/update-sub-training")
	public void updateSubTraining(@RequestBody TrainingSubTypeRequest trainingSubTypeRequest) {
		trainingSubTypeService.update(trainingSubTypeRequest);
	}

	@GetMapping("/get-sub-training-by-training-type-uuid")
	public List<TrainingSubTypeDetails> getTrainingSubTypesByTrainingType(@RequestParam UUID trainingTypeUuid, @RequestParam(required = false) String text) {
		return trainingSubTypeService.getTrainingSubTypesByTrainingType(trainingTypeUuid, text);
	}

	@DeleteMapping("delete-sub-training")
	public void deleteTrainingSubType(@RequestParam("trainingSubTypeUuid") UUID trainingSubTypeUuid) {
		trainingSubTypeService.deleteTrainingSubType(trainingSubTypeUuid);
	}

	@DeleteMapping
	public void deleteTrainingType(@RequestParam("trainingTypeUuid") UUID trainingTypeUuid) {
		trainingTypeService.deleteTrainingType(trainingTypeUuid);
	}

	@GetMapping("/all")
	public List<TrainingTypeResponse> getAllDetailedTrainingTypes(@RequestParam(required = false) String text) {
		return this.trainingTypeService.getAllDetailedTrainingTypes(text);
	}

	@GetMapping("/{trainingTypeUuid}/all-sub-trainings")
	public List<TrainingSubTypeResponse> getAllDetailedSubTrainingTypes(
			@PathVariable("trainingTypeUuid") UUID trainingTypeUuid) {
		return this.trainingTypeService.getAllDetailedSubTrainingTypes(trainingTypeUuid);
	}

	@PostMapping("/assign-training-type/{trainingTypeUuid}/{operatorUuid}")
	public void assignTrainingType(@PathVariable("trainingTypeUuid") UUID trainingTypeUuid,
			@PathVariable("operatorUuid") UUID operatorUuid) {
		trainingTypeService.assignTrainingType(trainingTypeUuid, operatorUuid);
	}

	@PostMapping("/assign-sub-training-type/{trainingSubTypeUuid}/{operatorUuid}")
	public ResponseEntity<String> assignSubTrainingType(@PathVariable("trainingSubTypeUuid") UUID trainingSubTypeUuid,
			@PathVariable("operatorUuid") UUID operatorUuid) {
		String response = trainingSubTypeService.assignSubTrainingType(trainingSubTypeUuid, operatorUuid);
		return response.equals(Constants.OK) ? ResponseEntity.ok(null)
				: ResponseEntity.status(HttpStatus.CONFLICT).body(response);
	}
	
	@GetMapping("/assigned-operators-to-training/{trainingId}")
	public List<TrainingOperatorResponse> getOperatorsForTraining(@PathVariable("trainingId") UUID trainingId) {
		return trainingTypeService.getOperators(trainingId);
	}
	
	@GetMapping("/assigned-operators-to-sub-training/{subTrainingId}")
	public List<TrainingOperatorResponse> getOperatorsForSubTraining(@PathVariable("subTrainingId") UUID subTrainingId) {
		return trainingSubTypeService.getOperators(subTrainingId);
	}
	@DeleteMapping("/cancel-training-type/{trainingTypeUuid}/{operatorUuid}")
	public ResponseEntity<String> cancelTrainingType(
			@PathVariable("trainingTypeUuid") UUID trainingTypeUuid,
			@PathVariable("operatorUuid") UUID operatorUuid
	) {
		System.out.println("Received cancel request for trainingTypeUuid: " + trainingTypeUuid + " and operatorUuid: " + operatorUuid);

		String response = trainingTypeService.cancelTrainingType(trainingTypeUuid, operatorUuid);

		return response.equals(Constants.OK) ? ResponseEntity.ok(null)
				: ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	}

}
