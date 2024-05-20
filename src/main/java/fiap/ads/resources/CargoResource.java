package fiap.ads.resources;

import fiap.ads.models.Cargo;
import fiap.ads.repositories.CargoRepository;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("cargo")
public class CargoResource {

    CargoRepository cargoRepo = new CargoRepository();


    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCargo(@PathParam("id") int id) {
        var cargo = cargoRepo.findById(id);
        if (cargo == null)
            return Response.status(404).entity("Cargo não encontrado").build();
        return Response.status(200).entity(cargo).build();
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createCargo(Cargo cargo) {
        if (cargo == null)
            return Response.status(400).entity("Cargo não pode ser vazio").build();
        cargoRepo.create(cargo);
        return Response.status(201).entity(cargo).build();
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllCargos() {
        List<Cargo> cargos = cargoRepo.readAll();
        return Response.status(200).entity(cargos).build();
    }


    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCargo(@PathParam("id") int id, Cargo cargo) {
        cargo.setId(id);
        cargoRepo.update(cargo);
        return Response.status(Response.Status.OK).entity(cargo).build();
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteCargo(@PathParam("id") int id) {
        cargoRepo.delete(id);
        return Response.status(204).build();
    }
}
