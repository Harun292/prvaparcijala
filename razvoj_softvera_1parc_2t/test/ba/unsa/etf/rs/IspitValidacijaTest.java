package ba.unsa.etf.rs;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(ApplicationExtension.class)
class IspitValidacijaTest {
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

    // Pomoćna funkcija za CSS
    boolean sadrziStil(TextField polje, String stil) {
        for (String s : polje.getStyleClass())
            if (s.equals(stil)) return true;
        return false;
    }

    @Test
    void testDuzinaImena(FxRobot robot) {
        robot.clickOn("#fldIme");
        robot.write("ab");

        TextField ime = robot.lookup("#fldIme").queryAs(TextField.class);
        assertTrue(sadrziStil(ime, "poljeNijeIspravno"));

        robot.write("c");
        assertTrue(sadrziStil(ime, "poljeIspravno"));

        robot.clickOn("#fldIme");
        robot.press(KeyCode.BACK_SPACE).release(KeyCode.BACK_SPACE);
        assertTrue(sadrziStil(ime, "poljeNijeIspravno"));
    }

    @Test
    void testZnakoviUImenu(FxRobot robot) {
        robot.clickOn("#fldIme");
        robot.write("Ana-Marija Peric-Simic");

        TextField ime = robot.lookup("#fldIme").queryAs(TextField.class);
        assertTrue(sadrziStil(ime, "poljeIspravno"));

        robot.write(".");
        assertTrue(sadrziStil(ime, "poljeNijeIspravno"));

        robot.press(KeyCode.BACK_SPACE).release(KeyCode.BACK_SPACE);
        robot.write("_");
        assertTrue(sadrziStil(ime, "poljeNijeIspravno"));

        robot.press(KeyCode.BACK_SPACE).release(KeyCode.BACK_SPACE);
        robot.write("3");
        assertTrue(sadrziStil(ime, "poljeNijeIspravno"));

        robot.press(KeyCode.BACK_SPACE).release(KeyCode.BACK_SPACE);
        robot.write("a"); // Još jedno slovo je dozvoljeno
        assertTrue(sadrziStil(ime, "poljeIspravno"));
    }


    @Test
    void testDuzinaPrezimena(FxRobot robot) {
        robot.clickOn("#fldPrezime");
        robot.write("ab");

        TextField ime = robot.lookup("#fldPrezime").queryAs(TextField.class);
        assertTrue(sadrziStil(ime, "poljeNijeIspravno"));

        robot.write("c");
        assertTrue(sadrziStil(ime, "poljeIspravno"));

        robot.press(KeyCode.BACK_SPACE).release(KeyCode.BACK_SPACE);
        assertTrue(sadrziStil(ime, "poljeNijeIspravno"));
    }

    @Test
    void testZnakoviUPrezimenu(FxRobot robot) {
        robot.clickOn("#fldPrezime");
        robot.write("abc def-ghi jkl-");

        TextField ime = robot.lookup("#fldPrezime").queryAs(TextField.class);
        assertTrue(sadrziStil(ime, "poljeIspravno"));

        robot.write(",");
        assertTrue(sadrziStil(ime, "poljeNijeIspravno"));

        robot.press(KeyCode.BACK_SPACE).release(KeyCode.BACK_SPACE);
        robot.write("!");
        assertTrue(sadrziStil(ime, "poljeNijeIspravno"));

        robot.press(KeyCode.BACK_SPACE).release(KeyCode.BACK_SPACE);
        robot.write("@");
        assertTrue(sadrziStil(ime, "poljeNijeIspravno"));

        robot.press(KeyCode.BACK_SPACE).release(KeyCode.BACK_SPACE);
        robot.write("-");
        assertTrue(sadrziStil(ime, "poljeIspravno"));

        robot.press(KeyCode.BACK_SPACE).release(KeyCode.BACK_SPACE);
        robot.write("0");
        assertTrue(sadrziStil(ime, "poljeNijeIspravno"));
    }

