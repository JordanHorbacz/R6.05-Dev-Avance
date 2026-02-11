package org.univ_paris8.iut.montreuil.dev_avance;

import org.univ_paris8.iut.montreuil.dev_avance.entity.Annonce;
import org.univ_paris8.iut.montreuil.dev_avance.entity.User;
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

@WebServlet("/AnnonceAdd")
public class AnnonceAdd extends HttpServlet {
    private final AnnoncesService service = new AnnoncesService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("categories", service.getAllCategories());
        request.getRequestDispatcher("/AnnonceAdd.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        Annonce a = new Annonce();
        a.setTitle(request.getParameter("title"));
        a.setDescription(request.getParameter("description"));
        a.setAdress(request.getParameter("adress"));
        a.setMail(request.getParameter("mail"));

        String catIdStr = request.getParameter("categoryId");
        Long catId = (catIdStr != null && !catIdStr.isEmpty()) ? Long.parseLong(catIdStr) : null;

        User user = (User) request.getSession().getAttribute("user");

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Annonce>> violations = validator.validate(a);

        if (!violations.isEmpty()) {
            request.setAttribute("errors", violations);
            request.setAttribute("annonce", a);
            request.setAttribute("categories", service.getAllCategories());
            request.getRequestDispatcher("/AnnonceAdd.jsp").forward(request, response);
        } else {
            service.createAnnonce(a, catId, user != null ? user.getId() : null);
            response.sendRedirect("AnnonceList");
        }
    }
}