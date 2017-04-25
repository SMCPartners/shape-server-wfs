package com.smcpartners.shape.shapeserver.frameworks.data.entitymodel.shape;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * Responsible:</br>
 * 1.  Entity</br>
 * <p>
 * Created by johndestefano on 10/28/15.
 * </p>
 * <p>
 * Changes:</br>
 * 1. </br>
 * </p>
 */
@Entity
@Table(name = "provider", schema = "", catalog = "shape")
@NamedQueries({
        @NamedQuery(name = "Provider.findAll",
                query = "SELECT OBJECT(p) FROM ProviderEntity p"),
        @NamedQuery(name = "Provider.findByOrg",
                query = "SELECT OBJECT(p) FROM ProviderEntity p " +
                        "WHERE p.organizationById = :org")
})
@Data
@NoArgsConstructor
public class ProviderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, insertable = true, updatable = true)
    private int id;

    @Basic
    @Column(name = "active", columnDefinition = "TINYINT", length = 1, nullable = false, insertable = true, updatable = true)
    private boolean active;

    @Basic
    @Column(name = "name", nullable = true, insertable = true, updatable = true, length = 45)
    private String name;

    @Basic
    @Column(name = "npi", nullable = true, insertable = true, updatable = true)
    private Integer npi;

    @Basic
    @Column(name = "createdBy", nullable = true, insertable = true, updatable = true, length = 45)
    private String createdBy;

    @Basic
    @Column(name = "modifiedDt", nullable = false, insertable = true, updatable = true)
    private Date modifiedDt;

    @Basic
    @Column(name = "modifiedBy", nullable = true, insertable = true, updatable = true, length = 45)
    private String modifiedBy;

    @OneToOne()
    @JoinColumn(name="organizationId", referencedColumnName = "id", nullable = false)
    private OrganizationEntity organizationById;
}
