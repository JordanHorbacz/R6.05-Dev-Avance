package org.univ_paris8.iut.montreuil.dev_avance;

import org.univ_paris8.iut.montreuil.dev_avance.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        try {
            userService.createUser(username, email, password);
            resp.sendRedirect("login.jsp");
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Erreur lors de l'inscription (email ou username déjà pris ?)");
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
        }
    }
}