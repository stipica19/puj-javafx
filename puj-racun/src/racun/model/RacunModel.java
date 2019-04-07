package racun.model;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class RacunModel implements Model{
     static final String DATABASE_URL = "jdbc:mysql://localhost:3306/fsre-puj?zeroDateTimeBehavior=convertToNull";
     static final String USERNAME = "root";
     static final String PASSWORD = "";

    ObservableList<RacunModel> lista  = FXCollections.observableArrayList();
   
    public  SimpleIntegerProperty sifra;
    public  SimpleStringProperty ime;
    public  SimpleIntegerProperty cijena;
    public SimpleIntegerProperty kol;
    public  SimpleIntegerProperty korisnik;
   

    public RacunModel (Integer sifra,Integer id, String ime,Integer cijena,Integer kol) {
            this.sifra = new SimpleIntegerProperty (sifra);
            this.korisnik = new SimpleIntegerProperty (id);
            this.ime = new SimpleStringProperty(ime);
            this.cijena = new SimpleIntegerProperty(cijena);
            this.kol = new SimpleIntegerProperty(kol);
 }

    RacunModel() {
    }
        public Integer getKorisnik() {
            return korisnik.get();
        }
       public Integer getCijena() {
            return cijena.get();
        }
        public Integer getKol() {
            return kol.get();
        }

       public Integer getSifra () {
            return sifra.get();
        }
       public String getIme () {
            return ime.get();
        }   
   
        public void setCijena(Integer cijena) {
            this.cijena.set(cijena);
        }

        public void setKol(Integer kol) {
            this.kol.set(kol);
        }
   
public  static ObservableList<RacunModel> listaa () throws SQLException {
                 ObservableList<RacunModel> lista = FXCollections.observableArrayList();
         
                 Statement statement = null;
                 java.sql.Connection con = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);
                 statement = con.createStatement();
                 ResultSet rs = statement.executeQuery("SELECT * FROM racun");
        
        try {
            while (rs.next()) {
             
                lista.add(new RacunModel(
                        rs.getInt("racun_id"),
                        rs.getInt("id_korisnika"),
                        rs.getString("ime"),
                        rs.getInt("cijena"),
                        rs.getInt("kol")));
              
            }
            
            
        } catch (SQLException ex) {
            System.out.println("Nastala je greška prilikom iteriranja: " + ex.getMessage());
      }
       
        return lista;
    }

    //Azuriranje racuna tj. kolicine i cijene 
    @Override
    public void update() {
       
         try { 
           java.sql.Connection conn = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);
                 
           PreparedStatement upit = Database.CONNECTION.prepareStatement("UPDATE racun SET  cijena=?, kol=? WHERE racun_id=?",
                   Statement.RETURN_GENERATED_KEYS);
             
            upit.setInt(1,this.getCijena());
            upit.setInt(2,this.getKol());
            upit.setInt(3,this.getSifra());
           
            upit.executeUpdate();
            
            System.out.println("Uspjesno Azuriran artikal na racun");
            upit.close();
        
            
        } catch (SQLException ex) {
            System.out.println("Greška prilikom spasavanja korisnika u bazu: " + ex.getMessage());
        }
        
    }
    //Stvaranje racuna tj. artikla koji ima kolicinu = 1
    @Override
    public void create() {
         try {
             try (PreparedStatement stmnt = Database.CONNECTION.prepareStatement(
                     "INSERT INTO racun VALUES (null,?,?,?,?)",Statement.RETURN_GENERATED_KEYS)) {
                 int a= this.getKol()+1;
                 
                 
                 stmnt.setInt(1,this.getKorisnik());
                 stmnt.setString(2,this.getIme());
                 stmnt.setInt(3,this.getCijena());
                 stmnt.setInt(4,a);
                 stmnt.executeUpdate();
             }
             System.out.println("Uspjesno dodan artikal na racun :D");
         } catch (SQLException ex) {
             Logger.getLogger(RacunModel.class.getName()).log(Level.SEVERE, null, ex);
         }
    }
     @Override
public void delete () {
 try {
        java.sql.Connection conn = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);
                 
        PreparedStatement upit = Database.CONNECTION.prepareStatement("DELETE FROM racun WHERE racun_id=?",
               Statement.RETURN_GENERATED_KEYS);
        upit.setInt(1, this.getSifra());
        upit.executeUpdate();
        } catch (SQLException ex) {
        System.out.println("Greška prilikom brisanja artikla:" + ex.getMessage());
 }
 }
}

