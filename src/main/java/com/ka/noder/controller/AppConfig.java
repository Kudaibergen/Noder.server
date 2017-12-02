package com.ka.noder.controller;

import com.ka.noder.utils.widget.GsonJsonProvider;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/api")
public class AppConfig extends Application {
    private final Set<Class<?>> classes;

    public AppConfig() {
        Set<Class<?>> h = new HashSet<>();
        h.add(UserResource.class);
        h.add(NoteResource.class);
        h.add(GsonJsonProvider.class);
        classes = Collections.unmodifiableSet(h);
    }

    @Override
    public Set<Class<?>> getClasses() {
        return classes;
    }
}