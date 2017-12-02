package com.ka.noder.controller;

import com.ka.noder.dao.BasicDao;
import com.ka.noder.dao.impl.BasicDaoImpl;
import com.ka.noder.model.User;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("users")
public class UserResource {
    private BasicDao<User> dao = new BasicDaoImpl<>();
    private Class<User> userClass = User.class;

    @GET
    @Path("/hello")
    public Response sayHello(){
        return Response.ok("Hay hello!").build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam("id") int id){

        User user = dao.getById(id, userClass);
        return Response.ok(user).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addUser(User user){
        try {
            dao.save(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.ok("Created").build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateUser(@PathParam("id") int id, User user){
        dao.update(id, user);
        return Response.ok("put").build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteUser(@PathParam("id") int id){
        dao.remove(id, userClass);
        return Response.ok("deleted").build();
    }
}