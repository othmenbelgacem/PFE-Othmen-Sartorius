package com.sartorius.tma.rest;

import java.util.List;
import java.util.UUID;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sartorius.tma.business.services.TrainerService;
import com.sartorius.tma.business.services.UserService;
import com.sartorius.tma.client.dtos.request.ResetPasswordRequestDto;
import com.sartorius.tma.client.dtos.request.TrainerRequest;
import com.sartorius.tma.client.dtos.request.UserRequest;
import com.sartorius.tma.dtos.EmailVerifyDto;
import com.sartorius.tma.dtos.PageDto;
import com.sartorius.tma.dtos.TrainerDetails;
import com.sartorius.tma.dtos.TrainerDto;
import com.sartorius.tma.dtos.UserDetails;
import com.sartorius.tma.enumeration.RoleCode;

import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin
@RequestMapping("/trainer")
@RequiredArgsConstructor
public class TrainerController {
  private final TrainerService trainerService;
  private final UserService userService;


  @GetMapping(value = "/list")
  public List<TrainerDetails> getAllUsers() {
    return this.trainerService.getAllTrainers();
  }


  @GetMapping()
  public PageDto<TrainerDetails> getPagedUsers(
      @RequestParam(name = "page",required = false)
          Integer page,
      @RequestParam(name = "offset",required = false)
          Integer offset,
          @RequestParam(name = "userType",required = false, defaultValue = "TRAINER")
      RoleCode userType) {
    return this.trainerService.getPagedUsers(page,offset,userType);
  }

  @GetMapping("user-profile")
  public TrainerDetails getUserProfile() {
    return this.trainerService.getUserInfo();
  }
  @PostMapping(value="/add-new-user",produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public void saveOwner(@ModelAttribute TrainerRequest userRequest) {
	  if(userRequest.getUserPhoneNumber().equals("undefined") || userRequest.getUserPhoneNumber().isEmpty()) userRequest.setUserPhoneNumber(null);
       this.trainerService.saveUser(userRequest);
  }
  @GetMapping("/is-matricule-unique/{identifier}")
  public boolean isMatriculeUnique(@PathVariable String identifier) {
    return trainerService.isMatriculeUnique(identifier);
  }
  @PatchMapping(value="/update-user",produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public void updateUser(@ModelAttribute TrainerRequest userRequest) throws Exception {
	  if(userRequest.getUserPhoneNumber().equals("undefined") || userRequest.getUserPhoneNumber().equals("null") || userRequest.getUserPhoneNumber().isEmpty()) userRequest.setUserPhoneNumber(null);
    this.trainerService.updateUser(userRequest);
  }

  
  @PostMapping(value = "/verify")
  public Boolean checkUserEmail(@NotEmpty @Email @RequestBody EmailVerifyDto emailVerify) {
    return userService.checkUserEmail(emailVerify.getUserEmail());
  }

  @GetMapping(value = "/verify-phone-number")
  public Boolean checkUserPhoneNumber(@NotEmpty @RequestParam("user_phone_number") String userPhoneNumber) {
    return userService.checkUserPhoneNumber(userPhoneNumber);
  }

  @DeleteMapping
  public ResponseEntity<Void> deleteUser(@RequestParam(value = "user-uuid") UUID userUuid){
     trainerService.deleteTrainer(userUuid);
    return ResponseEntity.noContent()
            .build();  // Utilisez noContent() pour un statut 204
  }

  @GetMapping("/all")
  public List<TrainerDto> getAllTrainers() {
      return trainerService.retrieveAllTrainers();
  }
  
  @GetMapping("/by-training-type")
  public List<TrainerDto> getTrainersByTrainingTypeUuid(@RequestParam UUID trainingTypeUuid) {
      return trainerService.getTrainersByTrainingTypeUuid(trainingTypeUuid);
  }
  
  @GetMapping("/by-training-sub-type")
  public List<TrainerDto> getTrainersByTrainingSubTypeUuid(@RequestParam UUID trainingSubTypeUuid) {
      return trainerService.getTrainersByTrainingSubTypeUuid(trainingSubTypeUuid);
  }

}
