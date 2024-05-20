package fiap.ads.resources;

import fiap.ads.models.Cliente;
import fiap.ads.repositories.ClienteRepository;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("cliente")
public class ClienteResource {

    ClienteRepository clienteRepo = new ClienteRepository();


    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCliente(@PathParam("id") int id) {
        var cliente = clienteRepo.findById(id);
        if (cliente == null)
            return Response.status(404).entity("Cliente não encontrado").build();
        return Response.status(200).entity(cliente).build();
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createCliente(Cliente cliente) {
        if (cliente == null)
            return Response.status(400).entity("Cliente não pode ser vazio").build();
        clienteRepo.create(cliente);
        return Response.status(201).entity(cliente).build();
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllClientes() {
        List<Cliente> clientes = clienteRepo.readAll();
        return Response.status(200).entity(clientes).build();
    }


    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCliente(@PathParam("id") int id, Cliente cliente) {
        cliente.setId(id);
        clienteRepo.update(cliente);
        return Response.status(Response.Status.OK).entity(cliente).build();
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteCliente(@PathParam("id") int id) {
        clienteRepo.delete(id);
        return Response.status(204).build();
    }

}

