/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package esmeralda.proyectos.api;
import esmeralda.proyectos.beans.RestOperation;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
@Path("/luces")
public interface ILuces {

    @GET
    @Path("/prueba")
    public abstract String prueba();

    @POST
    @Path("/authServer")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public RestOperation authServer(@FormParam("username") String username, @FormParam("handshask") String handshask) throws Exception;

    @POST
    @Path("/authClient")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public RestOperation authClient(@FormParam("handshaskresponse") String handshaskresponse, @FormParam("token") String token) throws Exception;

    @POST
    @Path("/ligthOn")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public RestOperation ligthOn(@FormParam("token") String token) throws Exception;

    @POST
    @Path("/ligthOff")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public RestOperation ligthOff(@FormParam("token") String token) throws Exception;

    @POST
    @Path("/getStatus")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public RestOperation getStatus(@FormParam("token") String token) throws Exception;

}
