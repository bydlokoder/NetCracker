package utils;

import org.hibernate.Query;
import org.hibernate.Session;

import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Амфетамин on 05.11.14.
 */
public class FileDAO {
    private final static String allFilesQuery = "from UserFile where user.id=:userID";
    private final static String XML_QUERY = "select pathToXML from UserFile where id=:id";

    public long addFile(UserFile userFile) throws SQLException {
        Session session = null;
        long id = -1;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            id = (Long) session.save(userFile);
            session.getTransaction().commit();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка I/O", JOptionPane.OK_OPTION);

        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return id;
    }

    public List<UserFile> getAllFiles(long userID) {
        Session session = null;
        List<UserFile> userFiles = new ArrayList<UserFile>();
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query query = session.createQuery(allFilesQuery);
            query.setParameter("userID", userID);
            userFiles = query.list();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка I/O", JOptionPane.OK_OPTION);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return userFiles;
    }

    public String getXML(long fileID) {
        Session session = null;
        String xmlPath = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query query = session.createQuery(XML_QUERY);
            query.setParameter("id", fileID);
            List<String> results = query.list();
            if (results.size() == 1) {
                xmlPath = results.get(0);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка I/O", JOptionPane.OK_OPTION);

        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return xmlPath;
    }
}
