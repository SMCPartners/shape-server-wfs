package com.smcpartners.shape.shapeserver.frameworks.data.entitymodel.shape;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * Responsible:</br>
 * 1.  Entity </br>
 * <p>
 * Created by johndestefano on 10/28/15.
 * </p>
 * <p>
 * Changes:</br>
 * 1. </br>
 * </p>
 */
@Entity
@Table(name = "organization", schema = "", catalog = "shape")
@NamedQueries({
        @NamedQuery(name = "Organization.findAll",
                query = "SELECT OBJECT(o) FROM OrganizationEntity o")
})
@Data
@NoArgsConstructor
public class OrganizationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, insertable = true, updatable = true)
    private int id;

    @Basic
    @Column(unique = true, name = "name", nullable = true, insertable = true, updatable = true, length = 255)
    private String name;

    @Basic
    @Column(name = "active", columnDefinition = "TINYINT", length = 1, nullable = false, insertable = true, updatable = true)
    private boolean active;

    @Basic
    @Column(name = "address_street", nullable = true, insertable = true, updatable = true, length = 255)
    private String addressStreet;

    @Basic
    @Column(name = "address_state", nullable = true, insertable = true, updatable = true, length = 45)
    private String addressState;

    @Basic
    @Column(name = "address_city", nullable = true, insertable = true, updatable = true, length = 45)
    private String addressCity;

    @Basic
    @Column(name = "address_zip", nullable = true, insertable = true, updatable = true)
    private String addressZip;

    @Basic
    @Column(name = "phone", nullable = true, insertable = true, updatable = true)
    private String phone;

    @Basic
    @Column(name = "primary_contact_name", nullable = true, insertable = true, updatable = true, length = 45)
    private String primaryContactName;

    @Basic
    @Column(name = "primary_contact_email", nullable = true, insertable = true, updatable = true, length = 45)
    private String primaryContactEmail;

    @Basic
    @Column(name = "primary_contact_role", nullable = true, insertable = true, updatable = true, length = 45)
    private String primaryContactRole;

    @Basic
    @Column(name = "primary_contact_phone", nullable = true, insertable = true, updatable = true, length = 45)
    private String primaryContactPhone;

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
}
