package com.smcpartners.shape.shapeserver.usecases.helpers;

import com.smcpartners.shape.shapeserver.shared.exceptions.MaxFileSizeExceededException;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.MultivaluedMap;

/**
 * Responsibility: Helpers used in file upload and download</br>
 * 1. </br>
 * 2. </br>
 * Created By: johndestefano
 * Date: 7/24/17
 */
@ApplicationScoped
public class UploadDownLoadHelpers {

    /**
     * Extract the file name from the Content-disposition of the request
     *
     * @param header
     * @return
     */
    public String getFileName(MultivaluedMap<String, String> header) {

        String[] contentDisposition = header.getFirst("Content-Disposition").split(";");

        for (String filename : contentDisposition) {
            if ((filename.trim().startsWith("filename"))) {

                String[] name = filename.split("=");

                String finalFileName = name[1].trim().replaceAll("\"", "");
                return finalFileName;
            }
        }
        return "unknown";
    }

    public void checkContentLength(long maxContentLen, long incomingContentLen) throws MaxFileSizeExceededException {
        // Check maximum content length
        if (incomingContentLen > maxContentLen) {
            throw new MaxFileSizeExceededException();
        }
    }
}
