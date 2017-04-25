package com.smcpartners.shape.shapeserver.shared.dto.shape.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Responsible: Data transfer object for uploaded file.<br/>
 * 1. <br/>
 * <p>
 * Created by johndestefano on 5/9/16.
 * </p>
 * <p>
 * Changes:<br/>
 * 1. <br/>
 * </p>
 */
@Data
@NoArgsConstructor
public class FileUploadRequestDTO {
    private int fileUploadId;
    private String userId;
    private int measureEntityId;
    private Date uploadDt;
    private String uploadedB64File;

    public FileUploadRequestDTO(String userId, int measureEntityId, Date uploadDt, String uploadedB64File) {
        this.userId = userId;
        this.measureEntityId = measureEntityId;
        this.uploadDt = uploadDt;
        this.uploadedB64File = uploadedB64File;
    }
}
