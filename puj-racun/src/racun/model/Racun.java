package racun.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.logging.Level;
import java.util.logging.Logger;


import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;


import javafx.scene.Cursor;

import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.stage.FileChooser;
import racun.controller.KorisnikController;




public class Racun {
  
    @FXML TableView racunTablica;
    @FXML TableColumn nazivT;
    @FXML TableColumn kolT;
    @FXML TableColumn cijenaT;
    @FXML private TextField ukupnaCijena;

    @FXML private TilePane libraryTilePane;
    @FXML private TextField kolicina;

    @FXML private Button spremiBtn;
    
    @FXML private Button minuKol;

    @FXML private Button plusKol;
    @FXML private Button izbrisiBtn;
    
     static final String DATABASE_URL = "jdbc:mysql://localhost:3306/fsre-puj?zeroDateTimeBehavior=convertToNull";
     static final String USERNAME = "root";
     static final String PASSWORD = "";
     private Connection connection;
     private Statement statement;
     public static int br;
     public int cena;
     RacunModel odaberiArtikl;
     public int bb;
     int c=0;
     int cijena=0;
     int id=0;
    
    public int getBr() {
        return br;
    }

   
    public void setBr(int br) { 
        this.br = br;
    }

     //Brisanje iz tablice racun sve artikle
    @FXML
    void printajRacun(ActionEvent event) throws SQLException {
                System.out.println("Ukupno za platit: "+cijena);
                java.sql.Connection con = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);
                 
                Statement stmt = con.createStatement();
                String sql = "DELETE FROM racun";
                int deleteCount = stmt.executeUpdate(sql);
                sql = "DELETE FROM racun WHERE";
                PreparedStatement pstmt = con.prepareStatement(sql);
                racunTablica.getItems().clear();
                ukupnaCijena.clear();
                cijena=0;
                
                
    }
    @FXML
    public void selectArtikl (Event e) {
            this.odaberiArtikl = (RacunModel)
            this.racunTablica.getSelectionModel().getSelectedItem();
            this.kolicina.setText(String.valueOf(this.odaberiArtikl.getKol()));
            cena = this.odaberiArtikl.getCijena();
            cena=cena/this.odaberiArtikl.getKol();
            System.out.println("Cena"+cena);
            
            
            
    }
    @FXML
    public void smanjenjeKolicine(ActionEvent event) throws SQLException {
             
        try {
            this.odaberiArtikl.setKol(Integer.parseInt(this.kolicina.getText())-1);
            c=cena*(Integer.parseInt(this.kolicina.getText())-1);
            this.odaberiArtikl.setCijena(c);
            
            if((Integer.parseInt(this.kolicina.getText())-1)==0){
                this.odaberiArtikl.delete();
            }
            this.odaberiArtikl.update();
            ObservableList<RacunModel> data = RacunModel.listaa();
            this.racunTablica.setItems(data);
            this.kolicina.setText(String.valueOf(this.odaberiArtikl.getKol()));
        } catch (SQLException ex) {
            Logger.getLogger(Racun.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
      

    @FXML
    void izbrisiArtikl(ActionEvent event) {
         if (this.odaberiArtikl != null) {
            try {
                this.odaberiArtikl.delete();
                 ObservableList<RacunModel> data = RacunModel.listaa();
                this.racunTablica.setItems(data);
            } catch (SQLException ex) {
                Logger.getLogger(KorisnikController.class.getName()).log(Level.SEVERE, null, ex);
            }
          }
        
    }

    
    
    @FXML
    void povecanjeKolicine(ActionEvent event) {
        try {
            
            this.odaberiArtikl.setKol(Integer.parseInt(this.kolicina.getText())+1);
            c=cena*(Integer.parseInt(this.kolicina.getText())+1);
            this.odaberiArtikl.setCijena(c);
            
           
            
            this.odaberiArtikl.update();
            ObservableList<RacunModel> data = RacunModel.listaa();
            this.racunTablica.setItems(data);
            this.kolicina.setText(String.valueOf(this.odaberiArtikl.getKol()));
        } catch (SQLException ex) {
            Logger.getLogger(Racun.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    private void refreshTableView() throws SQLException {
         ObservableList<RacunModel> data = RacunModel.listaa();
         this.racunTablica.setItems(data);
         racunTablica.getSelectionModel().selectFirst();
         nazivT.setCellValueFactory(new PropertyValueFactory<>("Ime"));
         cijenaT.setCellValueFactory(new PropertyValueFactory<>("Cijena"));
         kolT.setCellValueFactory(new PropertyValueFactory<>("Kol"));
         racunTablica.setItems(data);
       
    }
    
    

    public void initialize() throws SQLException {
        
        try {
            Statement statement = null;
            java.sql.Connection con = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);
            statement = con.createStatement();
             
            Racun k = new Racun();
            id=k.getBr();
            System.out.println("ID logiranog korisnika: "+id);
               
               
            String select ="SELECT * FROM artikli";
            ResultSet rs = statement.executeQuery(select);
                            
                while(rs.next()) {
                    String nazivArtikla = rs.getString("naziv");
                    String slikaArtikla = rs.getString("slika");
                    Integer cijenaArtikla = rs.getInt("cijena");
                   
                    
                    Image gameImg = new Image(slikaArtikla);
                    ImageView coverImageView = new ImageView(gameImg);
                    coverImageView.setFitHeight(120);
                    coverImageView.setFitWidth(100);
                    Hyperlink downloadGame = new Hyperlink(nazivArtikla);
                    downloadGame.setContentDisplay(ContentDisplay.TOP);
                    downloadGame.setGraphic(coverImageView);
                    downloadGame.getStyleClass().add("tiles");
                    downloadGame.setCursor(Cursor.DEFAULT);
                    libraryTilePane.getChildren().add(downloadGame);
                    RacunModel noviProizvod = new RacunModel(0,id,nazivArtikla,cijenaArtikla,0);

                    downloadGame.setOnAction((ActionEvent event) -> {
                        try {
                            boolean poz = false;
                            ObservableList<RacunModel> data = RacunModel.listaa();
                            cijena = cijena+cijenaArtikla;
                            ukupnaCijena.setText(Integer.toString(cijena));
                            for(RacunModel p : data){
                                if(p.getIme().equalsIgnoreCase(noviProizvod.getIme())){
                                  
                                    p.setKol(p.getKol()+1); //Povecanje kolicine artikla
                                    p.setCijena(noviProizvod.getCijena()*p.getKol());  //Povecanje cijena artikla 
                                    
                                    ukupnaCijena.setText(Integer.toString(cijena));
                                    p.update();
                                    refreshTableView();
                                    poz = true;
                                    break;
                                }
                            }
                       
                            if(poz==false){ //Ispitivanje dali je pozvan UPDATE
                                
                                noviProizvod.create();
                                refreshTableView();
                                ukupnaCijena.setText(Integer.toString(cijena));
                            }  
                         
                        } catch (SQLException e) {
                            System.out.println("Greska prilikom naplacivanja:"+ e.getMessage());
                        }
                    });
                }  
        }
    
        catch (SQLException sqlException) {
        } finally {
            try {
                if(statement != null) {
                    connection.close();
                }
            } catch (SQLException sqlException) {
            } 
            try {
                if(connection != null) {
                    connection.close();
                }
            } catch (SQLException sqlException) {
            } 
        }  
   
    }

    
} 
  

    



