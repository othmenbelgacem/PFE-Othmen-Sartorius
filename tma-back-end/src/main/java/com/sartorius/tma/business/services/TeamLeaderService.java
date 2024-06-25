package com.sartorius.tma.business.services;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.sartorius.tma.persistence.entities.TeamLeader;
import com.sartorius.tma.persistence.entities.User;
import com.sartorius.tma.persistence.repositories.TeamLeaderRepository;
import com.sartorius.tma.utils.SecurityUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class TeamLeaderService {

	private final TeamLeaderRepository teamLeaderRepository;
	
	public TeamLeader getCurrentTeamLeader() {
		UUID currentuserId = SecurityUtil.getCurrentUserUuid();
		TeamLeader user = teamLeaderRepository.findByUuid(currentuserId);
		return user;
	}

}
