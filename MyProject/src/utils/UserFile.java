package utils;

import javax.persistence.*;

/**
 * Created by Амфетамин on 05.11.14.
 */
@Entity
@Table(name = "files")
public class UserFile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name = "name")
    private String name;
    @Column(name = "excelPath")
    private String pathToExcel;
    @Column(name = "xmlPath")
    private String pathToXML;
    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "user_id")
    private User user;

    public User getUser() {
        return user;
    }


    public UserFile(String name, String pathToExcel, String pathToXML, User user) {
        this.name = name;
        this.pathToExcel = pathToExcel;
        this.pathToXML = pathToXML;
        this.user = user;
    }

    public UserFile() {

    }

    public void setUser(User user) {
        this.user = user;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPathToExcel() {
        return pathToExcel;
    }

    public void setPathToExcel(String pathToExcel) {
        this.pathToExcel = pathToExcel;
    }

    public String getPathToXML() {
        return pathToXML;
    }

    public void setPathToXML(String pathToXML) {
        this.pathToXML = pathToXML;
    }


    @Override
    public String toString() {
        return name;
    }
}
