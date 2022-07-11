package systemrestauracjiklientserver_v5;

import java.util.HashMap;

/**
 *
 * @author Piotr Utrata
 */

//serce aplikacji dzięki interfejsowi nie piszemy ciągle tych samych funkcji dla róznych obiektów
//Tworząc nowy rodzaj obiektów wystarczy zaimplementować w niej poniższe metody oraz dołączyć serializację, dodać rodzaj klasy typie wyliczeniowym i zaktualizować metody:
//sprawdzaczObiektu i nowyObiekt w klasie KomunikacjaObiektyPliki. Resztę robi za nas implementacje programu.

public interface IObiekty {
    public RodzajZapytania getRodzajZapytania();
    public void setRodzajZapytania(RodzajZapytania rodzaj);
    public HashMap<String, String> getDane();
    public void setDane(String[] tab);
    public String get(int numerPola);
    public void set(int numerPola, String a_nowaWartosc);
    public String [] getEtykiety();
    public String getEtykiete(int i);
}
