package com.smcpartners.shape.shapeserver.frameworks.data.entitymodel.shape;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * Responsibility: Request/Response logging</br>
 * 1. </br>
 * 2. </br>
 * Created By: johndestefano
 * Date: 4/26/17
 */
@Entity
@Data
@NoArgsConstructor
public class LogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, insertable = false, updatable = false)
    private int id;

    @Basic
    @Column(name = "requestPath", updatable = false, length = 1024)
    private String requestPath;

    @Basic
    @Column(name = "requestHeader", updatable = false, length = 1024)
    private String requestHeader;

    @Basic
    @Lob
    @Column(name = "requestEntity", updatable = false)
    private String requestEntity;

    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "requestDt", nullable = false, updatable = false)
    private Date requestDt;

    @Basic
    @Column(name = "responseHeader", updatable = false, length = 1024)
    private String responseHeader;

    @Basic
    @Lob
    @Column(name = "responseEntity", updatable = false)
    private String responseEntity;

    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "responseDt", nullable = false, updatable = false)
    private Date responseDt;

    @Column(name = "userId", updatable = false, length = 25)
    private String user;
}
