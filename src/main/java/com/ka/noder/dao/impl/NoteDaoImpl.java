package com.ka.noder.dao.impl;

import com.ka.noder.dao.NoteDao;
import com.ka.noder.model.Note;
import com.ka.noder.utils.HibernateUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NoteDaoImpl extends BasicDaoImpl<Note> implements NoteDao {

    public void update(Note model) {
        EntityManager em = null;
        try {
            em = HibernateUtil.getEmf().createEntityManager();
            EntityTransaction transaction = em.getTransaction();
            transaction.begin();

            System.err.println("Date from json in model: " + model.getDate());

            int rows = em.createQuery("UPDATE Note SET title =:title, text =:text, password =:password, date =:dateTime, status =:status WHERE uuid =:uuid")
                    .setParameter("title", model.getTitle())
                    .setParameter("text", model.getText())
                    .setParameter("password", model.getPassword())
                    .setParameter("status", 100)
                    .setParameter("dateTime", model.getDate())
                    .setParameter("uuid", model.getUuid())
                    .executeUpdate();

            System.err.println("rows updated: " + rows);
            transaction.commit();
        } catch (Exception e){
            System.err.println("NoteDao Update exc: " + e);
        } finally {
            if (em != null && em.isOpen()){
                em.close();
            }
        }
    }

    public void remove(UUID uuid) throws Exception{
        EntityManager em = null;
        try {
            em = HibernateUtil.getEmf().createEntityManager();

            TypedQuery<Note> q = em.createQuery("SELECT n FROM Note n WHERE n.uuid = ?1", Note.class);
            q.setParameter(1, uuid);
            Note note = q.getSingleResult();

            if (note != null){
                System.err.println("Note uuid: " + note.getUuid());
                System.err.println("Note title: " + note.getTitle());
                EntityTransaction transaction = em.getTransaction();
                transaction.begin();
                em.remove(note);
                transaction.commit();
            } else {
                System.err.println("Note null!!!");
            }
        } catch (Exception e){
            System.err.println("Note Remove exc: " + e);
            throw new Exception("Remove EXC: " + e);
        } finally {
            if (em != null && em.isOpen()){
                em.close();
            }
        }
    }

    public void removeNotes(List<String> uuidList) throws Exception{
        List<UUID> ids = new ArrayList<>();
        for (String id : uuidList) {
            ids.add(UUID.fromString(id));
            System.err.println("prepare remove uuid: " + id);
        }
        EntityManager em = null;
        try {
            em = HibernateUtil.getEmf().createEntityManager();

            TypedQuery<Note> query = em.createQuery("SELECT n FROM Note n WHERE n.uuid IN :ids", Note.class);
            query.setParameter("ids", ids);
            List<Note> notes = query.getResultList();
            EntityTransaction transaction = em.getTransaction();
            transaction.begin();
            for (Note note : notes) {
                System.err.println("remove note title: " + note.getTitle());
                em.remove(note);
            }
            transaction.commit();
        } catch (Exception e) {
            System.err.println("Remove notes exc: " + e);
            throw new Exception("Remove EXC: " + e);
        } finally {
            if (em != null && em.isOpen()){
                em.close();
            }
        }
    }
}