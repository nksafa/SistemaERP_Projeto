package com.suplementos.erp.repository;

import com.suplementos.erp.model.AuditLog;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import java.util.List;

public class AuditLogRepository {
    private final SessionFactory sessionFactory;

    public AuditLogRepository() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    public void salvar(AuditLog log) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(log);
            session.getTransaction().commit();
        }
    }

    // Busca os logs ordenados do mais recente para o mais antigo
    public List<AuditLog> buscarTodos() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM AuditLog ORDER BY dataHora DESC", AuditLog.class).list();
        }
    }
}
