package com.suplementos.erp.repository;

import com.suplementos.erp.model.Fornecedor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import java.util.List;

public class FornecedorRepository implements IRepository<Fornecedor> {

    private final SessionFactory sessionFactory;

    public FornecedorRepository() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    @Override
    public void salvar(int id, Fornecedor fornecedor) {
        salvar(fornecedor);
    }

    public void salvar(Fornecedor fornecedor) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.saveOrUpdate(fornecedor);
            session.getTransaction().commit();
        }
    }

    @Override
    public Fornecedor buscarPorId(int id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Fornecedor.class, id);
        }
    }

    public Fornecedor buscarPorNome(String nome) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Fornecedor WHERE lower(nome) = :nome", Fornecedor.class)
                    .setParameter("nome", nome.toLowerCase())
                    .uniqueResult();
        }
    }

    @Override
    public void remover(int id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Fornecedor fornecedor = session.get(Fornecedor.class, id);
            if (fornecedor != null) {
                session.delete(fornecedor);
            }
            session.getTransaction().commit();
        }
    }

    @Override
    public List<Fornecedor> buscarTodos() {
        try (Session session = sessionFactory.openSession()) {
            // AQUI ESTÁ A CORREÇÃO: Filtramos por fornecedores ativos
            return session.createQuery("FROM Fornecedor WHERE ativo = true", Fornecedor.class).list();
        }
    }
}