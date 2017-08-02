package com.smcpartners.shape.shapeserver.frameworks.data.entitymodel.shape;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * Responsibility: Record the upload of a Organization Stratification file</br>
 * 1. </br>
 * 2. </br>
 * Created By: johndestefano
 * Date: 7/24/17
 */
@Entity
@Table(name = "organization_measure_file_upload", schema = "", catalog = "shape")
@Data
@NoArgsConstructor
public class OrganizationStratificationFileUploadEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orgStratUploadId", nullable = false, insertable = true, updatable = true)
    private int orgStratUploadId;

    @Basic
    @Column(name = "uploadDt", nullable = false, insertable = true, updatable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date uploadDt;

    @Basic
    @Column(name = "fileName", nullable = false, insertable = true, updatable = true)
    private String fileName;

    @Lob
    @Column(name = "uploadedB64File", nullable = true, insertable = true, updatable = true)
    private String uploadedB64File;

    @ManyToOne
    @JoinColumn(name = "org_id", referencedColumnName = "id", nullable = false)
    private OrganizationEntity organizationEntity;

    @ManyToOne
    @JoinColumn(name = "org_strat_id", referencedColumnName = "id", nullable = false)
    private OrganizationStratificationEntity organizationStratificationEntity;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private UserEntity userByUserId;

}
