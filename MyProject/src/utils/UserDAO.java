package utils;

import org.hibernate.Query;
import org.hibernate.Session;

import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private String authQuery = "FROM User  WHERE email = :emailParam AND hash = :hashParam";
    private String findByEmailQuery = "FROM User  WHERE email = :emailParam";

    public void addUser(User stud) throws SQLException {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();

            session.beginTransaction();
            session.save(stud);
            session.getTransaction().commit();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка I/O", JOptionPane.OK_OPTION);

        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public void updateUser(User stud) throws SQLException {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.update(stud);
            session.getTransaction().commit();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка I/O", JOptionPane.OK_OPTION);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public User getUser(long ID) {
        Session session = null;
        User user = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            user = (User) session.get(User.class, ID);
            session.getTransaction().commit();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка I/O", JOptionPane.OK_OPTION);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return user;
    }

    private User getUserByEmail(String email) {
        Session session = null;
        User user = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query query = session.createQuery(findByEmailQuery);
            query.setParameter("emailParam", email);
            List<User> users = (List<User>) query.list();
            if (users.size() > 0) {
                user = users.get(0);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка I/O", JOptionPane.OK_OPTION);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return user;
    }

    public boolean isUserExists(String email) {
        User user = getUserByEmail(email);
        return user == null ? false : true;
    }

    public User authUser(String email, int hash) {
        Session session = null;
        User user = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query query = session.createQuery(authQuery);
            query.setParameter("emailParam", email);
            query.setParameter("hashParam", hash);
            List<User> users = (List<User>) query.list();
            if (users.size() > 0) {
                user = users.get(0);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка I/O", JOptionPane.OK_OPTION);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return user;
    }

    public List<User> getAllUsers() throws SQLException {
        Session session = null;
        List<User> studs = new ArrayList<User>();
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            studs = session.createCriteria(User.class).list();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка I/O", JOptionPane.OK_OPTION);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return studs;
    }

    public void deleteUser(User stud) throws SQLException {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.delete(stud);
            session.getTransaction().commit();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка I/O", JOptionPane.OK_OPTION);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }
}