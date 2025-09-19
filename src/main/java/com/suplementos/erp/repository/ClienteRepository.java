package com.suplementos.erp.repository;

import com.suplementos.erp.model.Cliente;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import java.util.List;

public class ClienteRepository {

    private final SessionFactory sessionFactory;

    public ClienteRepository() {
        // Supondo que você tenha uma classe HibernateUtil
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    public void salvar(Cliente cliente) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.saveOrUpdate(cliente);
            session.getTransaction().commit();
        }
    }

    public Cliente buscarPorId(int id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Cliente.class, id);
        }
    }

    public List<Cliente> buscarTodos() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Cliente", Cliente.class).list();
        }
    }

    public void remover(Cliente cliente) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(cliente);
            session.getTransaction().commit();
        }
    }
}