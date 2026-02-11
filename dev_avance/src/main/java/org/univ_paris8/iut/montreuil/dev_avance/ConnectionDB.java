package org.univ_paris8.iut.montreuil.dev_avance;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDB {
    private String url = "jdbc:postgresql://database-etudiants:5432/jhorbacz";
    private String user = "jhorbacz";
    private String passwd = "0";
    /**
     * Objet Connection
     */
    private static Connection connect;
    /**
     * Constructeur privé
     * @throws ClassNotFoundException
     */
    private ConnectionDB() throws ClassNotFoundException{
        try {
            Class.forName("org.postgresql.Driver");
            this.connect = DriverManager.getConnection(url, user, passwd);
            if (this.connect != null) {
                System.out.println("Connexion réussie à la base !");
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Pilote PostgreSQL introuvable (JAR manquant) !", e);
        } catch (SQLException e) {
            throw new RuntimeException("Impossible de se connecter à la base : " + e.getMessage(), e);
        }
    }
    /**
     * Methode qui va nous retourner notre instance
     * et la creer si elle n'existe pas...
     * @return
     * @throws ClassNotFoundException
     */
    public static Connection getInstance() throws ClassNotFoundException{
        if(connect == null){
            new ConnectionDB();
        }
        return connect;
    }
}