package com.suplementos.erp.repository;

import com.suplementos.erp.model.Produto;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import java.util.List;

public class ProdutoRepository implements IRepository<Produto> {

    private final SessionFactory sessionFactory;

    public ProdutoRepository() {
        sessionFactory = HibernateUtil.getSessionFactory();
    }

    // Metodo para salvar um produto no banco de dados
    public void salvar(Produto produto) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.saveOrUpdate(produto);
            session.getTransaction().commit();
        }
    }


    @Override
    public void salvar(int id, Produto produto) {
        salvar(produto);
    }

    @Override
    public Produto buscarPorId(int id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Produto.class, id);
        }
    }

    public int getNextId() {
        try (Session session = sessionFactory.openSession()) {
            List<Produto> produtos = session.createQuery("FROM Produto ORDER BY id DESC", Produto.class)
                    .setMaxResults(1)
                    .list();
            if (produtos.isEmpty()) {
                return 1;
            }
            return produtos.get(0).getId() + 1;
        }
    }

    @Override
    public void remover(int id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Produto produto = session.get(Produto.class, id);
            if (produto != null) {
                session.delete(produto);
            }
            session.getTransaction().commit();
        }
    }

    @Override
    public List<Produto> buscarTodos() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Produto WHERE ativo = true", Produto.class).list();
        }
    }

    public Produto buscarPorNome(String nome) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Produto WHERE nome = :nome", Produto.class)
                    .setParameter("nome", nome)
                    .uniqueResult();
        }
    }
    public List<Produto> buscarPorFornecedor(int idFornecedor) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Produto WHERE fornecedor.id = :idFornecedor", Produto.class)
                    .setParameter("idFornecedor", idFornecedor)
                    .list();
        }
    }
}