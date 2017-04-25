package com.smcpartners.shape.shapeserver.frameworks.data.entitymodel.shape;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * Responsible: Entity for saving measure upload information<br/>
 * 1. <br/>
 * <p>
 * Created by johndestefano on 5/9/16.
 * </p>
 * <p>
 * Changes:<br/>
 * 1. <br/>
 * </p>
 */
@Entity
@Table(name = "file_upload", schema = "", catalog = "shape")
@Data
@NoArgsConstructor
public class FileUploadEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fileUploadId", nullable = false, insertable = true, updatable = true)
    private int fileUploadId;

    @Basic
    @Column(name = "uploadDt", nullable = false, insertable = true, updatable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date uploadDt;

    @Lob
    @Column(name = "uploadedB64File", nullable = true, insertable = true, updatable = true)
    private String uploadedB64File;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = true)
    private UserEntity userByUserId;

    @ManyToOne
    @JoinColumn(name = "measure_id", referencedColumnName = "id", nullable = false)
    private MeasureEntity measureEntityByMeasureEntityId;
}
