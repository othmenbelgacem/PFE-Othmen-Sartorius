package com.sartorius.tma.business.services;

import com.sartorius.tma.dtos.statics.TrainingSessionStatisticDto;
import com.sartorius.tma.dtos.statics.TrainingRequestCountDTO;
import com.sartorius.tma.persistence.entities.Role;
import com.sartorius.tma.persistence.entities.User;
import com.sartorius.tma.persistence.repositories.TrainingRequestRepository;
import com.sartorius.tma.persistence.repositories.TrainingSessionRepository;
import com.sartorius.tma.persistence.repositories.UserRepository;
import com.sartorius.tma.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.sartorius.tma.enumeration.RoleCode.*;


@Service
@Slf4j
@RequiredArgsConstructor
public class TrainingSessionStatisticService {

    private final TrainingSessionRepository trainingSessionRepository;
    private final TrainingRequestRepository trainingRequestRepository;
    private final UserRepository userRepository;

    public TrainingSessionStatisticDto getStatisticData() {
        UUID currentUserId = SecurityUtil.getCurrentUserUuid();
        Optional<User> userByUuid = userRepository.findByUuid(currentUserId);
        if (userByUuid.isPresent()) {
            Role role = userByUuid.get().getRole();
            if (MANAGER.equals(role.getRoleCode())) {
                return getManagerStatisticData(currentUserId);
            } else if (TRAINER.equals(role.getRoleCode())) {
                return getTrainerStatisticData(currentUserId);
            } else if (ADMINISTRATOR.equals(role.getRoleCode())) {
                return getAdminStatisticData();
            }else if (OPERATOR.equals(role.getRoleCode())) {
                return getCollaboratorStatisticData(currentUserId);
            }
        }
        return null;
    }

    public TrainingSessionStatisticDto getCollaboratorStatisticData(UUID collaboratorId) {
        return TrainingSessionStatisticDto.builder()
                .totalSession(trainingSessionRepository.countAllSessionsByOperator(collaboratorId))
                .totalInProgressSession(trainingSessionRepository.countInProgressSessionsByOperator(collaboratorId))
                .totalDoneSession(trainingSessionRepository.countDoneSessionsByOperator(collaboratorId))
                .totalPlannedSession(trainingSessionRepository.countPlannedSessionsByOperator(collaboratorId))
                .sessionStaticByTrainerOrOperator(null)
                .build();
    }

    public TrainingSessionStatisticDto getAdminStatisticData() {
        return TrainingSessionStatisticDto.builder()
                .totalSession(trainingSessionRepository.countAllSessions())
                .totalInProgressSession(trainingSessionRepository.countInProgressSessions())
                .totalDoneSession(trainingSessionRepository.countDoneSessions())
                .totalPlannedSession(trainingSessionRepository.countPlannedSessions())
                .rejectedSession(trainingSessionRepository.countRejectedSessions())
                .sessionStaticByTrainerOrOperator(trainingSessionRepository.findTrainerSessionStatisticsByTrainer())
                .build();
    }

    public TrainingSessionStatisticDto getTrainerStatisticData(
            UUID trainerId) {
        return trainingSessionRepository.findTrainerSessionStatisticsByTrainerId(
                trainerId);
    }

    public TrainingSessionStatisticDto getManagerStatisticData(
            UUID teamLeaderId) {
        return TrainingSessionStatisticDto.builder()
                .totalSession(trainingRequestRepository.countAllSessions(
                        teamLeaderId))
                .totalInProgressSession(
                        trainingRequestRepository.countInProgressSessions(
                                teamLeaderId))
                .totalDoneSession(trainingRequestRepository.countDoneSessions(
                        teamLeaderId))
                .totalPlannedSession(
                        trainingRequestRepository.countPlannedSessions(
                                teamLeaderId))
                .sessionStaticByTrainerOrOperator(null)
                .build();
    }
    public Map<String, Integer> getSessionStatsByMonth(int year, int month) {
        Map<String, Integer> stats = new HashMap<>();
        stats.put("done", trainingSessionRepository.countDoneSessionsByMonth(year, month));

        stats.put("inProgress", trainingSessionRepository.countInProgressSessions1());

        return stats;
    }
    public List<TrainingRequestCountDTO> getTop10TrainingRequests() {
        return trainingRequestRepository.findTop10TrainingRequests();
    }
}
