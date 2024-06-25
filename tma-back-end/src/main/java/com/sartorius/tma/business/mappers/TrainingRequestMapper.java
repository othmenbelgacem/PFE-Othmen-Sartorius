package com.sartorius.tma.business.mappers;

import com.sartorius.tma.dtos.training_request.BaseEntityDTO;
import com.sartorius.tma.dtos.training_request.TrainingRequestDTO;
import com.sartorius.tma.persistence.entities.TrainingRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TrainingRequestMapper {
    public TrainingRequestDTO trainingResponseToDTO(TrainingRequest trainingRequest) {

        TrainingRequestDTO.TrainingRequestDTOBuilder trainingRequestBuilder = TrainingRequestDTO
                .builder()
                .uuid(trainingRequest.getUuid())
                .requestDate(trainingRequest.getRequestDate())
                .status(trainingRequest.getStatus());


            if (Objects.nonNull(trainingRequest.getTeamLeader())) {
                trainingRequestBuilder.teamLeader(
                        buildBaseEntityDTO(
                                trainingRequest.getTeamLeader().getUuid(),
                                String.format("%s %s", trainingRequest.getTeamLeader().getUserFirstName(), trainingRequest.getTeamLeader().getUserLastName())
                ));
            }

            if (Objects.nonNull(trainingRequest.getOperator())) {
                trainingRequestBuilder.operator(
                        buildBaseEntityDTO(
                                trainingRequest.getOperator().getUuid(),
                                String.format("%s %s", trainingRequest.getOperator().getUserFirstName(), trainingRequest.getOperator().getUserLastName())
                ));
            }

            if (Objects.nonNull(trainingRequest.getTrainingType())) {
                trainingRequestBuilder.trainingType(
                        buildBaseEntityDTO(
                                trainingRequest.getTrainingType().getUuid(),
                                trainingRequest.getTrainingType().getLabel()
                        )
                );
            }

            if (Objects.nonNull(trainingRequest.getTrainingSubType())) {
                trainingRequestBuilder.trainingSubType(
                        buildBaseEntityDTO(
                                trainingRequest.getTrainingSubType().getUuid(),
                                trainingRequest.getTrainingSubType().getLabel()
                        )
                );
            }
        return trainingRequestBuilder.build();
    }

    private BaseEntityDTO buildBaseEntityDTO(UUID uuid, String name) {
        return BaseEntityDTO
                .builder()
                .uuid(uuid)
                .name(name)
                .build();
    }
}
