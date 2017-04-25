package com.smcpartners.shape.shapeserver.shared.dto.shape.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Responsible:<br/>
 * 1. DTO
 * <p>
 * Created by johndestefano on 11/2/15.
 * <p>
 * Changes:<b/>
 */
@Data
@NoArgsConstructor
public class CreateUserRequestDTO implements Serializable {
    private String id;
    private String role;
    private String password;
    private String newPassword;
    private int organizationId;
    private String firstName;
    private String lastName;
    private String email;
    private String questionOne;
    private String questionTwo;
    private String answerOne;
    private String answerTwo;
}
