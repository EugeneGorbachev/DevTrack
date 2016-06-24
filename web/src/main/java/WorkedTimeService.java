import dao.Factory;
import entity.WorkedTime;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.List;

@Path("/workedtime")
public class WorkedTimeService {
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public WorkedTime getById(long id) {
        WorkedTime workedTime = null;
        try {
            workedTime = Factory.getInstance().getWorkedTimeDAO().getById(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return workedTime;
    }

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public List<WorkedTime> getAll() {
        List<WorkedTime> workedTimeList = null;
        try {
            workedTimeList = Factory.getInstance().getWorkedTimeDAO().getAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return workedTimeList;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(WorkedTime workedTime) {
        try {
            Factory.getInstance().getWorkedTimeDAO().update(workedTime);
            return Response.status(204).entity(workedTime).build();
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(501).entity(e.getStackTrace()).build();
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add(WorkedTime workedTime) {
        try {
            Factory.getInstance().getWorkedTimeDAO().add(workedTime);
            return Response.status(201).entity(workedTime).build();
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(501).entity(e.getStackTrace()).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response delete(long id) {
        try {
            WorkedTime workedTime = Factory.getInstance().getWorkedTimeDAO().getById(id);
            Factory.getInstance().getWorkedTimeDAO().delete(workedTime);
            return Response.status(204).build();
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(501).entity(e.getStackTrace()).build();
        }
    }
}
