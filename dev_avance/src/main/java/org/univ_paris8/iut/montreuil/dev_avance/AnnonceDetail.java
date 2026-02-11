package org.univ_paris8.iut.montreuil.dev_avance;

import org.univ_paris8.iut.montreuil.dev_avance.entity.Annonce;
import org.univ_paris8.iut.montreuil.dev_avance.service.AnnoncesService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/AnnonceDetail")
public class AnnonceDetail extends HttpServlet {
    private final AnnoncesService service = new AnnoncesService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            long id = Long.parseLong(request.getParameter("id"));
            Annonce a = service.getAnnonce(id);
            if (a == null) {
                response.sendRedirect("AnnonceList");
                return;
            }
            request.setAttribute("annonce", a);
            request.getRequestDispatcher("/AnnonceDetail.jsp").forward(request, response);
        } catch (Exception e) {
            response.sendRedirect("AnnonceList");
        }
    }
}
