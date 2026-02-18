package org.univ_paris8.iut.montreuil.dev_avance.api;

import org.univ_paris8.iut.montreuil.dev_avance.dto.Credentials;
import org.univ_paris8.iut.montreuil.dev_avance.entity.User;
import org.univ_paris8.iut.montreuil.dev_avance.security.TokenService;
import org.univ_paris8.iut.montreuil.dev_avance.service.UserService;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

@Path("/login")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LoginResource {

    private UserService userService = new UserService();

    @POST
    public Response login(@Valid Credentials credentials) {
        User user = userService.loginByUsername(credentials.getUsername(), credentials.getPassword());
        if (user == null) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"error\": \"Invalid credentials\"}")
                    .build();
        }

        String token = TokenService.generateToken(user.getId());

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("expiresIn", 3600);

        return Response.ok(response).build();
    }
}
