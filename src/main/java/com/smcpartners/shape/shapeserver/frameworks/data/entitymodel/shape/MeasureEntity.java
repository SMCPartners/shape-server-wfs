package com.smcpartners.shape.shapeserver.frameworks.data.entitymodel.shape;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Responsible:</br>
 * 1.  Entity</br>
 * <p>
 * Created by johndestefano on 3/15/16.
 * </p>
 * <p>
 * Changes:</br>
 * 1. </br>
 * </p>
 */
@Entity
@Table(name = "measure", schema = "", catalog = "shape")
@NamedQueries({
        @NamedQuery(name = "Measure.findAll",
                query = "SELECT OBJECT(m) FROM MeasureEntity m"),
        @NamedQuery(name = "Measure.findAllById",
                query = "SELECT OBJECT(m) FROM MeasureEntity m WHERE m.id = :id")
})
@Data
@NoArgsConstructor
public class MeasureEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, insertable = true, updatable = true)
    private int id;

    @Basic
    @Column(name = "name", nullable = true, insertable = true, updatable = true, length = 255)
    private String name;

    @Basic
    @Column(name = "Description", nullable = true, insertable = true, updatable = true, length = 1024)
    private String description;

    @Basic
    @Column(name = "nqf_id", nullable = true, insertable = true, updatable = true, length = 75)
    private String nqfId;

    @Basic
    @Column(name = "numerator_description", nullable = true, insertable = true, updatable = true, length = 1024)
    private String numeratorDescription;

    @Basic
    @Column(name = "denominator_description", nullable = true, insertable = true, updatable = true, length = 1024)
    private String denominatorDescription;

    @Basic
    @Column(name = "exclusions_description", nullable = true, insertable = true, updatable = true, length = 1024)
    private String exclusionsDescription;

    @Basic
    @Column(name = "well_controlled_numerator", columnDefinition = "TINYINT", length = 1, nullable = true, insertable = true, updatable = true)
    private boolean wellControlledNumerator;

    @Basic
    @Column(name = "selected", columnDefinition = "TINYINT", length = 1, nullable = false, insertable = true, updatable = true)
    private boolean selected;

    @Basic
    @Column(name = "active", columnDefinition = "TINYINT", length = 1, nullable = false, insertable = true, updatable = true)
    private boolean active;
}
