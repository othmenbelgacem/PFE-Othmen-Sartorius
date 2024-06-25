package com.sartorius.tma.business.services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.sartorius.tma.business.mappers.TeamMapper;
import com.sartorius.tma.business.mappers.UserMapper;
import com.sartorius.tma.client.dtos.request.TeamRequest;
import com.sartorius.tma.dtos.PageDto;
import com.sartorius.tma.persistence.entities.Team;
import com.sartorius.tma.persistence.entities.User;
import com.sartorius.tma.persistence.repositories.TeamRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeamService {

	private final TeamRepository teamRepository;
	private final UserMapper userMapper;
	private final TeamMapper teamMapper;

	public Team addOrUpdateTeam(TeamRequest teamRequest) {
		Team team;
		if (teamRequest.getTeamUuid() != null) {
			team = teamRepository.findByUuid(teamRequest.getTeamUuid());
			if (team != null) {
				team.setTeamName(teamRequest.getTeamName());
				team.setManager(userMapper.toUser(teamRequest.getManager()));
			}
		} else {
			team = new Team();
			team.setTeamName(teamRequest.getTeamName());
			team.setManager(userMapper.toUser(teamRequest.getManager()));
		}
		return teamRepository.save(team);
	}

	public Team addMembers(TeamRequest teamRequest) {
		Team team = teamRepository.findByUuid(teamRequest.getTeamUuid());
		if (team != null) {
			team.getMembers().removeAll(team.getMembers());
			team.getMembers()
					.addAll(teamRequest.getMembers().stream().map(userMapper::toOPerator).collect(Collectors.toList()));
			teamRepository.save(team);
		}
		return team;
	}

	public PageDto<TeamRequest> getPagedTeams(int page, int offset) {
		Pageable pageable = PageRequest.of(page, offset, Sort.by("teamName").ascending());
		Page<Team> teams = this.teamRepository.findAll(pageable);
		return new PageDto<TeamRequest>(
				teams.getContent().stream().map(teamMapper::toTeamDetailsResponse).collect(Collectors.toList()),
				teams.getTotalElements());
	}

	public void deleteTeam(UUID teamUuid) {
		Team team = teamRepository.findByUuid(teamUuid);
		if (team != null) {
			this.teamRepository.deleteById(team.getId());
		}
	}

	public TeamRequest getTeamByUuid(UUID uuid) {
		return teamMapper.toTeamDetailsResponse(teamRepository.findByUuid(uuid));
	}

	public Team findTeamByUuid(UUID uuid) {
		return teamRepository.findByUuid(uuid);
	}

	public List<TeamRequest> getAllTeams() {
		return this.teamRepository.findAll().stream().map(teamMapper::toTeamDetailsResponse)
				.collect(Collectors.toList());
	}

	public Team getTeamByManager(User manager) {
		return teamRepository.findByManager(manager);
	}

	public Team getTeamByMember(User collaborator) {
		return teamRepository.findByMember(collaborator.getId()).orElse(null);
	}

}
