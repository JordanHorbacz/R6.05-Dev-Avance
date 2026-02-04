package org.univ_paris8.iut.montreuil.dev_avance;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/AnnonceUpdate")
public class AnnonceUpdate extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        try {
            Annonce a = new AnnonceDAO().find(id);
            request.setAttribute("annonce", a);
            this.getServletContext().getRequestDispatcher("/AnnonceUpdate.jsp").forward(request, response);
        } catch (Exception e) { e.printStackTrace(); }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Annonce a = new Annonce();
            a.setId(Integer.parseInt(request.getParameter("id"))); // CachÃ© dans le form
            a.setTitle(request.getParameter("title"));
            a.setDescription(request.getParameter("description"));
            a.setAdress(request.getParameter("adress"));
            a.setMail(request.getParameter("mail"));

            new AnnonceDAO().update(a);
            response.sendRedirect("AnnonceList");
        } catch (Exception e) { e.printStackTrace(); }
    }
}