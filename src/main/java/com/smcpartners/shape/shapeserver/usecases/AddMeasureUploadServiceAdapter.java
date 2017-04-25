package com.smcpartners.shape.shapeserver.usecases;


import com.smcpartners.shape.shapeserver.crosscutting.logging.annotations.Logged;
import com.smcpartners.shape.shapeserver.crosscutting.security.rest.annotations.Secure;
import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.FileUploadDAO;
import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.MeasureDAO;
import com.smcpartners.shape.shapeserver.gateway.rest.services.AddMeasureUploadService;
import com.smcpartners.shape.shapeserver.shared.constants.SecurityRoleEnum;
import com.smcpartners.shape.shapeserver.shared.dto.common.BooleanValueDTO;
import com.smcpartners.shape.shapeserver.shared.dto.common.UserExtras;
import com.smcpartners.shape.shapeserver.shared.dto.shape.OrganizationMeasureDTO;
import com.smcpartners.shape.shapeserver.shared.dto.shape.request.FileUploadRequestDTO;
import com.smcpartners.shape.shapeserver.shared.dto.shape.response.FileUploadResponseDTO;
import com.smcpartners.shape.shapeserver.shared.exceptions.NotAuthorizedToPerformActionException;
import com.smcpartners.shape.shapeserver.shared.exceptions.UseCaseException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Responsible:<br/>
 * 1. Upload a file that contains a measure. All files will be in a consistent excel format.
 * 2. A record of the file processing, including the file contents must be saved.
 * 3. The data in the file will create an organizational measure
 * 4. Only user role ADMIN or OR_ADMIN can perform this task
 * 5. Only org admins can perform this functions
 * <p>
 * Created by johndestefano on 05/09/2016.
 * <p>
 * Changes:<b/>
 */
@Path("/admin")
public class AddMeasureUploadServiceAdapter implements AddMeasureUploadService {

    @Inject
    private Logger log;

    @EJB
    private MeasureDAO measureDAO;

    @EJB
    private FileUploadDAO fileUploadDAO;

    @Context
    private UserExtras userExtras;


    public AddMeasureUploadServiceAdapter() {
    }

    @Override
    @POST
    @Path("/measure/add/upload")
    @Produces("application/json")
    @Consumes("multipart/form-data")
    @Secure({SecurityRoleEnum.ORG_ADMIN})
    @Logged
    public FileUploadResponseDTO addMeasureUpload(MultipartFormDataInput input) throws UseCaseException {
        try {
            // Create the return object
            final FileUploadResponseDTO retDTO = new FileUploadResponseDTO();

            // Check permissions
            if (userExtras.getRole() == SecurityRoleEnum.ORG_ADMIN) {

                // Get a list of uploaded files to process from the request
                Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
                List<InputPart> inputParts = uploadForm.get("uploadedFile");

                // The current file name being processed
                String fileName = null;

                // Loop through the list and process
                for (InputPart inputPart : inputParts) {
                    try {
                        // Get file name and bytes
                        MultivaluedMap<String, String> header = inputPart.getHeaders();
                        fileName = getFileName(header);

                        //convert the uploaded file to input stream
                        InputStream inputStream = inputPart.getBody(InputStream.class,null);
                        byte [] bytes = IOUtils.toByteArray(inputStream);

                        // Save b64 version of later use
                        String base64FileContents = Base64.encodeBase64String(bytes);

                        // Convert file to excel
                        POIFSFileSystem fs = new POIFSFileSystem(new ByteArrayInputStream(bytes));
                        HSSFWorkbook wb = new HSSFWorkbook(fs);
                        HSSFSheet sheet = wb.getSheetAt(0);

                        // Determine the measure id


                        // Extract the data and load into a

                        // Create a measure entry using the users org id and save the
                        // entry in the file upload table
                        OrganizationMeasureDTO organizationMeasureDTO = new OrganizationMeasureDTO();
                        FileUploadRequestDTO fileUploadRequestDTO = new FileUploadRequestDTO();

                        // Save the results of the above processing
                        BooleanValueDTO dto = fileUploadDAO.createAndLogFileMeasureUpload(organizationMeasureDTO,
                                fileUploadRequestDTO);

                        // Save entry in return map
                        retDTO.addToMap(fileName, dto.isValue()? "succeeded" : "false");

                    } catch (Exception e) {
                        log.logp(Level.SEVERE, this.getClass().getName(), "addMeasureUpload - file", e.getMessage(), e);

                        // On errors record the files name and failed
                        retDTO.addToMap(fileName, "failed");
                    }
                }

            } else {
                throw new NotAuthorizedToPerformActionException();
            }

            return retDTO;
        } catch (Exception e) {
            log.logp(Level.SEVERE, this.getClass().getName(), "addMeasureUpload", e.getMessage(), e);
            if (e instanceof NotAuthorizedToPerformActionException) {
                throw (NotAuthorizedToPerformActionException)e;
            } else {
                throw new UseCaseException(e.getMessage());
            }
        }
    }

    /**
     * Extract the file name from the Content-dispoition of the request
     * @param header
     * @return
     */
    private String getFileName(MultivaluedMap<String, String> header) {

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
}
