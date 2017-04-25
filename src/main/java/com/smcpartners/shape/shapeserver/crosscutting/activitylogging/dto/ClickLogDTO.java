package com.smcpartners.shape.shapeserver.crosscutting.activitylogging.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * Responsible:</br>
 * 1. Holds data for the ClickLog</br>
 * <p>
 * Created by johndestefano on 10/3/15.
 * </p>
 * <p>
 * Changes:</br>
 * 1. </br>
 * </p>
 */
public class ClickLogDTO implements Serializable {
    private String userId;
    private int clickLogId;

    /**
     * Limit to 255 characters! This is usually the long class name of the method being invoked.
     */
    private String event;

    private Date eventDt;

    private String additionalInfo;

    private String requestInfo;
    private String responseInfo;

    public ClickLogDTO() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getClickLogId() {
        return clickLogId;
    }

    public void setClickLogId(int clickLogId) {
        this.clickLogId = clickLogId;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public Date getEventDt() {
        return eventDt;
    }

    public void setEventDt(Date eventDt) {
        this.eventDt = eventDt;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public String getRequestInfo() {
        return requestInfo;
    }

    public void setRequestInfo(String requestInfo) {
        this.requestInfo = requestInfo;
    }

    public String getResponseInfo() {
        return responseInfo;
    }

    public void setResponseInfo(String responseInfo) {
        this.responseInfo = responseInfo;
    }
}
