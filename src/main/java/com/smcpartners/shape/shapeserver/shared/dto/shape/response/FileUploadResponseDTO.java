package com.smcpartners.shape.shapeserver.shared.dto.shape.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * Responsible: Returned to user with map of file name and upload status (failed, succeeded)<br/>
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
public class FileUploadResponseDTO {
    private Map<String, String> fileStatusLst;
    public FileUploadResponseDTO() {
        fileStatusLst = new HashMap();
    }
    public void addToMap(String fileName, String status) {
        fileStatusLst.put(fileName, status);
    }

}
