package com.smcpartners.shape.shapeserver.shared.dto.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Responsible: Holds data for mail messages.</br>
 * <p>
 * Created by johndestefano on 10/6/15.
 * </p>
 * <p>
 * Changes:</br>
 * 1. </br>
 * </p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MailDTO implements Serializable {
    /**
     * How the email should be sent to
     */
    private String toEmail;

    /**
     * Email subject
     */
    private String subject;

    /**
     * Message body of email
     */
    private String message;
}
