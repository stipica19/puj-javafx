package racun.model;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ArtikliModel {
    static final String DATABASE_URL = "jdbc:mysql://localhost:3306/fsre-puj?zeroDateTimeBehavior=convertToNull";
     static final String USERNAME = "root";
     static final String PASSWORD = "";

   
     private java.sql.Connection connection;
     private Statement statement;
    
  SimpleIntegerProperty id = new SimpleIntegerProperty();
  SimpleStringProperty ime = new SimpleStringProperty();
  SimpleIntegerProperty cijena = new SimpleIntegerProperty();
  SimpleStringProperty url = new SimpleStringProperty();
 
   public ArtikliModel (Integer id, String ime, Integer cijena,String url) {
            this.id = new SimpleIntegerProperty (id);
            this.ime = new SimpleStringProperty(ime);
            this.cijena = new SimpleIntegerProperty(cijena);
            this.url = new SimpleStringProperty(url);
 }
    
   public Integer getId () {
        return id.get();
    }
    public String getIme () {
           return ime.get();
    }
    public String getUrl () {
           return url.get();
    }

    public void setId(Integer id) {
        this.id.set(id);
    }

    public void setIme(String ime) {
           this.ime.set(ime);
    }

    public void setUrl(String url) {
           this.url.set(url);
    }

    public Integer getCijena() {
        return cijena.get();
    }

    public void setCijena(Integer cijena) {
        this.cijena.set(cijena);
    }
 
   public static ObservableList<ArtikliModel> listaArtikala () throws SQLException {
         ObservableList<ArtikliModel> lista = FXCollections.observableArrayList();
                Statement statement = null;
                java.sql.Connection con = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);
                statement = con.createStatement();
            
                String upit ="SELECT * FROM artikli";
                ResultSet rs = statement.executeQuery(upit);
       
        try {
            while (rs.next()) {
                lista.add(new ArtikliModel(
                        rs.getInt("artikal_id"),
                        rs.getString("naziv"),
                        rs.getInt("cijena"),
                        rs.getString("slika")));
            }
        } catch (SQLException ex) {
            System.out.println("Nastala je greška prilikom iteriranja: " + ex.getMessage());
        }
        return lista;
    }
   
 public void uredi () {
    try {
           java.sql.Connection conn = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);
                 
           PreparedStatement upit = Database.CONNECTION.prepareStatement("UPDATE artikli SET naziv=?,cijena=? ,slika=? WHERE artikal_id=?",
                   Statement.RETURN_GENERATED_KEYS);
        
            upit.setString(1, this.getIme());
            upit.setInt(2, this.getCijena());
            upit.setString(3, this.getUrl());
            upit.setInt(4, this.getId());
           
            upit.executeUpdate();
            } catch (SQLException ex) {
            System.out.println("Greška prilikom azuriranja artikla u bazu:" + ex.getMessage());
 
     }
   }
   public void brisi () {
 try {
        java.sql.Connection conn = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);
                 
       PreparedStatement upit = Database.CONNECTION.prepareStatement("DELETE FROM artikli WHERE artikal_id=?",Statement.RETURN_GENERATED_KEYS);
        upit.setInt(1, this.getId());
        upit.executeUpdate();
        } catch (SQLException ex) {
        System.out.println("Greška prilikom brisanja artikla:" + ex.getMessage());
 }
 }

 
}
