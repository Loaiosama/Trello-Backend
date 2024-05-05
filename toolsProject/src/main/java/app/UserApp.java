package app;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ejbs.User;
import services.UserService;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Stateless
public class UserApp {

	@Inject 
	private UserService service;
	
	@POST
	@Path("/register")
	public Response registerUser(User user) {
		
		User newAcc = service.register(user.getEmail(), user.getName(), user.getPassword(), user.getRole());
		return Response.ok(newAcc).build();
		
	}
	
	@POST
    @Path("/login")
    public Response loginUser(User user) {
        User loggedUser = service.login(user.getEmail(), user.getPassword());
        return Response.ok(loggedUser).build();
    }

    @PUT
    @Path("/updateProfile/{id}")
    public Response updateUserProfile(@PathParam("id") Long id, User user) {
        User updatedUser = service.updateProfile(id, user.getEmail(), user.getName(), user.getPassword());
        return Response.ok(updatedUser).build();
    }
}
