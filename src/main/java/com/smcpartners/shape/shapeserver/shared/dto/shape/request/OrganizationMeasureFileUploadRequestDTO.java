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
public class OrganizationMeasureFileUploadRequestDTO {
    private int fileUploadId;
    private String userId;
    private int orgId;
    private int orgMeasureId;
    private int measureEntityId;
    private String measureEntityName;
    private Date uploadDt;
    private String uploadedB64File;
    private String fileName;

    public OrganizationMeasureFileUploadRequestDTO(String userId, String measureEntityName, Date uploadDt, String uploadedB64File,
                                                   int orgId, int orgMeasureId, String fileName) {
        this.userId = userId;
        this.measureEntityName = measureEntityName;
        this.uploadDt = uploadDt;
        this.uploadedB64File = uploadedB64File;
        this.orgId = orgId;
        this.orgMeasureId = orgMeasureId;
        this.fileName = fileName;
    }
}
