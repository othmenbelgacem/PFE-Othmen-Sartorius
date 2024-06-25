package com.sartorius.tma.rest;

import com.sartorius.tma.business.services.TeamService;
import com.sartorius.tma.business.services.UserService;
import com.sartorius.tma.client.dtos.request.ResetPasswordRequestDto;
import com.sartorius.tma.client.dtos.request.UserRequest;
import com.sartorius.tma.client.dtos.response.UserResponse;
import com.sartorius.tma.dtos.EmailVerifyDto;
import com.sartorius.tma.dtos.PageDto;
import com.sartorius.tma.dtos.UserDetails;
import com.sartorius.tma.enumeration.RoleCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;
  private final TeamService teamService;


  @GetMapping(value = "/list")
  public List<UserDetails> getAllUsers() {
    return this.userService.getAllUsers();
  }


  @GetMapping()
  public PageDto<UserDetails> getPagedUsers(
      @RequestParam(name = "page",required = false, defaultValue="0")
          Integer page,
      @RequestParam(name = "offset",required = false, defaultValue="5")
          Integer offset,
          @RequestParam(name = "userType",required = false)
      RoleCode userType,
      @RequestParam(name = "text",required = false)
      String text) {
    return this.userService.getPagedUsers(page,offset,userType, text);
  }

  @GetMapping("user-profile")
  public UserDetails getUserProfile() {
    return this.userService.getUserInfo();
  }
  @PostMapping(value="/add-new-user",produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public void saveOwner(@ModelAttribute UserRequest userRequest) {
	  if(userRequest.getUserPhoneNumber().equals("undefined") || userRequest.getUserPhoneNumber().isEmpty()) userRequest.setUserPhoneNumber(null);
       this.userService.saveUser(userRequest);
  }

  @PatchMapping(value="/update-user",produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public void updateUser(@ModelAttribute UserRequest userRequest) throws Exception {
	  if(userRequest.getUserPhoneNumber().equals("undefined") || userRequest.getUserPhoneNumber().equals("null") || userRequest.getUserPhoneNumber().isEmpty()) userRequest.setUserPhoneNumber(null);
    this.userService.updateUser(userRequest);
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
    public ResponseEntity<Void> deleteUser(
            @RequestParam(value = "user-uuid") UUID userUuid) {
        userService.deleteUser(userUuid);
        return ResponseEntity.noContent()
                .build();  // Utilisez noContent() pour un statut 204
    }

    @PostMapping(value = "/reset-password", consumes = MediaType.APPLICATION_JSON_VALUE)
    public boolean resetPassword(
            @RequestBody ResetPasswordRequestDto resetPasswordRequestDto) {
        return userService.resetPassword(resetPasswordRequestDto);
    }

  @GetMapping(value = "/user-team-member")
  public List<UserDetails> getTeamMembers() {
    return userService.getTeamMembers();
  }
  
  @GetMapping(value = "/all-operators")
  public List<UserResponse> getAllOperators() {
    return userService.getAllOperators();
  }


}
