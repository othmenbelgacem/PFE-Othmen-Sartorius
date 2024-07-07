package com.sartorius.tma.persistence.entities;

import com.sartorius.tma.enumeration.MediaContext;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Document extends BaseEntity {

    private static final long serialVersionUID = -1593688143432377586L;

    private String mediaLabel;
    private Long mediaSize;
    private String mediaUrl;
    private String mediaContentType;
    @Enumerated(EnumType.STRING)
    private MediaContext mediaContext;
    private String originalName;

    @ManyToOne
    @JoinColumn(name = "training_session_id")
    private TrainingSession trainingSession;



    public void setTrainingSession(TrainingSession trainingSession) {
        this.trainingSession = trainingSession;
    }
}