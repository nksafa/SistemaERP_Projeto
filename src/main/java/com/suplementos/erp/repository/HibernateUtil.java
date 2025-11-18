package com.suplementos.erp.repository; // Ajuste o pacote se necessário

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import java.util.Properties;

public class HibernateUtil {
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            Configuration configuration = new Configuration();

            // 1. Tenta pegar as variáveis de ambiente (Railway)
            String dbUrl = System.getenv("MYSQL_URL");
            String dbUser = System.getenv("MYSQLUSER");
            String dbPassword = System.getenv("MYSQLPASSWORD");
            String dbPort = System.getenv("MYSQLPORT");
            String dbHost = System.getenv("MYSQLHOST");
            String dbName = System.getenv("MYSQLDATABASE");

            // 2. Configura as propriedades
            Properties settings = new Properties();

            if (dbUrl != null) {
                // --- CONFIGURAÇÃO PARA O RAILWAY (PRODUÇÃO) ---
                // O Railway fornece uma URL completa ou partes. Vamos montar a URL JDBC.
                // JDBC URL format: jdbc:mysql://HOST:PORT/DATABASE
                String jdbcUrl = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;

                settings.put("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver");
                settings.put("hibernate.connection.url", jdbcUrl);
                settings.put("hibernate.connection.username", dbUser);
                settings.put("hibernate.connection.password", dbPassword);
                settings.put("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
                settings.put("hibernate.hbm2ddl.auto", "update"); // Cria as tabelas automaticamente
                settings.put("hibernate.show_sql", "false");
            } else {
                // --- CONFIGURAÇÃO LOCAL (SEU COMPUTADOR) ---
                // Se não achar variáveis de ambiente, carrega do hibernate.cfg.xml normal
                return new Configuration().configure().buildSessionFactory();
            }

            configuration.setProperties(settings);

            // Adicione suas classes anotadas aqui!
            configuration.addAnnotatedClass(com.suplementos.erp.model.Produto.class);
            configuration.addAnnotatedClass(com.suplementos.erp.model.Cliente.class);
            configuration.addAnnotatedClass(com.suplementos.erp.model.Fornecedor.class);
            configuration.addAnnotatedClass(com.suplementos.erp.model.Usuario.class);
            configuration.addAnnotatedClass(com.suplementos.erp.model.Venda.class);
            configuration.addAnnotatedClass(com.suplementos.erp.model.Compra.class);
            configuration.addAnnotatedClass(com.suplementos.erp.model.Categoria.class);
            configuration.addAnnotatedClass(com.suplementos.erp.model.AuditLog.class);

            return configuration.buildSessionFactory();

        } catch (Throwable ex) {
            System.err.println("Falha na criação da SessionFactory." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}