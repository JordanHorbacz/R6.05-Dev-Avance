package org.univ_paris8.iut.montreuil.dev_avance;

import org.univ_paris8.iut.montreuil.dev_avance.entity.Annonce;
import org.univ_paris8.iut.montreuil.dev_avance.service.AnnoncesService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.io.IOException;
import java.util.Set;

@WebServlet("/AnnonceUpdate")
public class AnnonceUpdate extends HttpServlet {
    private final AnnoncesService service = new AnnoncesService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            long id = Long.parseLong(request.getParameter("id"));
            Annonce a = service.getAnnonce(id);
            request.setAttribute("annonce", a);

            request.setAttribute("categories", service.getAllCategories());
            request.getRequestDispatcher("/AnnonceUpdate.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("AnnonceList");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        try {
            long id = Long.parseLong(request.getParameter("id"));
            Annonce a = service.getAnnonce(id);

            if (a == null) {
                response.sendRedirect("AnnonceList");
                return;
            }

            a.setTitle(request.getParameter("title"));
            a.setDescription(request.getParameter("description"));
            a.setAdress(request.getParameter("adress"));
            a.setMail(request.getParameter("mail"));

            Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
            Set<ConstraintViolation<Annonce>> violations = validator.validate(a);

            if (!violations.isEmpty()) {
                request.setAttribute("errors", violations);
                request.setAttribute("annonce", a);
                request.setAttribute("categories", service.getAllCategories());
                request.getRequestDispatcher("/AnnonceUpdate.jsp").forward(request, response);
                return;
            }

            service.updateAnnonce(a);
            response.sendRedirect("AnnonceList");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("AnnonceList");
        }
    }
}