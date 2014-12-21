package Sample;

import utils.Factory;
import utils.User;
import utils.Utils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Амфетамин on 26.10.14.
 */
@WebServlet("/auth.jsp")
public class Auth extends HttpServlet {

    public static String Auth_OK = "Authorization success";
    public static String Auth_BAD = "User with these data not found";


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String password = request.getParameter("password");
        String login = request.getParameter("email");
        String check = User.check(password, login);
        if (check == null) {
            // User user = new User(password, login, new Date());
            User user = Factory.getInstance().getUserDAO().authUser(login, password.hashCode());

            if (user != null) {
                request.setAttribute(Utils.Auth_Result, Auth_OK);
                Cookie loginCookie = new Cookie(Utils.Auth_User, Long.toString(user.getId()));
                //setting cookie to expiry in 30 mins
                loginCookie.setMaxAge(30 * 60);
                response.addCookie(loginCookie);
                response.sendRedirect("/upload.jsp");
                return;
            } else {
                request.setAttribute(Utils.Auth_Result, Auth_BAD);
            }
        } else {
            request.setAttribute(Utils.Auth_User, check);
        }
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
}
