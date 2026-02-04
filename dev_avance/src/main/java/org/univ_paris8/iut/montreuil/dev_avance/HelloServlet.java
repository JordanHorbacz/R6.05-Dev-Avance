package org.univ_paris8.iut.montreuil.dev_avance;

import java.io.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {
    private String message;

    public void init() {
        message = "Hello the World";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();


        String nomSaisi = request.getParameter("nom");

        out.println("<html><body>");


        if (nomSaisi != null && !nomSaisi.trim().isEmpty()) {
            out.println("<h1>" + message + " " + nomSaisi + " !</h1>");
            out.println("<hr>"); // Une ligne de séparation
        } else {
            out.println("<h1>" + message + "</h1>");
        }

        out.println("<h3>Veuillez saisir votre nom :</h3>");
        out.println("<form action='hello-servlet' method='GET'>");
        out.println("  <input type='text' name='nom' placeholder='Votre nom ici...' required>");
        out.println("  <input type='submit' value='Envoyer'>");
        out.println("</form>");


        out.println("</body></html>");
    }

    public void destroy() {
    }
}