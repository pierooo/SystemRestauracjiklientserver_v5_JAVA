package systemrestauracjiklientserver_v5.SystemRestauracjiKlient;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import systemrestauracjiklientserver_v5.IObiekty;
import systemrestauracjiklientserver_v5.RodzajZapytania;
import systemrestauracjiklientserver_v5.SystemRestauracjiKlient_v5;


/**
 *
 * @author Piotr Utrata
 */

//tworzymy okno obiekto by go dodać/edytować
//dodajemy do okna panel obiektu generowany według przekazanego obiektu
public class OknoObiektu extends JDialog implements ActionListener {

    private SystemRestauracji systemRestauracji;
    private PanelObiektu panelObiektu;
    private IObiekty obiekt;
    private JButton zatwierdz;

    public OknoObiektu(SystemRestauracji systemRestauracji, IObiekty obiekt) {
        this.setTitle("Edycja Listy");
        this.systemRestauracji = systemRestauracji;
        this.obiekt = obiekt;
        setSize(300, 200);
        setLocation(50, 100);
        setLayout(new FlowLayout());
        setModal(true);
        this.panelObiektu = new PanelObiektu(systemRestauracji, obiekt);
        zatwierdz = new JButton("Zatwierdz");
        zatwierdz.addActionListener(this);
        this.add(panelObiektu);
        this.add(zatwierdz);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object button = e.getSource();
        if (button == zatwierdz) {
            this.setVisible(false);
            for (int i = 1; i < obiekt.getEtykiety().length; i++) {
                this.obiekt.set(i, this.panelObiektu.getPole(i));
            }
            systemRestauracji.komunikacjaZServerem.wyslijZapytanieDoServera(obiekt);
            if (obiekt.getRodzajZapytania() == RodzajZapytania.UPDATE) {
                systemRestauracji.listaObiektow.edytujUsunZListy(obiekt);
            } else if (obiekt.getRodzajZapytania() == RodzajZapytania.POST) {
                systemRestauracji.listaObiektow.dodajWiersz(obiekt);
            }
            systemRestauracji.listaObiektow.odswiezDane();
            systemRestauracji.glowny.add(systemRestauracji.listaObiektow);
            this.setVisible(false);
        }
    }
//ustawiamy pola w panelu według przekazanego obiektu
    public void setEdytowany(IObiekty obiekt) {
        for (int i = 1; i < obiekt.getEtykiety().length; i++) {
            panelObiektu.setPole(i, obiekt.get(i));
        }
    }
}
