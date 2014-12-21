package utils;

/**
 * Created by Амфетамин on 03.11.14.
 */
public class Factory {
    private static UserDAO userDAO = null;


    private static FileDAO fileDAO = null;
    private static Factory instance = null;

    public static synchronized Factory getInstance() {
        if (instance == null) {
            instance = new Factory();
        }
        return instance;
    }

    public UserDAO getUserDAO() {
        if (userDAO == null) {
            userDAO = new UserDAO();
        }
        return userDAO;
    }

    public  FileDAO getFileDAO() {
        if (fileDAO == null) {
            fileDAO = new FileDAO();
        }
        return fileDAO;
    }

}
