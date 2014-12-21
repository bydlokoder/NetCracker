package utils;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Амфетамин on 24.10.14.
 */
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id = 0;
    @Column(name = "email", unique = true, nullable = false)
    private String email;
    @Column(name = "date")
    private Date date;
    @Column(name = "hash")
    private int hash;


    //private static String userRegex = " ^[a-zA-Z][a-zA-Z0-9._\\-]{5,}$";

    public User(String password, String login, Date date) {
        this.date = date;
        this.hash = password.hashCode();
        this.email = login;
    }

    public User() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getHash() {
        return hash;
    }

    public void setHash(int hash) {
        this.hash = hash;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public static String check(String password, String login) {
        if (password == null || password.equals("") || login == null || login.equals("")) {
            return "Enter data";
        }
        if (!login.contains("@")) {
            return "Invalid e-mail address";
        }
        return null;
    }


    @Override
    public String toString() {
        return "<tr>\n" + "<td>" + email + "</td>\n" + "<td>" + hash + "</td>\n" + "<td>" + date + "</td>" + "</tr>\n";
    }


}
