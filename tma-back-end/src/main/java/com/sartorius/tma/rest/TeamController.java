package com.sartorius.tma.rest;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sartorius.tma.business.services.TeamService;
import com.sartorius.tma.client.dtos.request.TeamRequest;
import com.sartorius.tma.dtos.PageDto;
import com.sartorius.tma.persistence.entities.Team;

import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin
@RequestMapping("/team")
@RequiredArgsConstructor
public class TeamController {

	 private final TeamService teamService;

	  @PostMapping(value="/add-new-team")
	  public void saveTeam(@RequestBody TeamRequest teamRequest) {
	    teamService.addOrUpdateTeam(teamRequest);
	  }

	  @PatchMapping(value="/add-members")
	  public void addTeamMembers(@RequestBody TeamRequest teamRequest) {
	    teamService.addMembers(teamRequest);
	  }


	  @GetMapping()
	  public PageDto<TeamRequest> getPagedTeams(
	      @RequestParam(name = "page",required = false)
	          Integer page,
	      @RequestParam(name = "offset",required = false)
	          Integer offset) {
	    return this.teamService.getPagedTeams(page,offset);
	  }

	  @DeleteMapping
	  public void deleteTeam(@RequestParam(value = "team-uuid") UUID teamUuid){
	    teamService.deleteTeam(teamUuid);
	  }

	  @GetMapping("/team-by-uuid")
	  public TeamRequest getOffersByUuid(@RequestParam("team-uuid") UUID teamUuid) {
	    return this.teamService.getTeamByUuid(teamUuid);
	  }
	  @GetMapping(value = "/list")
	  public List<TeamRequest> getAllTeams() {
	    return this.teamService.getAllTeams();
	  }



}
