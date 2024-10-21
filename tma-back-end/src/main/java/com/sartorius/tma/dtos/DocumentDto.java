package com.sartorius.tma.dtos;

import com.sartorius.tma.persistence.entities.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentDto {
    private Long id;
    private String mediaLabel;
    private Long mediaSize;
    private String mediaUrl;
    private String mediaContentType;
    private String originalName;

    public DocumentDto(Document document) {
        this.id = document.getId();
        this.mediaLabel = document.getMediaLabel();
        this.mediaSize = document.getMediaSize();
        this.mediaUrl = document.getMediaUrl();
        this.mediaContentType = document.getMediaContentType();
        this.originalName = document.getOriginalName();
    }
}