    @Test
    void testValidacijaEmail(FxRobot robot) {
        robot.clickOn("#fldEmail");
        robot.write("a@b");

        TextField ime = robot.lookup("#fldEmail").queryAs(TextField.class);
        assertTrue(sadrziStil(ime, "poljeIspravno"));

        robot.press(KeyCode.BACK_SPACE).release(KeyCode.BACK_SPACE);
        assertTrue(sadrziStil(ime, "poljeNijeIspravno"));

        robot.press(KeyCode.BACK_SPACE).release(KeyCode.BACK_SPACE);
        robot.press(KeyCode.BACK_SPACE).release(KeyCode.BACK_SPACE);
        robot.write("@b");
        assertTrue(sadrziStil(ime, "poljeNijeIspravno"));

        robot.press(KeyCode.BACK_SPACE).release(KeyCode.BACK_SPACE);
        robot.press(KeyCode.BACK_SPACE).release(KeyCode.BACK_SPACE);
        robot.write("abc");
        assertTrue(sadrziStil(ime, "poljeNijeIspravno"));
    }

    @Test
    void testUsernameDuzina(FxRobot robot) {
        robot.clickOn("#fldUsername");
        robot.write("abcdefghijklmnop");

        TextField ime = robot.lookup("#fldUsername").queryAs(TextField.class);
        assertTrue(sadrziStil(ime, "poljeIspravno"));

        robot.write("q");
        assertTrue(sadrziStil(ime, "poljeNijeIspravno"));

        robot.press(KeyCode.BACK_SPACE).release(KeyCode.BACK_SPACE);
        assertTrue(sadrziStil(ime, "poljeIspravno"));

        // Polje ne smije biti ni prazno
        KeyCode ctrl = KeyCode.CONTROL;
        if (System.getProperty("os.name").contains("Mac")) ctrl = KeyCode.COMMAND;

        robot.press(ctrl).press(KeyCode.A).release(KeyCode.A).release(ctrl);
        robot.press(KeyCode.BACK_SPACE).release(KeyCode.BACK_SPACE);
        assertTrue(sadrziStil(ime, "poljeNijeIspravno"));

        robot.write("Z");
        assertTrue(sadrziStil(ime, "poljeIspravno"));
    }

    @Test
    void testUsernameValidacija(FxRobot robot) {
        robot.clickOn("#fldUsername");
        robot.write("Aa109");

        TextField ime = robot.lookup("#fldUsername").queryAs(TextField.class);
        assertTrue(sadrziStil(ime, "poljeIspravno"));

        robot.write(",");
        assertTrue(sadrziStil(ime, "poljeNijeIspravno"));

        robot.press(KeyCode.BACK_SPACE).release(KeyCode.BACK_SPACE);
        robot.write("!");
        assertTrue(sadrziStil(ime, "poljeNijeIspravno"));

        robot.press(KeyCode.BACK_SPACE).release(KeyCode.BACK_SPACE);
        robot.write("@");
        assertTrue(sadrziStil(ime, "poljeNijeIspravno"));

        robot.press(KeyCode.BACK_SPACE).release(KeyCode.BACK_SPACE);
        robot.write("-"); // Minus nije dozvoljen
        assertTrue(sadrziStil(ime, "poljeNijeIspravno"));

        robot.press(KeyCode.BACK_SPACE).release(KeyCode.BACK_SPACE);
        robot.write("_"); // Donja crta je dozvoljena
        assertTrue(sadrziStil(ime, "poljeIspravno"));

        robot.press(KeyCode.BACK_SPACE).release(KeyCode.BACK_SPACE);
        robot.write(" x"); // Razmak nije dozvoljen
        assertTrue(sadrziStil(ime, "poljeNijeIspravno"));
    }


    @Test
    void testLozinkaDuzina(FxRobot robot) {
        robot.clickOn("#fldPassword");
        robot.write("abcdefg");

        TextField ime = robot.lookup("#fldPassword").queryAs(TextField.class);
        assertTrue(sadrziStil(ime, "poljeNijeIspravno"));

        robot.write("h");
        assertTrue(sadrziStil(ime, "poljeIspravno"));

        robot.press(KeyCode.BACK_SPACE).release(KeyCode.BACK_SPACE);
        assertTrue(sadrziStil(ime, "poljeNijeIspravno"));

        // Polje ne smije biti ni prazno
        KeyCode ctrl = KeyCode.CONTROL;
        if (System.getProperty("os.name").contains("Mac")) ctrl = KeyCode.COMMAND;

        robot.press(ctrl).press(KeyCode.A).release(KeyCode.A).release(ctrl);
        robot.press(KeyCode.BACK_SPACE).release(KeyCode.BACK_SPACE);
        assertTrue(sadrziStil(ime, "poljeNijeIspravno"));

        robot.write("abcdefgh");

        assertTrue(sadrziStil(ime, "poljeIspravno"));
    }
}