package systemrestauracjiklientserver_v5.SystemRestauracjiKlient;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import systemrestauracjiklientserver_v5.IObiekty;
import systemrestauracjiklientserver_v5.Modele.Uzytkownik;
import systemrestauracjiklientserver_v5.RodzajZapytania;

/**
 *
 * @author Piotr Utrata
 */
public class OknoLogowania extends JDialog implements ActionListener, KeyListener {

    private SystemRestauracji systemRestauracji;
    KomunikacjaZServerem komunikacjaZServerem;
    private Uzytkownik probaLogowania = new Uzytkownik();
    private final JButton wejdz;
    private final JTextField kod;

    public OknoLogowania() {
        this.setTitle("Logowanie Restauracja");
        setSize(300, 150);
        setLocation(50, 100);
        setLayout(new FlowLayout());
        this.add(new JLabel("Podaj Swój KOD"));
        kod = new JTextField(100);
        kod.setHorizontalAlignment(JTextField.CENTER);
        kod.addKeyListener(this);
        wejdz = new JButton("Zatwierdz");
        wejdz.addActionListener(this);
        this.add(kod);
        this.add(wejdz);
        this.setVisible(true);
        //dodajemy listener zamykający okno (dodałem bo w systemie restauracji też mam a tam dodatkowo kończy komunikację z serverem)
        //tutaj komunikację albo przekazujemy dalej jeśli zalogowano, albo server sam zamyka wątek po nieudanym logowaniu
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                System.exit(0);
            }
        });
    }

    public void zaloguj() {
        probaLogowania.setRodzajZapytania(RodzajZapytania.LOGIN);
        //bierzemy z pola kod użytkownika
        probaLogowania.set(2, kod.getText());
        System.out.println(probaLogowania.get(1) + " | " + probaLogowania.get(2));
        //rozpoczynamy komunikację z serverem
        komunikacjaZServerem = new KomunikacjaZServerem(systemRestauracji);
        komunikacjaZServerem.wyslijZapytanieDoServera((IObiekty) probaLogowania);

        if (komunikacjaZServerem.getObiektPowrotny() != null) {
            System.out.println("Sprawdzamy czy kod istnieje");
            System.out.println(komunikacjaZServerem.getObiektPowrotny().get(1) + " | " + komunikacjaZServerem.getObiektPowrotny().get(2));
            if (komunikacjaZServerem.getObiektPowrotny().get(2).equals(probaLogowania.get(2))) {
                //otwieramy system restauracji po otrzymaniu od servera odpowiedzi (serwer zwraca po sprawdzeniu pusty obiekt jesli nie znalazł lub ten sam jesli istnieje)
                //przekazujemy komunikację do systemu restauracji i on dalej według zapotrzebowania
                systemRestauracji = new SystemRestauracji((Uzytkownik) komunikacjaZServerem.getObiektPowrotny(), this.komunikacjaZServerem);
                //ładujemy system restauracji
                systemRestauracji.init();
                this.setVisible(false);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object button = e.getSource();
        if (button == wejdz) {
            zaloguj();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            zaloguj();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
