package systemrestauracjiklientserver_v5.SystemRestauracjiKlient;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import systemrestauracjiklientserver_v5.IObiekty;
import systemrestauracjiklientserver_v5.Modele.*;
import systemrestauracjiklientserver_v5.RodzajZapytania;
import systemrestauracjiklientserver_v5.SystemRestauracjiKlient_v5;

/**
 *
 * @author Piotr Utrata
 */
public class SystemRestauracji extends JFrame implements ActionListener {

    public KomunikacjaZServerem komunikacjaZServerem;
    private Uzytkownik user;
    public ListaObiektow listaObiektow = new ListaObiektow(this);
    private OknoObiektu oknoObiektu;
    JButton wyloguj;
    JPanel glowny = new JPanel(new GridLayout(1, 1, 5, 5));
    private JMenu uzytkownicy;
    private JMenuItem wyswietlUzytkownikow;
    private JMenuItem dodajUzytkownika;
    private JMenuItem edytujUzytkownika;
    private JMenuItem wyswietlProdukty;
    private JMenuItem dodajProdukt;
    private JMenuItem edytujProdukt;
    private JMenuItem usunProdukt;
    //dostajemy zalogowanego uzytkownika i komunikacje z serwerem
    public SystemRestauracji(Uzytkownik user, KomunikacjaZServerem komunikacjaZServerem) {
        super("System Restauracji");
        this.user = user;
        this.komunikacjaZServerem = komunikacjaZServerem;
        // jeśli zamykamy okno to musimy tez wysłać serwerowi info
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                Uzytkownik wylogujUser = new Uzytkownik();
                wylogujUser.setRodzajZapytania(RodzajZapytania.LOGOUT);
                komunikacjaZServerem.wyslijZapytanieDoServera(wylogujUser);
                System.exit(0);
            }
        });
    }

    public void init() {

        glowny.add(listaObiektow);
        add(glowny);

        JMenuBar menu = new JMenuBar();

        uzytkownicy = new JMenu("Uzytkownicy");
        wyswietlUzytkownikow = new JMenuItem("Wyswietl");
        wyswietlUzytkownikow.addActionListener(this);
        dodajUzytkownika = new JMenuItem("Dodaj");
        dodajUzytkownika.addActionListener(this);
        edytujUzytkownika = new JMenuItem("Edytuj");
        edytujUzytkownika.addActionListener(this);
        uzytkownicy.add(wyswietlUzytkownikow);
        uzytkownicy.add(dodajUzytkownika);
        uzytkownicy.add(edytujUzytkownika);
        menu.add(uzytkownicy);

        JMenu produkty = new JMenu("Produkty");
        wyswietlProdukty = new JMenuItem("Wyswietl");
        wyswietlProdukty.addActionListener(this);
        dodajProdukt = new JMenuItem("Dodaj");
        dodajProdukt.addActionListener(this);
        edytujProdukt = new JMenuItem("Edytuj");
        edytujProdukt.addActionListener(this);
        usunProdukt = new JMenuItem("Usun");
        usunProdukt.addActionListener(this);
        produkty.add(wyswietlProdukty);
        produkty.add(dodajProdukt);
        produkty.add(edytujProdukt);
        produkty.add(usunProdukt);
        menu.add(produkty);

        wyloguj = new JButton("Wyloguj");
        wyloguj.addActionListener(this);
        menu.add(wyloguj);
        setJMenuBar(menu);

        zaladujListe();

        this.setVisible(true);
        setSize(800, 600);
    }

// sprawdzamy czy uzytkownik to super admin - jesli tak to od razu wyswietlamy mu uzytkownikow i inne funkcje
    public void zaladujListe() {
        if ("1".equals(user.get(0))) {
            this.listaObiektow.setDaneListy((IObiekty) new Uzytkownik());
        } else {
            this.listaObiektow.setDaneListy(new Produkt());
            this.dodajUzytkownika.setVisible(false);
            this.edytujUzytkownika.setVisible(false);
            this.wyswietlUzytkownikow.setVisible(false);
            this.dodajProdukt.setVisible(false);
            this.edytujProdukt.setVisible(false);
            this.usunProdukt.setVisible(false);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object choose = e.getSource();
        if (choose == wyswietlUzytkownikow) {
            this.listaObiektow.setDaneListy((IObiekty) new Uzytkownik());
            this.glowny.add(this.listaObiektow);
        } else if (choose == wyswietlProdukty) {
            this.listaObiektow.setDaneListy(new Produkt());
            glowny.add(this.listaObiektow);
        } else if (choose == wyloguj) {
            this.setVisible(false);
            Uzytkownik wylogujUser = new Uzytkownik();
            wylogujUser.setRodzajZapytania(RodzajZapytania.LOGOUT);
            this.komunikacjaZServerem.wyslijZapytanieDoServera(wylogujUser);
            new OknoLogowania();
            this.dispose();
        } else if (choose == dodajProdukt) {
            this.listaObiektow.setDaneListy(new Produkt());
            glowny.add(this.listaObiektow);
            Produkt produkt = new Produkt();
            produkt.setRodzajZapytania(RodzajZapytania.POST);
            this.oknoObiektu = new OknoObiektu(this, produkt);
            this.oknoObiektu.setVisible(true);
        } else if (choose == dodajUzytkownika) {
            this.listaObiektow.setDaneListy((IObiekty) new Uzytkownik());
            glowny.add(this.listaObiektow);
            Uzytkownik uzytkownik = new Uzytkownik();
            uzytkownik.setRodzajZapytania(RodzajZapytania.POST);
            this.oknoObiektu = new OknoObiektu(this, (IObiekty) uzytkownik);
            this.oknoObiektu.setVisible(true);
        } else if (choose == edytujProdukt) {
            this.listaObiektow.setDaneListy(new Produkt());
            glowny.add(this.listaObiektow);
            Produkt produkt = (Produkt) listaObiektow.obiekty.get(listaObiektow.getSelectedIndex());
            produkt.setRodzajZapytania(RodzajZapytania.UPDATE);
            this.oknoObiektu = new OknoObiektu(this, produkt);
            this.oknoObiektu.setEdytowany(produkt);
            this.oknoObiektu.setVisible(true);
        } else if (choose == edytujUzytkownika) {
            this.listaObiektow.setDaneListy((IObiekty) new Uzytkownik());
            glowny.add(this.listaObiektow);
            Uzytkownik uzytkownik = (Uzytkownik) listaObiektow.obiekty.get(listaObiektow.getSelectedIndex());
            uzytkownik.setRodzajZapytania(RodzajZapytania.UPDATE);
            this.oknoObiektu = new OknoObiektu(this, (IObiekty) uzytkownik);
            this.oknoObiektu.setEdytowany((IObiekty) uzytkownik);
            this.oknoObiektu.setVisible(true);
        } else if (choose == usunProdukt) {
            this.listaObiektow.setDaneListy(new Produkt());
            glowny.add(this.listaObiektow);
            Produkt produkt = (Produkt) listaObiektow.obiekty.get(listaObiektow.getSelectedIndex());
            produkt.setRodzajZapytania(RodzajZapytania.DELETE);
            this.listaObiektow.edytujUsunZListy(produkt);
            this.komunikacjaZServerem.wyslijZapytanieDoServera(produkt);

        }
    }
}
