package org.univ_paris8.iut.montreuil.dev_avance.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
public class HelloResource {

    @GET
    @Path("helloWorld")
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello World";
    }

    @GET
    @Path("params")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getParams(@QueryParam("query") String query) {
        return Response.ok("Received query param: " + query).build();
    }

    @GET
    @Path("params/{path}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPathParams(@PathParam("path") String path) {
        return Response.ok("Received path param: " + path).build();
    }
}
