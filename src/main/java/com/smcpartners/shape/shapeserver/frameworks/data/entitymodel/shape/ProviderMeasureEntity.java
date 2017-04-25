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
@Table(name = "provider_measure", schema = "", catalog = "shape")
@Data
@NoArgsConstructor
public class ProviderMeasureEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Basic
    @Column(name = "numerator_value", nullable = true, insertable = true, updatable = true)
    private Integer numeratorValue;

    @Basic
    @Column(name = "denominator_value", nullable = true, insertable = true, updatable = true)
    private Integer denominatorValue;

    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "rpDate", nullable = true, insertable = true, updatable = true)
    private Date rpDate;

    @ManyToOne
    @JoinColumn(name = "measure_id", referencedColumnName = "id", nullable = false)
    private MeasureEntity measureByMeasureId;

    @ManyToOne
    @JoinColumn(name = "provider_id", referencedColumnName = "id", nullable = false)
    private ProviderEntity providerById;
}
