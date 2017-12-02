package com.ka.noder.dao;

import com.ka.noder.model.Note;

import java.util.List;
import java.util.UUID;

public interface NoteDao extends BasicDao<Note> {

    void update(Note model);

    void remove(UUID uuid) throws Exception;

    void removeNotes(List<String> ids) throws Exception;
}
