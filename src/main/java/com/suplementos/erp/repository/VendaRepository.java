package com.suplementos.erp.repository;

import com.suplementos.erp.model.Venda;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import java.util.List;

public class VendaRepository implements IRepository<Venda> {

    private final SessionFactory sessionFactory;

    public VendaRepository() {
        sessionFactory = HibernateUtil.getSessionFactory();
    }

    // Metodo para salvar e atualizar uma venda no banco de dados
    public void salvar(Venda venda) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.saveOrUpdate(venda); // O Hibernate cuida de salvar ou atualizar
            session.getTransaction().commit();
        }
    }

    // Este metodo é obrigatório por causa da interface IRepository.
    // Ele simplesmente chama o metodo salvar correto, que usa o Hibernate.
    @Override
    public void salvar(int id, Venda venda) {
        salvar(venda);
    }

    @Override
    public Venda buscarPorId(int id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Venda.class, id);
        }
    }

    public List<Venda> buscarPorClienteId(int clienteId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Venda v WHERE v.cliente.id = :clienteId", Venda.class)
                    .setParameter("clienteId", clienteId)
                    .list();
        }
    }

    @Override
    public void remover(int id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Venda venda = session.get(Venda.class, id);
            if (venda != null) {
                session.delete(venda);
            }
            session.getTransaction().commit();
        }
    }

    @Override
    public List<Venda> buscarTodos() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Venda", Venda.class).list();
        }
    }
}