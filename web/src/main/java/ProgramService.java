import dao.Factory;
import entity.Program;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.List;

@Path("/program")
public class ProgramService {
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Program getById(long id) {
        Program program = null;
        try {
            program = Factory.getInstance().getProgramDAO().getById(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return program;
    }

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Program> getAll() {
        List<Program> programList = null;
        try {
            programList = Factory.getInstance().getProgramDAO().getAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return programList;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(Program program) {
        try {
            Factory.getInstance().getProgramDAO().update(program);
            return Response.status(204).entity(program).build();
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(501).entity(e.getStackTrace()).build();
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add(Program program) {
        try {
            Factory.getInstance().getProgramDAO().add(program);
            return Response.status(201).entity(program).build();
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(501).entity(e.getStackTrace()).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response delete(long id) {
        try {
            Program program = Factory.getInstance().getProgramDAO().getById(id);
            Factory.getInstance().getProgramDAO().delete(program);
            return Response.status(204).build();
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(501).entity(e.getStackTrace()).build();
        }
    }
}
