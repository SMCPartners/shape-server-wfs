package com.smcpartners.shape.shapeserver.shared.dto.common;

import com.smcpartners.shape.shapeserver.shared.constants.SecurityRoleEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.enterprise.context.RequestScoped;

/**
 * Responsibility: Request scoped user information data holder</br>
 * 1. </br>
 * 2. </br>
 * Created By: johndestefano
 * Date: 4/18/17
 */
@Data
@NoArgsConstructor
@RequestScoped
public class UserExtras {
    private SecurityRoleEnum role;
    private String userId;
    private int orgId;
}
