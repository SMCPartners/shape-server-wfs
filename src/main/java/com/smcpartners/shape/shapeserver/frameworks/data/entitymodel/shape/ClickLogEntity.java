package com.smcpartners.shape.shapeserver.frameworks.data.entitymodel.shape;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * Responsible:</br>
 * 1. Entity</br>
 * <p>
 * Created by johndestefano on 3/15/16.
 * </p>
 * <p>
 * Changes:</br>
 * 1. </br>
 * </p>
 */
@NamedQueries({
        @NamedQuery(name = "ClickLog.findByUser",
                query = "SELECT l FROM ClickLogEntity l WHERE l.userByUserId = :uId")
})
@Entity
@Table(name = "click_log", schema = "", catalog = "shape")
@Data
@NoArgsConstructor
public class ClickLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "clickLogId", nullable = false, insertable = true, updatable = true)
    private int clickLogId;

    @Lob
    @Column(name = "event", nullable = false, insertable = true, updatable = true)
    private String event;

    @Basic
    @Column(name = "eventDt", nullable = false, insertable = true, updatable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date eventDt;

    @Basic
    @Column(name = "additionalInfo", nullable = true, insertable = true, updatable = true, length = 255)
    private String additionalInfo;

    @Lob
    @Column(name = "requestInfo", nullable = true, insertable = true, updatable = true)
    private String requestInfo;

    @Lob
    @Column(name = "responseInfo", nullable = true, insertable = true, updatable = true)
    private String responseInfo;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = true)
    private UserEntity userByUserId;
}