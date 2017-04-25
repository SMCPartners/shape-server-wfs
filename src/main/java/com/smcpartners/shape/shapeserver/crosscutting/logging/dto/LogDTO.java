package com.smcpartners.shape.shapeserver.crosscutting.logging.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Responsibility: </br>
 * 1. </br>
 * 2. </br>
 * Created By: johndestefano
 * Date: 4/25/17
 */
@Data
@NoArgsConstructor
public class LogDTO {
    private String uuid;
    private String requestPath;
    private String requestHeader;
    private String requestEntity;
    private Date requestDt;
    private String responseHeader;
    private String responseEntity;
    private Date responseDt;
    private String user;
}
