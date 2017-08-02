package com.smcpartners.shape.shapeserver.shared.dto.shape;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * Responsible: DTO<br/>
 * 1.
 * <p>
 * Created by johndestefano on 9/11/15.
 * </p>
 * <p>
 * Changes:<b/>
 * </p>
 */
@Data
@NoArgsConstructor
public class UserDTO implements Serializable {
    private String id;
    private String role;
    private boolean admin;
    private Date createDt;
    private String createdBy;
    private Date modifiedDt;
    private String modifiedBy;
    private String password;
    private boolean active;
    private boolean resetPwd;
    private int organizationId;
    private String organizationName;
    private String firstName;
    private String lastName;
    private String email;
    private String questionOne;
    private String questionTwo;
    private String answerOne;
    private String answerTwo;
    private int userResetPwdChallenge;
}
