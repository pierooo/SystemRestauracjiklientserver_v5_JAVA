package systemrestauracjiklientserver_v5.SystemRestauracjiKlient;

import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import systemrestauracjiklientserver_v5.IObiekty;
import systemrestauracjiklientserver_v5.Modele.*;

/**
 *
 * @author Piotr Utrata
 */

//panel obiektu dla dodawania, edytowania generowany według przekazanego obiektu
public class PanelObiektu extends JPanel {

    private String rodzajObiektu = "";
    IObiekty obiekt;
    private final JTextField[] pola;

    public PanelObiektu(SystemRestauracji systemRestauracji, IObiekty obiekt) {
        setLayout(new GridLayout(obiekt.getEtykiety().length, 2));
        if (new Produkt().getClass() == obiekt.getClass()) {
            //zapisujemy jaki rodzaj klasy by wyświetlić info
            rodzajObiektu = "Produkty";
        } else if (new Uzytkownik().getClass() == obiekt.getClass()) {
            rodzajObiektu = "Uzytkownicy";
        }
        this.obiekt = obiekt;
        pola = new JTextField[obiekt.getEtykiety().length];
        for (int i = 1; i < obiekt.getEtykiety().length; i++) {
            add(new JLabel(obiekt.getEtykiete(i)));
            pola[i] = new JTextField();
            add(pola[i]);
        }
        TitledBorder border = new TitledBorder("Edytuj listę " + rodzajObiektu);
        border.setTitleColor(Color.RED);
        setBorder(border);
        setVisible(true);
    }

    public String getPole(int i) {
        return pola[i].getText();
    }

    public void setPole(int i, String value) {
        pola[i].setText(value);
    }
}