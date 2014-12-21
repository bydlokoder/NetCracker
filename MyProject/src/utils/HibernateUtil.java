package utils;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistryBuilder;

public class HibernateUtil {
    private static SessionFactory sessionFactory = null;
    
    static {
        try {
            Configuration configuration = new Configuration();
            configuration.configure();
            ServiceRegistryBuilder serviceRegistryBuilder = new ServiceRegistryBuilder().applySettings(configuration.getProperties());
            sessionFactory = configuration.buildSessionFactory(serviceRegistryBuilder.buildServiceRegistry());
        } catch (Exception e) {
              e.printStackTrace();
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}