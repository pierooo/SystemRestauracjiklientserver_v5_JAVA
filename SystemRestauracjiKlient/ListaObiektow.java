package systemrestauracjiklientserver_v5.SystemRestauracjiKlient;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import systemrestauracjiklientserver_v5.IObiekty;
import systemrestauracjiklientserver_v5.Modele.*;
import systemrestauracjiklientserver_v5.RodzajZapytania;
import systemrestauracjiklientserver_v5.SystemRestauracjiKlient_v5;


/**
 *
 * @author Piotr Utrata
 */

public class ListaObiektow extends JPanel {

    private String rodzajObiektu = "";
    ArrayList<IObiekty> obiekty = new ArrayList<>();
    //tabela dla odbieranych danych - wybrałem JTable zamiast JList by ladnie wyklądało, ale nie dziala mi getSelectedIndex
    JTable tabela = new JTable() {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    JScrollPane pojemnikTabeli = new JScrollPane();
    SystemRestauracji system;
    IObiekty obiekt;
    DefaultTableModel modelTabeli;

    public ListaObiektow(SystemRestauracji system) {
        setLayout(new BorderLayout());
        TitledBorder border = new TitledBorder(rodzajObiektu);
        border.setTitleColor(Color.RED);
        setBorder(border);
        this.system = system;
        //dozwolone jedyne pojedyncze zaznaczenie na liście by nie robilo błędów
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    }

    // pobieramy dane z bazy i ustawiamy w tabeli
    public void setDaneListy(IObiekty obiekt) {
        this.obiekt = obiekt;
        if (new Produkt().getClass() == obiekt.getClass()) {
            //zapisujemy jaki rodzaj klasy
            rodzajObiektu = "Produkty";
        } else if (new Uzytkownik().getClass() == obiekt.getClass()) {
            rodzajObiektu = "Uzytkownicy";
        }
        this.obiekt.setRodzajZapytania(RodzajZapytania.GETALL);
        system.komunikacjaZServerem.wyslijZapytanieDoServera(obiekt);

        obiekty = system.komunikacjaZServerem.getListaObiektow();
        modelTabeli = new DefaultTableModel();
        dodajKolumny(obiekt);
        for (IObiekty o : obiekty) {
            dodajWiersz(o);
        }
        odswiezDane();
    }

    public void dodajWiersz(IObiekty obiekt) {
        String[] dane = new String[obiekt.getEtykiety().length];
        for (int j = 1; j < obiekt.getEtykiety().length; j++) {
            dane[j - 1] = obiekt.get(j);
            System.out.println(dane[j - 1]);
        }
        modelTabeli.addRow(dane);
    }

    //ustawiamy dane w tabeli przy edycji lub usuwaniu
    public void edytujUsunZListy(IObiekty obiekt) {
        modelTabeli = new DefaultTableModel();
        dodajKolumny(obiekt);
        for (IObiekty o : obiekty) {
            String[] dane = new String[obiekt.getEtykiety().length];
            for (int j = 1; j < obiekt.getEtykiety().length; j++) {
                if (o.get(0).equals(obiekt.get(0))) {
                    if (obiekt.getRodzajZapytania() == RodzajZapytania.DELETE) {
                        dane = null;
                    } else {
                        dane[j - 1] = obiekt.get(j);
                    }
                } else {
                    dane[j - 1] = o.get(j);
                }
            }
            if (dane != null) {
                modelTabeli.addRow(dane);
            }
        }
        odswiezDane();
    }

    public void dodajKolumny(IObiekty obiekt) {
        String[] ETYKIETY = obiekt.getEtykiety();
        for (int i = 1; i < ETYKIETY.length; i++) {
            modelTabeli.addColumn(ETYKIETY[i]);
        }
    }

    public void odswiezDane() {
        tabela.setModel(modelTabeli);
        tabela.setVisible(true);
        //tabela potrzebuje pojemnika
        pojemnikTabeli.setViewportView(tabela);
        this.add(pojemnikTabeli);
    }

    public int getSelectedIndex() {
        //funkcja nie działa i zwraca -1 - do poprawy
        return tabela.getSelectedRow() + 1;
    }
}
