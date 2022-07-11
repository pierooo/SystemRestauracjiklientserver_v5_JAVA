package systemrestauracjiklientserver_v5.SystemRestauracjiKlient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import systemrestauracjiklientserver_v5.IObiekty;
import systemrestauracjiklientserver_v5.RodzajZapytania;
import systemrestauracjiklientserver_v5.SystemRestauracjiKlient_v5;


/**
 *
 * @author Piotr Utrata
 */
public class KomunikacjaZServerem {
//korzystamy ze strumienia obiektów
    private Socket socket;
    private ObjectOutputStream strumienWyjsciaObiektu;
    private ObjectInputStream strumienWejsciaObiektu;

    private ArrayList<IObiekty> obiektyOdSerwera = null;
    private IObiekty obiektOdServera = null;

    public KomunikacjaZServerem(SystemRestauracji system) {
        try {
            socket = new Socket("localhost", 3030);
        } catch (IOException ex) {
            System.out.print("Wyjątek tworzenia socketu: " + ex);
        }
        try {
            System.out.println("Przygotowujemy strumienie");
            this.strumienWyjsciaObiektu = new ObjectOutputStream(socket.getOutputStream());
            try {
                this.strumienWejsciaObiektu = new ObjectInputStream(socket.getInputStream());
                System.out.println("Strumienie gotowe do pracy");
            } catch (IOException | NullPointerException ex) {
                System.out.print("Wyjątek 1 tworzenia strumieni: " + ex);
            }
        } catch (IOException ex) {
            System.out.print("Wyjątek 2 tworzenia strumieni: " + ex);
        }
    }

    public void wyslijZapytanieDoServera(IObiekty obiekt) {
        this.obiektyOdSerwera = null;
        this.obiektOdServera = null;

        System.out.println(
                "Proba wyslania do servera: " + obiekt.getRodzajZapytania() + " | " + obiekt.get(2));
        try {
            this.strumienWyjsciaObiektu.writeObject(obiekt);
            System.out.println("Wyslano pytanie do servera ");
            System.out.println("Czekamy na odpowiedz serwera");
            //obsługujemy odpowiedzi od serwera
            try {
                if (obiekt.getRodzajZapytania() == RodzajZapytania.GETALL) {
                    System.out.println("Pobieramy obiekt od servera");

                    obiektyOdSerwera = (ArrayList<IObiekty>) this.strumienWejsciaObiektu.readObject();

                    if (obiektyOdSerwera != null) {
                        //test
                        System.out.println("Czytamy listę od servera");
                        for (var o : obiektyOdSerwera) {
                            for (int i = 0; i < o.getDane().size(); i++) {
                                System.out.print(o.get(i) + " | ");
                            }
                            System.out.println();
                        }
                    }
                } else {
                    System.out.println("Czytamy obiekt od servera");
                    obiektOdServera = (IObiekty) this.strumienWejsciaObiektu.readObject();
                    //test
                    System.out.println(obiektOdServera.get(0) + " | " + obiektOdServera.get(1));
                }
            } catch (IOException | ClassNotFoundException ex) {
                System.out.print("Wyjątek odbierania obiektu: " + ex);
            }

        } catch (IOException ex) {
            System.out.print("Wyjątek wysyłania obiektu: " + ex);
        }
    }

    public ArrayList<IObiekty> getListaObiektow() {
        return this.obiektyOdSerwera;
    }

    public IObiekty getObiektPowrotny() {
        return this.obiektOdServera;

    }
}
