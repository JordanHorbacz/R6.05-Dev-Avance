package org.univ_paris8.iut.montreuil.dev_avance;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "AnnonceList", value = "/AnnonceList")
public class AnnonceList extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            AnnonceDAO dao = new AnnonceDAO();
            List<Annonce> listeAnnonces = dao.findAll();
            request.setAttribute("annonces", listeAnnonces);
            this.getServletContext().getRequestDispatcher("/AnnonceList.jsp").forward(request, response);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}