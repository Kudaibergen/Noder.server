package com.ka.noder.utils.widget;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ka.noder.model.Note;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.UUID;

@Provider
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class GsonJsonProvider<T> implements MessageBodyReader<T>, MessageBodyWriter<T> {
    private static final String UTF_8 = "UTF-8";
    private Gson gson;

    private Gson getGson(){
        if (gson == null) {
            gson = new GsonBuilder()
                    .registerTypeAdapter(UUID.class, new UuidConverter())
                    .registerTypeAdapter(LocalDateTime.class, new DateTimeConverter())
                    .registerTypeAdapter(Note.class, new NoteConverter())
                    .setPrettyPrinting()
                    .serializeNulls()
                    .setLenient()
                    .create();

            System.err.println("Gson Initialization");
        }
        System.err.println("Gson work");
        return gson;
    }

    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return true;
    }

    @Override
    public T readFrom(Class<T> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException, WebApplicationException {
        InputStreamReader streamReader = new InputStreamReader(entityStream, UTF_8);
        try {
            System.out.println("metka 1");
            return getGson().fromJson(streamReader, genericType);
        } finally {
            streamReader.close();
        }
    }

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return true;
    }

    @Override
    public long getSize(T t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return -1;
    }

    @Override
    public void writeTo(T t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {
        OutputStreamWriter writer = new OutputStreamWriter(entityStream, UTF_8);
        System.out.println("metka 2");
        try {
            getGson().toJson(t, genericType, writer);
        } finally {
            writer.close();
        }
    }
}
