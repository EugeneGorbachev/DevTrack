import dao.Factory;
import entity.Task;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.List;

@Path("/task")
public class TaskService {
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Task getById(long id) {
        Task task = null;
        try {
            task = Factory.getInstance().getTaskDAO().getById(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return task;
    }

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Task> getAll() {
        List<Task> taskList = null;
        try {
            taskList = Factory.getInstance().getTaskDAO().getAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return taskList;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(Task task) {
        try {
            Factory.getInstance().getTaskDAO().update(task);
            return Response.status(204).entity(task).build();
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(501).entity(e.getStackTrace()).build();
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add(Task ta) {
        try {
            Factory.getInstance().getTaskDAO().add(ta);
            return Response.status(201).entity(ta).build();
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(501).entity(e.getStackTrace()).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response delete(long id) {
        try {
            Task task = Factory.getInstance().getTaskDAO().getById(id);
            Factory.getInstance().getTaskDAO().delete(task);
            return Response.status(204).build();
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(501).entity(e.getStackTrace()).build();
        }
    }
}
