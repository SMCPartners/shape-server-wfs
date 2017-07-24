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
@Table(name = "organization_measure_file_upload", schema = "", catalog = "shape")
@NamedQueries({
        @NamedQuery(name = "OrganizationMeasureFileUpload.findByOrgMeasureIdAndUploadDt",
                query = "SELECT OBJECT(om) FROM OrganizationMeasureFileUploadEntity om " +
                        "WHERE om.organizationMeasureEntity.id = :omId AND " +
                        "om.uploadDt = :fileUploadDt")
})
@Data
@NoArgsConstructor
public class OrganizationMeasureFileUploadEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fileUploadId", nullable = false, insertable = true, updatable = true)
    private int fileUploadId;

    @Basic
    @Column(name = "uploadDt", nullable = false, insertable = true, updatable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date uploadDt;

    @Basic
    @Column(name = "fileName", nullable = false, insertable = true, updatable = true)
    private String fileName;

//    @Basic
//    @Column(name = "user_id", columnDefinition = "VARCHAR(25)", nullable = false, insertable = true, updatable = true)
//    private String userId;

    @Lob
    @Column(name = "uploadedB64File", nullable = true, insertable = true, updatable = true)
    private String uploadedB64File;

    @ManyToOne
    @JoinColumn(name = "measure_id", referencedColumnName = "id", nullable = false)
    private MeasureEntity measureEntityByMeasureEntityId;

    @ManyToOne
    @JoinColumn(name = "org_id", referencedColumnName = "id", nullable = false)
    private OrganizationEntity organizationEntity;

    @ManyToOne
    @JoinColumn(name = "org_measure_id", referencedColumnName = "id", nullable = false)
    private OrganizationMeasureEntity organizationMeasureEntity;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private UserEntity userByUserId;
}
