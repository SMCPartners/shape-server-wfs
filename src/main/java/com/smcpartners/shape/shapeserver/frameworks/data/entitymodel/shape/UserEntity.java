package com.smcpartners.shape.shapeserver.frameworks.data.entitymodel.shape;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;

/**
 * Responsible:</br>
 * 1. Entity </br>
 * <p>
 * Created by johndestefano on 10/28/15.
 * </p>
 * <p>
 * Changes:</br>
 * 1. </br>
 * </p>
 */
@Entity
@Table(name = "user", schema = "", catalog = "shape",
    indexes = @Index(name = "user_email_idx", columnList = "email", unique = true))
@NamedQueries({
        @NamedQuery(name = "User.findAll",
                query = "SELECT OBJECT(u) FROM UserEntity u"),
        @NamedQuery(name = "User.findByOrg",
                query = "SELECT OBJECT(u) FROM UserEntity u " +
                        "where u.organizationById = :org"),
        @NamedQuery(name = "User.findByEmail",
                query = "SELECT OBJECT(u) FROM UserEntity u " +
                        "where u.email = :emailAddress"),
})
@Data
@NoArgsConstructor
public class UserEntity {

    @Id
    @Column(name = "id", nullable = false, insertable = true, updatable = true, length = 25)
    private String id;

    @Basic
    @Column(name = "role", nullable = false, insertable = true, updatable = true, length = 45)
    private String role;

    @Basic
    @Column(name = "firstName", nullable = true, insertable = true, updatable = true, length = 45)
    private String firstName;

    @Basic
    @Column(name = "lastName", nullable = false, insertable = true, updatable = true, length = 75)
    private String lastName;

    @Basic
    @Column(name = "email", nullable = true, insertable = true, updatable = true, length = 45)
    private String email;

    @Basic
    @Column(name = "admin", columnDefinition = "TINYINT", length = 1, nullable = false, insertable = true, updatable = true)
    private boolean admin;

    @Basic
    @Column(name = "createDt", nullable = false, insertable = true, updatable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDt;

    @Basic
    @Column(name = "createdBy", nullable = true, insertable = true, updatable = true, length = 45)
    private String createdBy;

    @Basic
    @Column(name = "modifiedDt", nullable = false, insertable = true, updatable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDt;

    @Basic
    @Column(name = "modifiedBy", nullable = true, insertable = true, updatable = true, length = 45)
    private String modifiedBy;

    @Basic
    @Column(name = "passwordSalt", nullable = false, insertable = true, updatable = true, length = 100)
    private String passwordSalt;

    @Basic
    @Column(name = "passwordDigest", nullable = false, insertable = true, updatable = true, length = 100)
    private String passwordDigest;

    @Basic
    @Column(name = "active", columnDefinition = "TINYINT", length = 1, nullable = false, insertable = true, updatable = true)
    private boolean active;

    @Basic
    @Column(name = "resetPwd", columnDefinition = "TINYINT", length = 1, nullable = false, insertable = true, updatable = true)
    private boolean resetPwd;

    @Basic
    @Column(name = "generatedPwd", columnDefinition = "TINYINT", length = 1, nullable = false, insertable = true, updatable = true)
    private boolean generatedPwd;

    @Basic
    @Column(name = "generatedPwdDt", nullable = false, insertable = true, updatable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date generatedPwdDt;

    @Basic
    @Column(name = "question_1", nullable = true, insertable = true, updatable = true, length = 45)
    private String questionOne;

    @Basic
    @Column(name = "question_2", nullable = true, insertable = true, updatable = true, length = 45)
    private String questionTwo;

    @Basic
    @Column(name = "answer_1", nullable = true, insertable = true, updatable = true, length = 45)
    private String answerOne;

    @Basic
    @Column(name = "answer_2", nullable = true, insertable = true, updatable = true, length = 45)
    private String answerTwo;

    /**
     * This field stores the password challenge question that was last sent to the
     * user when they request a password reset
     */
    @Basic
    @Column(name = "UserResetPwdChallenge", columnDefinition = "INT(11) default 0", nullable = true, insertable = true, updatable = true)
    private int userResetPwdChallenge;

    @OneToMany(mappedBy = "userByUserId")
    private Collection<UserProviderEntity> userProvidersById;

    @OneToOne()
    @JoinColumn(name="organizationId", referencedColumnName = "id", nullable = false)
    private OrganizationEntity organizationById;
}
