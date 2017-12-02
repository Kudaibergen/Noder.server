package com.ka.noder.dao.impl;

import com.ka.noder.dao.BasicDao;
import com.ka.noder.model.Model;
import com.ka.noder.utils.HibernateUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.List;

public class BasicDaoImpl<T extends Model> implements BasicDao<T> {

    public List<T> getAll(String namedQuery, Class<T> tClass) {
        EntityManager em = null;
        List<T> resultList = null;
        try {
            em = HibernateUtil.getEmf().createEntityManager();
            TypedQuery<T> typedQuery = em.createNamedQuery(namedQuery, tClass);
            resultList = typedQuery.getResultList();
        } catch (Exception e){
            System.err.println("Note getAll exc: " + e);
        } finally {
            if (em != null && em.isOpen()){
                em.close();
            }
        }
        return resultList;
    }

    public T getById(int id, Class<T> tClass) {
        EntityManager em = null;
        T model = null;
        try {
            em = HibernateUtil.getEmf().createEntityManager();
            model = em.find(tClass, id);
        } catch (Exception e){
            System.err.println("Note GetById exc: " + e);
        } finally {
            if (em != null && em.isOpen()){
                em.close();
            }
        }
        return model;
    }

    public void save(T model) throws Exception {
        EntityManager em = null;
        try {
            em = HibernateUtil.getEmf().createEntityManager();
            EntityTransaction transaction = em.getTransaction();
            transaction.begin();
            em.persist(model);
            transaction.commit();
        } finally {
            if (em != null && em.isOpen()){
                em.close();
            }
        }
    }

    public void update(int id, T model) {
        EntityManager em = null;
        try {
            em = HibernateUtil.getEmf().createEntityManager();
            EntityTransaction transaction = em.getTransaction();
            transaction.begin();
            em.merge(model);
            transaction.commit();
        } catch (Exception e){
            System.err.println("Note Update exc: " + e);
        } finally {
            if (em != null && em.isOpen()){
                em.close();
            }
        }
    }

    public void remove(int id, Class<T> tClass) {
        EntityManager em = null;
        try {
            em = HibernateUtil.getEmf().createEntityManager();
            T model = em.find(tClass, id);
            if (model != null){
                EntityTransaction transaction = em.getTransaction();
                transaction.begin();
                em.remove(model);
                transaction.commit();
            }
        } catch (Exception e){
            System.err.println("Note Remove exc: " + e);
        } finally {
            if (em != null && em.isOpen()){
                em.close();
            }
        }
    }
}