package org.univ_paris8.iut.montreuil.dev_avance;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AnnonceDAO extends DAO<Annonce> {
    private Connection connect;

    public AnnonceDAO() throws ClassNotFoundException {
        this.connect = ConnectionDB.getInstance();
    }

    @Override
    public boolean create(Annonce obj) {
        try {
            String sql = "INSERT INTO annonce (title, description, adress, mail, date) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = connect.prepareStatement(sql);
            ps.setString(1, obj.getTitle());
            ps.setString(2, obj.getDescription());
            ps.setString(3, obj.getAdress());
            ps.setString(4, obj.getMail());
            ps.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
            ps.executeUpdate();
            return true;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    @Override
    public List<Annonce> findAll() {
        List<Annonce> list = new ArrayList<>();
        try {
            ResultSet rs = connect.createStatement().executeQuery("SELECT * FROM annonce");
            while (rs.next()) {
                Annonce a = new Annonce();
                a.setId(rs.getInt("id"));
                a.setTitle(rs.getString("title"));
                a.setDescription(rs.getString("description"));
                a.setAdress(rs.getString("adress"));
                a.setMail(rs.getString("mail"));
                a.setDate(rs.getTimestamp("date"));
                list.add(a);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    @Override
    public Annonce find(int id) {
        Annonce a = null;
        try {
            String sql = "SELECT * FROM annonce WHERE id = ?";
            PreparedStatement ps = connect.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                a = new Annonce();
                a.setId(rs.getInt("id"));
                a.setTitle(rs.getString("title"));
                a.setDescription(rs.getString("description"));
                a.setAdress(rs.getString("adress"));
                a.setMail(rs.getString("mail"));
                a.setDate(rs.getTimestamp("date"));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return a;
    }

    @Override
    public boolean update(Annonce obj) {
        try {
            String sql = "UPDATE annonce SET title=?, description=?, adress=?, mail=? WHERE id=?";
            PreparedStatement ps = connect.prepareStatement(sql);
            ps.setString(1, obj.getTitle());
            ps.setString(2, obj.getDescription());
            ps.setString(3, obj.getAdress());
            ps.setString(4, obj.getMail());
            ps.setInt(5, obj.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    @Override
    public boolean delete(int id) {
        try {
            String sql = "DELETE FROM annonce WHERE id = ?";
            PreparedStatement ps = connect.prepareStatement(sql);
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
}