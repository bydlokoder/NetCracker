package Sample;

import utils.Factory;
import utils.User;
import utils.Utils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;

/**
 * Created by Амфетамин on 27.10.14.
 */
@WebServlet("/register")
public class Register extends HttpServlet {
    private static String USER_EXISTS = "User with this email already exists";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String password = request.getParameter("password");
        String login = request.getParameter("email");
        String check = User.check(password, login);
        if (check == null) {
            User newUser = new User(password, login, new Date());

            boolean result = Factory.getInstance().getUserDAO().isUserExists(login);

            if (result) {
                request.setAttribute(Utils.Register_Result, USER_EXISTS);
            } else {
                try {
                    Factory.getInstance().getUserDAO().addUser(newUser);
                    request.setAttribute(Utils.Register_Result, "SUCCESS");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        } else {
            request.setAttribute(Utils.Register_Result, "Incorrect data");
        }

       request.getRequestDispatcher("/register.jsp").forward(request, response);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
