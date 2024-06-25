package com.sartorius.tma.business.services;

import org.springframework.stereotype.Service;

import com.sartorius.tma.persistence.repositories.TrainingRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class TrainingService {

	private final TrainingRepository trainingRepository;

	

}
