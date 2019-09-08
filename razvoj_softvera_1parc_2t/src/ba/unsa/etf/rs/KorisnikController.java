package ba.unsa.etf.rs;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;

public class KorisnikController {
    public TextField fldIme;
    public TextField fldPrezime;
    public TextField fldEmail;
    public TextField fldUsername;
    public ListView<Korisnik> listKorisnici;
    public PasswordField fldPassword;
    public Slider sliderGodinaRodjenja;

    private KorisniciModel model;

    public KorisnikController(KorisniciModel model) {
        this.model = model;
    }

    @FXML
    public void initialize() {
        listKorisnici.setItems(model.getKorisnici());
        listKorisnici.getSelectionModel().selectedItemProperty().addListener((obs, oldKorisnik, newKorisnik) -> {
            model.setTrenutniKorisnik(newKorisnik);
            listKorisnici.refresh();
         });

        model.trenutniKorisnikProperty().addListener((obs, oldKorisnik, newKorisnik) -> {
            if (oldKorisnik != null) {
                fldIme.textProperty().unbindBidirectional(oldKorisnik.imeProperty() );
                fldPrezime.textProperty().unbindBidirectional(oldKorisnik.prezimeProperty() );
                fldEmail.textProperty().unbindBidirectional(oldKorisnik.emailProperty() );
                fldUsername.textProperty().unbindBidirectional(oldKorisnik.usernameProperty() );
                fldPassword.textProperty().unbindBidirectional(oldKorisnik.passwordProperty() );
                sliderGodinaRodjenja.valueProperty().unbindBidirectional(oldKorisnik.godinaRodjenjaProperty());
            }
            if (newKorisnik == null) {
                fldIme.setText("");
                fldPrezime.setText("");
                fldEmail.setText("");
                fldUsername.setText("");
                fldPassword.setText("");
            }
            else {
                fldIme.textProperty().bindBidirectional( newKorisnik.imeProperty() );
                fldPrezime.textProperty().bindBidirectional( newKorisnik.prezimeProperty() );
                fldEmail.textProperty().bindBidirectional( newKorisnik.emailProperty() );
                fldUsername.textProperty().bindBidirectional( newKorisnik.usernameProperty() );
                fldPassword.textProperty().bindBidirectional( newKorisnik.passwordProperty() );
                sliderGodinaRodjenja.valueProperty().bindBidirectional(newKorisnik.godinaRodjenjaProperty());
            }
        });

        fldIme.textProperty().addListener((obs, oldIme, newIme) -> {
            if (!newIme.isEmpty()&&fldIme.getText().length()>=3&&provjeriImeIPrezime(fldIme.getText())) {
                fldIme.getStyleClass().removeAll("poljeNijeIspravno");
                fldIme.getStyleClass().add("poljeIspravno");
            } else {
                fldIme.getStyleClass().removeAll("poljeIspravno");
                fldIme.getStyleClass().add("poljeNijeIspravno");
            }
        });

        fldPrezime.textProperty().addListener((obs, oldIme, newIme) -> {
            if (!newIme.isEmpty()&&fldPrezime.getText().length()>=3&&provjeriImeIPrezime(fldPrezime.getText())) {
                fldPrezime.getStyleClass().removeAll("poljeNijeIspravno");
                fldPrezime.getStyleClass().add("poljeIspravno");
            } else {
                fldPrezime.getStyleClass().removeAll("poljeIspravno");
                fldPrezime.getStyleClass().add("poljeNijeIspravno");
            }
        });

        fldEmail.textProperty().addListener((obs, oldIme, newIme) -> {
            if (!newIme.isEmpty()&&provjeriEmail(fldEmail.getText())) {
                fldEmail.getStyleClass().removeAll("poljeNijeIspravno");
                fldEmail.getStyleClass().add("poljeIspravno");
            } else {
                fldEmail.getStyleClass().removeAll("poljeIspravno");
                fldEmail.getStyleClass().add("poljeNijeIspravno");
            }
        });

        fldUsername.textProperty().addListener((obs, oldIme, newIme) -> {
            if (!newIme.isEmpty()&&provjeriUserName(fldUsername.getText())&&fldUsername.getText().length()<=16) {
                fldUsername.getStyleClass().removeAll("poljeNijeIspravno");
                fldUsername.getStyleClass().add("poljeIspravno");
            } else {
                fldUsername.getStyleClass().removeAll("poljeIspravno");
                fldUsername.getStyleClass().add("poljeNijeIspravno");
            }
        });

        fldPassword.textProperty().addListener((obs, oldIme, newIme) -> {
            if (!newIme.isEmpty()&&fldPassword.getText().length()>=8) {
                fldPassword.getStyleClass().removeAll("poljeNijeIspravno");
                fldPassword.getStyleClass().add("poljeIspravno");
            } else {
                fldPassword.getStyleClass().removeAll("poljeIspravno");
                fldPassword.getStyleClass().add("poljeNijeIspravno");
            }
        });
    }

    public boolean provjeriImeIPrezime(String recenica)
    {
        for(int i=0;i<recenica.length();i++)
        {
         if(recenica.charAt(i)>='A'&&recenica.charAt(i)<='Z'||recenica.charAt(i)>='a'&&recenica.charAt(i)<='z'||recenica.charAt(i)==' '||recenica.charAt(i)=='-')
             continue;
         else if (recenica.charAt(i) == 'č' || recenica.charAt(i) == 'ć' || recenica.charAt(i) == 'ž' || recenica.charAt(i) == 'š' || recenica.charAt(i) == 'đ')
             continue;
         return false;
        }
        return true;
    }
    public boolean provjeriEmail(String email)
    {
        if(email.charAt(0) != '@' && email.charAt(email.length()-1) != '@' && email.contains("@")) return true;
        return false;
    }
    public boolean provjeriUserName(String user)
    {
        if(user.charAt(0)>='0'&&user.charAt(0)<='9')
            return false;
        for(int i=1;i<user.length();i++)
        {
            if(user.charAt(i)>='a'&&user.charAt(i)<='z'||user.charAt(i)>='A'&&user.charAt(i)<='Z'||user.charAt(i)>='0'&&user.charAt(i)<='9'||user.charAt(i)=='_')
                continue;
            return false;
        }
        return true;
    }

    public void dodajAction(ActionEvent actionEvent) {
        model.getKorisnici().add(new Korisnik("", "", "", "", ""));
        listKorisnici.getSelectionModel().selectLast();
    }

    public void krajAction(ActionEvent actionEvent) {
        System.exit(0);
    }
    public void Generisi(ActionEvent actionEvent)
    {
        for (Korisnik korisnik:model.getKorisnici()) {
            if(korisnik.getPrezime().length()==0||korisnik.getIme().length()==0) continue;;
            String pom=korisnik.getIme().substring(0,1)+korisnik.getPrezime();
            String pomoc="";
            for (int i = 0; i <pom.length() ; i++) {
                if(pom.charAt(i)=='č'||pom.charAt(i)=='ć'||pom.charAt(i)=='Č'||pom.charAt(i)=='Ć') {
                    pomoc+='c';
                }
                else if(pom.charAt(i)=='ž'||pom.charAt(i)=='Ž')pomoc+='z';
                else if(pom.charAt(i)=='š'||pom.charAt(i)=='Š')pomoc+='s';
                else if (pom.charAt(i)=='đ'||pom.charAt(i)=='Đ')pomoc+='d';
                else
                    pomoc+=pom.charAt(i);
                pomoc=pomoc.toLowerCase();
            }
            korisnik.setUsername(pomoc);
        }
    }
}
