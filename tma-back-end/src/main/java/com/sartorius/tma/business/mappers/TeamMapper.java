package com.sartorius.tma.business.mappers;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.sartorius.tma.client.dtos.request.TeamRequest;
import com.sartorius.tma.persistence.entities.Team;
import com.sartorius.tma.persistence.repositories.TeamRepository;

import lombok.RequiredArgsConstructor;


@Component
@RequiredArgsConstructor
public class TeamMapper {
  private final UserMapper userMapper;
  private final TeamRepository teamRepository;
  public TeamRequest toTeamDetailsResponse(Team team) {
    return new TeamRequest(team.getUuid(),team.getTeamName(),team.getMembers().stream().map(userMapper::toUserDetailsResponse).collect(Collectors.toList()),
        userMapper.toUserDetailsResponse(team.getManager()),0d
        );
  }

  public Team toTeam(TeamRequest teamRequest){
    return teamRepository.findByUuid(teamRequest.getTeamUuid());
  }
}
