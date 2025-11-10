package com.suplementos.erp.repository;

import com.suplementos.erp.model.Categoria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import java.util.List;

public class CategoriaRepository implements IRepository<Categoria> {

    private final SessionFactory sessionFactory;

    public CategoriaRepository() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    public void salvar(Categoria categoria) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.saveOrUpdate(categoria);
            session.getTransaction().commit();
        }
    }

    @Override
    public void salvar(int id, Categoria categoria) {
        salvar(categoria);
    }

    @Override
    public Categoria buscarPorId(int id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Categoria.class, id);
        }
    }

    public Categoria buscarPorNome(String nome) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Categoria WHERE lower(nome) = :nome", Categoria.class)
                    .setParameter("nome", nome.toLowerCase())
                    .uniqueResult();
        }
    }

    @Override
    public void remover(int id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Categoria categoria = session.get(Categoria.class, id);
            if (categoria != null) {
                session.delete(categoria);
            }
            session.getTransaction().commit();
        }
    }

    @Override
    public List<Categoria> buscarTodos() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Categoria", Categoria.class).list();
        }
    }
}