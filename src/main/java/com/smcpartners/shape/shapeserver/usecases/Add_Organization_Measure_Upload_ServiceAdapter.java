package com.smcpartners.shape.shapeserver.usecases;


import com.smcpartners.shape.shapeserver.crosscutting.logging.annotations.Logged;
import com.smcpartners.shape.shapeserver.crosscutting.security.rest.annotations.Secure;
import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.FileUploadProcessorDAO;
import com.smcpartners.shape.shapeserver.gateway.rest.services.Add_Organization_Measure_Upload_Service;
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
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.wildfly.swarm.spi.runtime.annotations.ConfigurationValue;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MultivaluedMap;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Date;
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
public class Add_Organization_Measure_Upload_ServiceAdapter implements Add_Organization_Measure_Upload_Service {

    @Inject
    private Logger log;

    @EJB
    private FileUploadProcessorDAO fileUploadProcessorDAO;

    @Inject
    private UserExtras userExtras;

    @Inject
    @ConfigurationValue("com.smc.server-core.fileUpload.measureFileUploadKey")
    private String formFileUploadKey;


    public Add_Organization_Measure_Upload_ServiceAdapter() {
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
                List<InputPart> inputParts = uploadForm.get(formFileUploadKey);

                // The current file name being processed
                String fileName = null;

                // Loop through the list and process
                ByteArrayInputStream in = null;
                XSSFWorkbook wb = null;
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
//                        POIFSFileSystem fs = new POIFSFileSystem(new ByteArrayInputStream(bytes));
//                        HSSFWorkbook wb = new HSSFWorkbook(fs);
//                        HSSFSheet sheet = wb.getSheetAt(0);

                        // Read sheet data into memory
                        in = new ByteArrayInputStream(bytes);
                        wb = new XSSFWorkbook(in);
                        Sheet sheet = wb.getSheetAt(0);
                        Row row1 = sheet.getRow(0);

                        // Get the measure name string
                        Cell measureIdCell = row1.getCell(1);
                        String measureName = measureIdCell.getRichStringCellValue().getString();

                        // Extract the data and load into a

                        // Create a measure entry using the users org id and save the
                        // entry in the file upload table
                        Date now = new Date();
                        OrganizationMeasureDTO organizationMeasureDTO = new OrganizationMeasureDTO();
                        FileUploadRequestDTO fileUploadRequestDTO = new FileUploadRequestDTO();

                        // File Upload Data
                        fileUploadRequestDTO.setUploadDt(now);
                        fileUploadRequestDTO.setUploadedB64File(base64FileContents);
                        fileUploadRequestDTO.setUserId(userExtras.getUserId());
                        fileUploadRequestDTO.setMeasureEntityName(measureName);

                        // Organization measure data
                        organizationMeasureDTO.setRpDate(now);
                        organizationMeasureDTO.setUserId(userExtras.getUserId());
                        organizationMeasureDTO.setOrganizationId(userExtras.getOrgId());
                        organizationMeasureDTO.setNumeratorValue(this.convertToInt(sheet, 1, 1));
                        organizationMeasureDTO.setDenominatorValue(this.convertToInt(sheet, 2, 1));
                        organizationMeasureDTO.setReportPeriodQuarter(this.convertToInt(sheet, 3, 1));
                        organizationMeasureDTO.setReportPeriodYear(this.convertToInt(sheet, 4, 1));
                        organizationMeasureDTO.setGenderMaleNum(this.convertToInt(sheet, 5, 1));
                        organizationMeasureDTO.setGenderMaleDen(this.convertToInt(sheet, 6, 1));
                        organizationMeasureDTO.setGenderFemaleNum(this.convertToInt(sheet, 7, 1));
                        organizationMeasureDTO.setGenderFemaleDen(this.convertToInt(sheet, 8, 1));
                        organizationMeasureDTO.setAge1844Num(this.convertToInt(sheet, 9, 1));
                        organizationMeasureDTO.setAge1844Den(this.convertToInt(sheet, 10, 1));
                        organizationMeasureDTO.setAge4564Num(this.convertToInt(sheet, 11, 1));
                        organizationMeasureDTO.setAge4564Den(this.convertToInt(sheet, 12, 1));
                        organizationMeasureDTO.setAgeOver65Num(this.convertToInt(sheet, 13, 1));
                        organizationMeasureDTO.setAgeOver65Den(this.convertToInt(sheet, 14, 1));
                        organizationMeasureDTO.setEthnicityHispanicLatinoNum(this.convertToInt(sheet, 15, 1));
                        organizationMeasureDTO.setEthnicityHispanicLatinoDen(this.convertToInt(sheet, 16, 1));
                        organizationMeasureDTO.setEthnicityNotHispanicLatinoNum(this.convertToInt(sheet, 17, 1));
                        organizationMeasureDTO.setEthnicityNotHispanicLatinoDen(this.convertToInt(sheet, 18, 1));
                        organizationMeasureDTO.setRaceAfricanAmericanNum(this.convertToInt(sheet, 19, 1));
                        organizationMeasureDTO.setRaceAfricanAmericanDen(this.convertToInt(sheet, 20, 1));
                        organizationMeasureDTO.setRaceAmericanIndianNum(this.convertToInt(sheet, 21, 1));
                        organizationMeasureDTO.setRaceAmericanIndianDen(this.convertToInt(sheet, 22, 1));
                        organizationMeasureDTO.setRaceAsianNum(this.convertToInt(sheet, 23, 1));
                        organizationMeasureDTO.setRaceAsianDen(this.convertToInt(sheet, 24, 1));
                        organizationMeasureDTO.setRaceNativeHawaiianNum(this.convertToInt(sheet, 25, 1));
                        organizationMeasureDTO.setRaceNativeHawaiianDen(this.convertToInt(sheet, 26, 1));
                        organizationMeasureDTO.setRaceWhiteNum(this.convertToInt(sheet, 27, 1));
                        organizationMeasureDTO.setRaceWhiteDen(this.convertToInt(sheet, 28, 1));
                        organizationMeasureDTO.setRaceOtherNum(this.convertToInt(sheet, 29, 1));
                        organizationMeasureDTO.setRaceOtherDen(this.convertToInt(sheet, 30, 1));

                        // Save the results of the above processing
                        BooleanValueDTO dto = fileUploadProcessorDAO.createAndLogFileMeasureUpload(organizationMeasureDTO,
                                fileUploadRequestDTO);

                        // Save entry in return map
                        retDTO.addToMap(fileName, dto.isValue()? "succeeded" : "false");

                    } catch (Exception e) {
                        log.logp(Level.SEVERE, this.getClass().getName(), "addMeasureUpload - file", e.getMessage(), e);

                        // On errors record the files name and failed
                        retDTO.addToMap(fileName, "failed");
                    } finally {
                        if (in != null) {
                            in.close();
                        }

                        if (wb != null) {
                            wb.close();
                        }
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

    /**
     * Convert the double value in a cell to an int
     *
     * @param row
     * @param cell
     * @return
     * @throws Exception - If the given cell can't be converted to an int
     */
    private int convertToInt(Sheet sheet, int row, int cell) throws Exception {
        double doubleValue = sheet.getRow(row).getCell(cell).getNumericCellValue();
        return (new Double(doubleValue)).intValue();
    }
}
