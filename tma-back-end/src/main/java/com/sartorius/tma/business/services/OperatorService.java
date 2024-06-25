package com.sartorius.tma.business.services;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.sartorius.tma.persistence.entities.Operator;
import com.sartorius.tma.persistence.repositories.OperatorRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class OperatorService {

	private final OperatorRepository operatorRepository;
	
	public Operator findByUuid(UUID operatorUuid) {
		return operatorRepository.findByUuid(operatorUuid);
	}

}
