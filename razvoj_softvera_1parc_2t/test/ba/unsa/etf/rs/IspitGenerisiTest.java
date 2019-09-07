package ba.unsa.etf.rs;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(ApplicationExtension.class)
class IspitGenerisiTest {
    KorisniciModel model;
    @Start
    public void start (Stage stage) throws Exception {
        model = new KorisniciModel();
        model.napuni();
        KorisnikController ctrl = new KorisnikController(model);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/korisnici.fxml"));
        loader.setController(ctrl);
        Parent root = loader.load();
        stage.setTitle("Korisnici");
        stage.setScene(new Scene(root, 500, 275));
        stage.show();
        stage.toFront();
    }

    @Test
    void imaLiDugme(FxRobot robot) {
        Button btnGenerisi = robot.lookup("#btnGenerisi").queryAs(Button.class);
        assertNotNull(btnGenerisi);
    }

    @Test
    void generisiLista(FxRobot robot) {
        robot.clickOn("#btnGenerisi");
        robot.clickOn("Sijerčić Tarik");
        TextField username = robot.lookup("#fldUsername").queryAs(TextField.class);
        assertNotNull(username);

        assertEquals("tsijercic", username.getText());
    }

    @Test
    void generisiModel(FxRobot robot) {
        robot.clickOn("#btnGenerisi");
        ObservableList<Korisnik> korisniks = model.getKorisnici();
        String s = "";
        for(Korisnik k : korisniks)
            s += k.getUsername();
        String expected = "vljubovicadelictsijercicrfejzic";
        assertEquals(expected, s);
    }

    @Test
    void generisiNovi(FxRobot robot) {
        robot.clickOn("#btnDodaj");
        robot.clickOn("#fldIme").write("Čćšđž");
        robot.clickOn("#fldPrezime").write("ČĆŠĐŽčćšđž");
        robot.clickOn("#fldEmail").write("a@a.ba");
        robot.clickOn("#fldUsername").write("usr");
        robot.clickOn("#fldPassword").write("Password");

        robot.clickOn("Delić Amra");

        robot.clickOn("#btnGenerisi");
        ObservableList<Korisnik> korisniks = model.getKorisnici();
        assertEquals("cccsdzccsdz", korisniks.get(4).getUsername());
    }


    @Test
    void generisiNoviModel(FxRobot robot) {
        Platform.runLater(
                () ->  model.getKorisnici().add( new Korisnik("Žšćšđ", "žđščć", "a@b", "asdf", "asdf"))
        );

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        robot.clickOn("žđščć Žšćšđ");

        robot.clickOn("#btnGenerisi");
        ObservableList<Korisnik> korisniks = model.getKorisnici();
        assertEquals("zzdscc", korisniks.get(4).getUsername());
    }


    @Test
    void testPrazno(FxRobot robot) {
        robot.clickOn("#btnDodaj");
        robot.clickOn("#btnGenerisi");
        Platform.runLater(
                () ->  model.getKorisnici().add( new Korisnik("", "", "", "", ""))
        );
        robot.clickOn("#btnGenerisi");
        ObservableList<Korisnik> korisniks = model.getKorisnici();
        assertEquals("", korisniks.get(4).getUsername());
    }
}