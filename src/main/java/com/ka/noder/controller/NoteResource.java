package com.ka.noder.controller;

import com.ka.noder.dao.NoteDao;
import com.ka.noder.dao.impl.NoteDaoImpl;
import com.ka.noder.model.Note;
import com.ka.noder.model.StatusResponse;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Path("notes")
public class NoteResource {
    private static final int STATUS_SUCCESS = 100;
    private static final int STATUS_ADDED = 101;
    private static final int STATUS_UPDATED = 102;
    private static final int STATUS_DELETED = 103;
    private static final int STATUS_FAILED_ADD = 201;
    private static final int STATUS_FAILED_UPDATE = 202;
    private static final int STATUS_FAILED_DELETE = 203;

    private NoteDao dao = new NoteDaoImpl();
    private Class<Note> noteClass = Note.class;

    @GET
    @Produces("application/json; charset=UTF-8")
    public Response getAll(){
        List<Note> noteList = dao.getAll("Note.getAll", noteClass);
        return Response.ok(noteList).build();
    }

    @GET
    @Path("/uuid")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUUIDList() {
        List<Note> noteList = dao.getAll("Note.getAll", noteClass);
        List<UUID> uuidList = new ArrayList<>();
        for (Note note : noteList) {
            uuidList.add(note.getUuid());
        }
        return Response.ok(uuidList).build();
    }

    @GET
    @Path("/{id}")
    @Produces("application/json; charset=UTF-8")
    public Response getNote(@PathParam("id") int id){
        Note note = dao.getById(id, noteClass);
        return Response.ok(note).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addNote(Note note){
        StatusResponse response = new StatusResponse();
        if (note == null) {
            System.err.println("Added, note null!");
            return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
        }
        System.err.println("Added, note title: " + note.getTitle());
        System.err.println("Added, uuid: " + note.getUuid());
        System.err.println("Added, date: " + note.getDate());
        if (note.getUuid() == null){
            response.setStatus(STATUS_FAILED_ADD);
            System.err.println("uuid: " + note.getUuid());
            return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
        }
        UriBuilder uriBuilder;
        try {
            System.err.println("date: " + note.getDate());

            note.setStatus(STATUS_SUCCESS);
            dao.save(note);
            response.setStatus(note.getStatus());
            uriBuilder =  UriBuilder.fromResource(NoteResource.class);
            uriBuilder.path(Integer.toString(note.getId()));

        } catch (Exception e) {
            System.err.println("Note save exc: " + e);
            response.setStatus(STATUS_FAILED_ADD);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
        }
        return Response.created(uriBuilder.build()).entity(response).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateNote(Note note){
        StatusResponse statusResponse = new StatusResponse();
        UUID uuid = note.getUuid();
        System.err.println("Updated, uuid: " + uuid);
        if (uuid == null) {
            statusResponse.setStatus(STATUS_FAILED_UPDATE);
            return Response.status(Response.Status.BAD_REQUEST).entity(statusResponse).build();
        }
        try {
            note.setStatus(STATUS_SUCCESS);
            int id = note.getId();
            System.err.println("id: " + id);
            dao.update(note);
            statusResponse.setStatus(note.getStatus());
        } catch (Exception e) {
            System.err.println("Note update exc: " + e);
            statusResponse.setStatus(STATUS_FAILED_UPDATE);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(statusResponse).build();
        }

        return Response.ok(statusResponse).build();
    }

    @DELETE
    @Path("/{uuid}")
    public Response deleteNote(@PathParam("uuid") UUID uuid){
        System.err.println("Deleted, uuid: " + uuid);
        if (uuid == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        try {
            dao.remove(uuid);
        } catch (Exception e) {
            System.err.println("Note delete exc: " + e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        return Response.ok().build();
    }

    @POST
    @Path("/del")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteNotes(List<String> uuidList) {
        System.err.println("uuidList " + uuidList);
        if (uuidList == null) {
            System.err.println("uuid list null!");
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        try {
            dao.removeNotes(uuidList);
        } catch (Exception e) {
            System.err.println("Delete notes exc: " + e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        return Response.ok().build();
    }
}