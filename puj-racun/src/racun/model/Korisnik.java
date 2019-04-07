package racun.model;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;




public class Korisnik implements Model {
    
     static final String DATABASE_URL = "jdbc:mysql://localhost:3306/fsre-puj?zeroDateTimeBehavior=convertToNull";
     static final String USERNAME = "root";
     static final String PASSWORD = "";

   
     private java.sql.Connection connection;
     private Statement statement;
    
    
    private int id;
    private String naziv;
    private String sifra;
    private String uloga;
  
    

    public Korisnik(int id, String naziv, String sifra,String uloga) {
        this.id = id;
        this.naziv = naziv;
        this.sifra = sifra;
        this.uloga=uloga;
 
    }

    public Korisnik() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getSifra() {
        return sifra;
    }

    public void setSifra(String sifra) {
        this.sifra = sifra;
    }
    
    public String getUloga() {
        return uloga;
    }
    public void setUloga(String uloga) {
        this.uloga = uloga;
    }
    
    
    @Override
    public void create() {
        try {
            PreparedStatement stmnt = Database.CONNECTION.prepareStatement(
                    "INSERT INTO korisnik VALUES (null, ?, ?,?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            stmnt.setString(1, this.naziv);
            stmnt.setString(2, this.sifra); 
            stmnt.setString(3, this.uloga); 
            stmnt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Greska prilikom stvaranja korisnika u bazi:"
                    + e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "Korisnik{" +
                "id=" + id +
                ", naziv='" + naziv + '\'' +
                ", sifra='" + sifra + '\'' +
                ", uloga='" + uloga + '\'' +
               
                '}';
    }

    @Override
    public void update() {
    }

    @Override
    public void delete() {
    }
}