package fiap.ads.resources;

import fiap.ads.models.Funcionario;
import fiap.ads.repositories.FuncionarioRepository;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("funcionario")
public class FuncionarioResource {

    FuncionarioRepository funcionarioRepo = new FuncionarioRepository();


    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFuncionario(@PathParam("id") int id) {
        var funcionario = funcionarioRepo.findById(id);
        if (funcionario == null)
            return Response.status(404).entity("Funcionario não encontrado").build();
        return Response.status(200).entity(funcionario).build();
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createFuncionario(Funcionario funcionario) {
        if (funcionario == null)
            return Response.status(400).entity("Funcionario não pode ser vazio").build();
        funcionarioRepo.create(funcionario);
        return Response.status(201).entity(funcionario).build();
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllFuncionarios() {
        List<Funcionario> funcionarios = funcionarioRepo.readAll();
        return Response.status(200).entity(funcionarios).build();
    }


    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateFuncionario(@PathParam("id") int id, Funcionario funcionario) {
        funcionario.setId(id);
        funcionarioRepo.update(funcionario);
        return Response.status(Response.Status.OK).entity(funcionario).build();
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteFuncionario(@PathParam("id") int id) {
        funcionarioRepo.delete(id);
        return Response.status(204).build();
    }
}
