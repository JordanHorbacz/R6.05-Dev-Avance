package org.univ_paris8.iut.montreuil.dev_avance;

import org.univ_paris8.iut.montreuil.dev_avance.service.AnnoncesService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/AnnonceLifecycle")
public class AnnonceLifecycleServlet extends HttpServlet {
    private final AnnoncesService service = new AnnoncesService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Long id = Long.parseLong(request.getParameter("id"));
            String action = request.getParameter("action");

            if ("publish".equals(action)) {
                service.publishAnnonce(id);
            } else if ("archive".equals(action)) {
                service.archiveAnnonce(id);
            }

            response.sendRedirect("AnnonceDetail?id=" + id);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("AnnonceList");
        }
    }
}