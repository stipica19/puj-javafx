package racun.model;


import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import racun.controller.PocetnaController;

public class LogiraniKorisnik {
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/fsre-puj?zeroDateTimeBehavior=convertToNull";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
    private java.sql.Connection connection;
    private Statement statement;
   
    public static boolean logiraj (String kime, String lozinka) throws SQLException {
          Statement statement = null;
          java.sql.Connection con = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);
          statement = con.createStatement();
          ResultSet rss = statement.executeQuery("SELECT *FROM korisnik"); 
           
          while(rss.next()) {
                       Integer id = rss.getInt("id");
                       String ime =rss.getString("korisnicko_ime");
                       String uloga = rss.getString("uloga");
                      
                       if(ime.equalsIgnoreCase(kime)){
                           
                          Racun k = new Racun();
                          k.setBr(id);
                          
                          PocetnaController p = new PocetnaController();
                          p.setUloga(uloga);
                         
                          System.out.println("_________________________________");
                          
                       }
                      }
        PreparedStatement ps = con.prepareStatement("SELECT * FROM korisnik WHERE  korisnicko_ime =? AND "
                + "lozinka=?");
        try {
            
            ps.setString(1, kime);
            ps.setString(2, lozinka);
         
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException ex) {
            System.out.println("Nastala je greška: "+ex.getMessage());
            return false;
        }
    }
   
}
