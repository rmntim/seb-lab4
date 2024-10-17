package ru.rmntim.web.tools;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceContext;
import ru.rmntim.web.models.Point;

import java.io.Serializable;
import java.util.ArrayList;

public class DBCommunicator implements Serializable {

    private static volatile DBCommunicator instance;

    public static DBCommunicator getInstance() {
        DBCommunicator localInstance = instance;
        if (localInstance == null) {
            synchronized (DBCommunicator.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new DBCommunicator();
                }
            }
        }
        return localInstance;
    }


    @PersistenceContext
    private EntityManager manager;

    public DBCommunicator() {
        var managerFactory = Persistence.createEntityManagerFactory("default");
        manager = managerFactory.createEntityManager();
    }

    public void sendOne(Point point) {
        var transaction = manager.getTransaction();
        try {
            transaction.begin();
            manager.persist(point);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) transaction.rollback();
        }
    }

    public ArrayList<Point> getAll() {
        var transaction = manager.getTransaction();
        try {
            transaction.begin();
            var res = new ArrayList<>(manager.createQuery("select p from Point p", Point.class).getResultList());
            transaction.commit();
            return res;
        } catch (Exception e) {
            if (transaction.isActive()) transaction.rollback();
            return new ArrayList<>();
        }
    }

    public void clearAll() {
        var transaction = manager.getTransaction();
        transaction.begin();
        manager.createQuery("delete from Point p").executeUpdate();
        transaction.commit();
    }
}
