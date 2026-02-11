package org.univ_paris8.iut.montreuil.dev_avance;

import org.univ_paris8.iut.montreuil.dev_avance.entity.Annonce;
import org.univ_paris8.iut.montreuil.dev_avance.service.AnnoncesService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "AnnonceList", value = "/AnnonceList")
public class AnnonceList extends HttpServlet {
    private final AnnoncesService service = new AnnoncesService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pageParam = request.getParameter("page");
        int page = 1;
        try {
            if (pageParam != null) {
                page = Integer.parseInt(pageParam);
            }
        } catch (NumberFormatException e) {
            page = 1;
        }

        String keyword = request.getParameter("search");
        List<Annonce> list;
        if (keyword != null && !keyword.trim().isEmpty()) {
            list = service.searchAnnonces(keyword, page, 10);
            request.setAttribute("search", keyword);
        } else {
            list = service.getAnnonces(page, 10);
        }

        request.setAttribute("annonces", list);
        request.setAttribute("currentPage", page);
        request.getRequestDispatcher("/AnnonceList.jsp").forward(request, response);
    }
}