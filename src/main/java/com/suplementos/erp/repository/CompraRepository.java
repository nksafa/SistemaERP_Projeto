package com.suplementos.erp.repository;

import com.suplementos.erp.model.Compra;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import java.util.List;

public class CompraRepository implements IRepository<Compra> {

    private final SessionFactory sessionFactory;

    public CompraRepository() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    @Override
    public void salvar(int id, Compra compra) {
        salvar(compra);
    }

    public void salvar(Compra compra) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.saveOrUpdate(compra);
            session.getTransaction().commit();
        }
    }

    @Override
    public Compra buscarPorId(int id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Compra.class, id);
        }
    }

    @Override
    public void remover(int id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Compra compra = session.get(Compra.class, id);
            if (compra != null) {
                session.delete(compra);
            }
            session.getTransaction().commit();
        }
    }

    @Override
    public List<Compra> buscarTodos() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Compra", Compra.class).list();
        }
    }
}
