package org.univ_paris8.iut.montreuil.dev_avance;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(name = "AnnonceAdd", value = "/AnnonceAdd")
public class AnnonceAdd extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.getServletContext().getRequestDispatcher("/AnnonceAdd.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String adress = request.getParameter("adress");
        String mail = request.getParameter("mail");


        try (Connection c = ConnectionDB.getInstance()) {

            String sql = "INSERT INTO annonce (title, description, adress, mail, date) VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP)";

            try (PreparedStatement pstmt = c.prepareStatement(sql)) {
                pstmt.setString(1, title);
                pstmt.setString(2, description);
                pstmt.setString(3, adress);
                pstmt.setString(4, mail);

                pstmt.executeUpdate();

                System.out.println("Annonce insérée avec succès dans la base !");
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("Erreur lors de l'insertion en base de données", e);
        }

        response.sendRedirect("AnnonceAdd?status=success");
    }
}