package com.smcpartners.shape.shapeserver.usecases;

import com.smcpartners.shape.shapeserver.crosscutting.email.SendMailService;
import com.smcpartners.shape.shapeserver.crosscutting.logging.annotations.Logged;
import com.smcpartners.shape.shapeserver.crosscutting.security.rest.annotations.Secure;
import com.smcpartners.shape.shapeserver.frameworks.data.dao.shape.UserDAO;
import com.smcpartners.shape.shapeserver.gateway.rest.services.AuthCreateUserAccountService;
import com.smcpartners.shape.shapeserver.shared.constants.SecurityRoleEnum;
import com.smcpartners.shape.shapeserver.shared.dto.common.BooleanValueDTO;
import com.smcpartners.shape.shapeserver.shared.dto.common.MailDTO;
import com.smcpartners.shape.shapeserver.shared.dto.common.UserExtras;
import com.smcpartners.shape.shapeserver.shared.dto.shape.UserDTO;
import com.smcpartners.shape.shapeserver.shared.dto.shape.request.CreateUserRequestDTO;
import com.smcpartners.shape.shapeserver.shared.dto.shape.response.CreateUserResponseDTO;
import com.smcpartners.shape.shapeserver.shared.exceptions.SendEmailException;
import com.smcpartners.shape.shapeserver.shared.exceptions.UseCaseException;
import com.smcpartners.shape.shapeserver.shared.utils.RandomPasswordGenerator;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Responsible:<br/>
 * 1. To set the ADMIN role you must be an ADMIN.
 * To set the ORG_ADMIN role you must be and ADMIN or
 * An ORG_ADMIN and the new user account must have the
 * same org id and the user creating the account. The userid must be greater than 4 characters.
 * <p>
 * Created by johndestefano on 11/2/15.
 * <p>
 * Changes:<b/>
 */
@Path("/admin")
public class AuthCreateUserAccountServiceAdapter implements AuthCreateUserAccountService {

    @Inject
    private Logger log;

    @EJB
    private UserDAO userDAO;

    @EJB
    private SendMailService sms;

    @Inject
    private UserExtras userExtras;


    public AuthCreateUserAccountServiceAdapter() {
    }

    @Override
    @POST
    @Path("/create/user")
    @Produces("application/json")
    @Consumes("application/json")
    @Secure({SecurityRoleEnum.ADMIN, SecurityRoleEnum.ORG_ADMIN})
    @Logged
    public CreateUserResponseDTO createUserAccount(CreateUserRequestDTO dto) throws UseCaseException {
        try {
            // User id must be at least 4 characters
            if (dto.getId().length() < 4) {
                throw new Exception("User id must be at least 4 characters.");
            }

            // Check account id to make sure its not in use
            BooleanValueDTO checkRetDTO = userDAO.checkUserId(dto.getId());
            if (!checkRetDTO.isValue()) {
                // Load this object
                UserDTO nDTO = null;

                // Check the role being set
                // To set the ADMIN role you must be an ADMIN
                // To set the ORG_ADMIN role you must be and ADMIN or
                // An ORG_ADMIN and the new user account must have the
                // same org id and the user creating the account

                // Is the requesting user an ADMIN, if so they can add for any role and any organization
                if (userExtras.getRole() == SecurityRoleEnum.ADMIN) {
                    nDTO = this.createUserDTO(dto, true);
                } else {
                    // For Org admin check that the role is anything but ADMIN
                    // And the new user is in the same org as the org admin.
                    if (!dto.getRole().equalsIgnoreCase(SecurityRoleEnum.ADMIN.getName()) && userExtras.getOrgId() == dto.getOrganizationId()) {
                        nDTO = this.createUserDTO(dto, false);
                    } else {
                        throw new IllegalAccessException();
                    }
                }

                // First add the database
                UserDTO respDTO = userDAO.create(nDTO);

                // Next send a confirmation email with the random password and account set to change password.
                // This will result in the user having to change their password.
                MailDTO mail = new MailDTO();
                mail.setToEmail(nDTO.getEmail());
                //TODO: The text here should be externalized to the config proprties file
                mail.setSubject("Welcome to SHAPE Dashboard");
                mail.setMessage("Hello " + nDTO.getFirstName() + "," + "\n" + "\n" +
                        "You have been added as a registered user to the eHealthConnecticut SHAPE dashboard. " +
                        "To access the dashboard please use the following credentials:" + "\n" + "\n" +
                        "Dashboard Website Address: https://shape.ehealthconnecticut.org" + "\n" +
                        "Temporary Password: " + nDTO.getPassword() + "\n" + "\n" +
                        "You will be prompted to update your password upon login." + "\n" + "\n" +
                        "If you experience issues with logging in or have any questions please contact Julia Moore" +
                        " at jmoore@smcpartners.com." + "\n" + "\n" +
                        "Thank you, " + "\n" + "\n" +
                        "eHealthConnecticut");
                try {
                    sms.sendEmailMsg(mail);
                } catch (Exception e) {
                    throw new SendEmailException();
                }

                // Generate return
                return new CreateUserResponseDTO(respDTO.getId());
            } else {
                throw new NotAuthorizedException("User id is in use.");
            }
        } catch (Exception e) {
            log.logp(Level.SEVERE, this.getClass().getName(), "createUserAccount", e.getMessage(), e);

            if (e instanceof SendEmailException) {
                throw (SendEmailException) e;
            } else if (e instanceof NotAuthorizedException) {
                throw (NotAuthorizedException) e;
            } else {
                throw new UseCaseException(e.getMessage());
            }
        }
    }

    /**
     * Creates a UserDTO from the CreateUserRequestDTO. Applies the following rules:<br/>
     * 1. Only and ADMIN (creatorIsAdmin == true) can create an ADMIN<br/>
     * 2. The initial password will be randomly generated<br/>
     *
     * @param newUserDTO
     * @param creatorIsAdmin
     * @return
     * @throws Exception
     */
    private UserDTO createUserDTO(CreateUserRequestDTO newUserDTO, boolean creatorIsAdmin) throws Exception {
        UserDTO nDTO = new UserDTO();

        // Set the role.
        // Only an ADMIN can make another ADMIN
        if (creatorIsAdmin) {
            nDTO.setRole(newUserDTO.getRole());
            if (SecurityRoleEnum.valueOf(newUserDTO.getRole()) == SecurityRoleEnum.ADMIN) {
                nDTO.setAdmin(true);
            } else {
                nDTO.setAdmin(true);
            }
        } else {
            nDTO.setRole(newUserDTO.getRole());
            nDTO.setAdmin(true);
        }

        // Creating a new user sets the password to a random generated one
        String pWd = RandomPasswordGenerator.generateApplicationDefaultPwd();

        // Set the UserDTO
        // New account must reset password
        // New account is active by default
        // New account has fake password
        nDTO.setResetPwd(true);
        nDTO.setActive(true);
        nDTO.setCreateDt(new Date());
        nDTO.setEmail(newUserDTO.getEmail());
        nDTO.setFirstName(newUserDTO.getFirstName());
        nDTO.setLastName(newUserDTO.getLastName());
        nDTO.setOrganizationId(newUserDTO.getOrganizationId());
        nDTO.setId(newUserDTO.getId());
        nDTO.setPassword(pWd);

        return nDTO;
    }
}
