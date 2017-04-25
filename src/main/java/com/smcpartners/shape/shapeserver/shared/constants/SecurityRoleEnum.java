package com.smcpartners.shape.shapeserver.shared.constants;

/**
 * SecurityRoleEnum.java<br/>
 * Responsibilities:<br/>
 * 1. Used to handle security roles<br/>
 *
 * @author John DeStefano
 * @version 1.0
 * @since May 26, 2013
 */
public enum SecurityRoleEnum {
    ADMIN("ADMIN", 8), DPH_USER("DPH_USER", 6), ORG_ADMIN("ORG_ADMIN", 4), REGISTERED("REGISTERED", 2);

    private String name;
    private int precedence;

    /**
     * Constructor
     *
     * @param name
     * @param precedence
     */
    SecurityRoleEnum(String name, int precedence) {
        this.name = name;
        this.precedence = precedence;
    }

    public String getName() {
        return name;
    }

    public int getPrecedence() {
        return precedence;
    }


}
