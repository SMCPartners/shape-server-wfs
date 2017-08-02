package com.smcpartners.shape.shapeserver.shared.dto.shape.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Responsibility: DTO</br>
 * 1. </br>
 * 2. </br>
 * Created By: johndestefano
 * Date: 7/24/17
 */
@Data
@NoArgsConstructor
public class OrganizationStratificationFileUploadRequestDTO {

    private int orgStratUploadId;
    private String userId;
    private int orgId;
    private int orgStratId;
    private Date uploadDt;
    private String uploadedB64File;
    private String fileName;

    public OrganizationStratificationFileUploadRequestDTO(String userId, Date uploadDt, String uploadedB64File,
                                                          int orgId, int orgStartId, String fileName) {
        this.userId = userId;
        this.uploadDt = uploadDt;
        this.uploadedB64File = uploadedB64File;
        this.orgId = orgId;
        this.fileName = fileName;
        this.orgStratId = orgStartId;
    }
}
