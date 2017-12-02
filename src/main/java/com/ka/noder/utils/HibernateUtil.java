package com.ka.noder.utils;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class HibernateUtil {
    private static final EntityManagerFactory emf;

    static{
        try{
            emf = Persistence.createEntityManagerFactory("com.ka.noder");
        } catch (Exception e){
            System.err.println("Initial EntityManagerFactory failed: " + e);
            throw new ExceptionInInitializerError(e);
        }
    }

    public static EntityManagerFactory getEmf(){
        return emf;
    }
}
