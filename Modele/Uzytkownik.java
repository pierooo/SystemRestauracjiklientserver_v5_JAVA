package systemrestauracjiklientserver_v5.Modele;

import java.io.Serializable;
import java.util.HashMap;
import systemrestauracjiklientserver_v5.IObiekty;
import systemrestauracjiklientserver_v5.RodzajZapytania;

/**
 *
 * @author Piotr Utrata
 */
public class Uzytkownik implements IObiekty, Serializable{
    private RodzajZapytania rodzaj;
    private final String[] ETYKIETY = {
        "Id",
        "Imie",
        "Kod"
    };

    private final HashMap<String, String> dane = new HashMap<>();

    public Uzytkownik(String[] tab) {
        for (int i = 0; i < ETYKIETY.length; i++) {
            this.dane.put(ETYKIETY[i], tab[i]);
        }
    }

    public Uzytkownik() {
        for (int i = 0; i < ETYKIETY.length; i++) {
            this.dane.put(ETYKIETY[i], "");
        }
    }
    
    @Override
    public HashMap<String, String> getDane() {
        return this.dane;
    }

    @Override
    public void setDane(String[] tab) {
        for (int i = 0; i < ETYKIETY.length; i++) {
            this.dane.put(ETYKIETY[i], tab[i]);
        }
    }

    @Override
    public String get(int numerPola) {
        return dane.get(ETYKIETY[numerPola]);
    }

    @Override
    public void set(int numerPola, String a_nowaWartosc) {
        dane.put(ETYKIETY[numerPola], a_nowaWartosc);
    }

    @Override
    public RodzajZapytania getRodzajZapytania() {
        return this.rodzaj;
    }

    @Override
    public void setRodzajZapytania(RodzajZapytania rodzaj) {
        this.rodzaj = rodzaj;
    }

    @Override
    public String[] getEtykiety() {
        return this.ETYKIETY;
    }

    @Override
    public String getEtykiete(int i) {
        return this.ETYKIETY[i];
    }
}
