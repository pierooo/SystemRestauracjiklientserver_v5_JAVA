package systemrestauracjiklientserver_v5.systemrestauracjiserver;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.Integer.parseInt;
import java.util.ArrayList;
import systemrestauracjiklientserver_v5.IObiekty;
import systemrestauracjiklientserver_v5.Modele.*;
import systemrestauracjiklientserver_v5.RodzajKlasy;

/**
 *
 * @author Piotr Utrata
 */
public class KomunikacjaObiektyPliki {
// obiekt przekazanyod klienta
    private final IObiekty obiekt;
// obiekt zwracany jako odpowiedz
    public IObiekty obiektPowrotny;
// jeśli rodzaj pytania to GETALL to wysyłamy listę
    public ArrayList<IObiekty> listaPowrotna;
// enum potrzebny do rozpoznania obiektu
    private RodzajKlasy klasa = null;

    public KomunikacjaObiektyPliki(IObiekty obiekt) {
        this.obiekt = obiekt;
    }
//w zależności od rodzaju ustawionego zapytania od klienta wywołanie odpowiedniej reakcji przy czym w zalezności od dostarczonego obiektu wywołanie funkcji ustawiającej nazwę pliku
    public void realizujZapytanie() {
        switch (obiekt.getRodzajZapytania()) {
            case LOGIN ->
                obiektPowrotny = logowanie(sprawdzaczObiektu(), obiekt);
            case GET ->
                obiektPowrotny = odczytElementu(sprawdzaczObiektu(), obiekt);
            case GETALL ->
                listaPowrotna = odczytWszystkichElementow(sprawdzaczObiektu(), obiekt);
            case POST ->
                obiektPowrotny = zapiszNowyElement(sprawdzaczObiektu(), obiekt);
            case DELETE ->
                obiektPowrotny = usunElement(sprawdzaczObiektu(), obiekt);
            case UPDATE ->
                obiektPowrotny = aktualizujElement(sprawdzaczObiektu(), obiekt);
        }
    }
//ustawienie nazwy pliku dla obiektu odebranego od klienta oraz nazwy klasy jakiej ten obiekt jest - porównanie do obiektów
// wykorzystanie typu enum
    private String sprawdzaczObiektu() {
        System.out.println("Sprawdzamy rodzaj obiektu i ustawiamy nazwe pliku");
        if (new Produkt().getClass() == obiekt.getClass()) {
            //zapisujemy jaki rodzaj klasy
            klasa = RodzajKlasy.Produkt;
            return "produkty.txt";
        }else if(new Uzytkownik().getClass() == obiekt.getClass()){
            klasa = RodzajKlasy.Uzytkownik;
            return "uzytkownicy.txt";
        }
        return null;
    }
//stworzenie nowego pustego obiektu takiego jaki otrzymaliśmy od klienta (potrzebne by nie odwoływać się do referencji obiektu otrzymanego)
// wykorzystanie typu enum
    private IObiekty nowyObiekt(RodzajKlasy rodzajKlasy) {
        switch (rodzajKlasy) {
            case Produkt -> {
                return new Produkt();
            }
            case Uzytkownik -> {
                return new Uzytkownik();
            }
        }
        return null;
    }
// jesli w rodzaju zapytania otrzymamy LOGIN wywołana zostanie ta metoda, nejpierw sprawdzamy czy mamy jakiegos
    IObiekty logowanie(String nazwaPliku, IObiekty obiekt){
        ArrayList<IObiekty> obiekty = odczytWszystkichElementow(nazwaPliku, obiekt);
        IObiekty obiektDlaOdpowiedzi = nowyObiekt(klasa);
        obiektDlaOdpowiedzi.setRodzajZapytania(obiekt.getRodzajZapytania());
        //logujac sie po raz pierwszy - kiedy plik z danymi jest pusty tworzymy admina
        if(obiekty.isEmpty()){
            IObiekty admin = nowyObiekt(klasa);
            admin.setDane(new String[]{"", "admin", "admin"});
            zapiszNowyElement(nazwaPliku, admin);
            return admin;
        }
        
        for (IObiekty ob : obiekty) {
            //test
            //System.out.println(ob.get(2) + " | " + obiekt.get(2));
            //koniec testu
            //sprawdzamy kod użytkownika
            if (ob.get(2).equals(obiekt.get(2))) {
                System.out.println("Podany obiekt istnieje");
                return ob;
            }
        }
        return obiektDlaOdpowiedzi;
    }
//zapisanie do pliku zaktualizowanej listy obiektow
    public boolean zapiszPlik(String nazwaPliku, ArrayList<IObiekty> obiekty) {
        System.out.println("Zapisywanie do pliku");
        IObiekty o = nowyObiekt(klasa);
        try ( PrintWriter printWriter = new PrintWriter(new FileWriter(nazwaPliku))) {
            String[] ETYKIETY = o.getEtykiety();
            for (IObiekty ob : obiekty) {
                for (int i = 0; i < ETYKIETY.length; i++) {
                    printWriter.print(ETYKIETY[i] + ";" + ob.get(i) + "\n");
                }
            }
            printWriter.close();
            return true;
        } catch (IOException ioe) {
            System.out.println("Bład komunikacji z plikiem podczas zapisywania nowego obiektu." + ioe.getMessage());
        }
        return false;
    }
//
    public IObiekty zapiszNowyElement(String nazwaPliku, IObiekty o) {
        System.out.println("Zapis nowego elementu: ");
        ArrayList<IObiekty> obiekty = odczytWszystkichElementow(nazwaPliku, o);
        IObiekty obiektDlaOdpowiedzi = nowyObiekt(klasa);
        obiektDlaOdpowiedzi.setRodzajZapytania(o.getRodzajZapytania());
        String ostatnieId = "0";
        //pobranie z listy ostatniego Id obiektu
        if (!obiekty.isEmpty()) {
            for (IObiekty ob : obiekty) {
                ostatnieId = ob.get(0);
            }
        }
        //zapisujac nowy element ustawiamy jego Id 
        int ostatnieIdWLiczbie = parseInt(ostatnieId);
        ostatnieIdWLiczbie++;
        o.set(0, Integer.toString(ostatnieIdWLiczbie));
        obiekty.add(o);
        if (zapiszPlik(nazwaPliku, obiekty)) {
            return o;
        }
        return obiektDlaOdpowiedzi;
    }

    public ArrayList<IObiekty> odczytWszystkichElementow(String nazwaPliku, IObiekty o) {
        //testy
//        System.out.println("Wyswietlamy nazwe pliku i obiekt: " + nazwaPliku + " ||| " + o.getRodzajZapytania());
//
//        ArrayList<IObiekty> listaObiektow = new ArrayList<>();
//        IObiekty test1 = nowyObiekt(klasa);
//        test1.setDane(new String[]{"2", "test", "10"});
//        listaObiektow.add(test1);
//
//        IObiekty test2 = nowyObiekt(klasa);
//        test2.setDane(new String[]{"3", "test2", "1012"});
//        listaObiektow.add(test2);
//
//        System.out.println("Obiekty w metodzie odczytWszystkichObiektow: ");
//        for (var ob : listaObiektow) {
//            ob.getDane().forEach((k, v) -> System.out.println(k + " " + v));
//        }

        ArrayList<IObiekty> obiektyzPliku = new ArrayList<>();
        
        try ( BufferedReader reader = new BufferedReader(new FileReader(nazwaPliku))) {
            String linia;
            String[] wartosci = new String[o.getEtykiety().length];
            int i = 0;
            obiektyzPliku = new ArrayList<>();
            while ((linia = reader.readLine()) != null) {
                IObiekty obiektWPetli = nowyObiekt(klasa);
                String[] dane = linia.split(";");     
                wartosci[i] = dane[1];
                i++;
                //jeśli długośc etykiet obiektu jest równa iteracji dodajemy obiekt do listy
                if (i == o.getEtykiety().length) {
                    i = 0;
                    obiektWPetli.setDane(wartosci);
                    obiektyzPliku.add(obiektWPetli);
                }
            }
            reader.close();
            return obiektyzPliku;
        } catch (IOException ioe) {
            System.out.println("Nie odnaleziono pliku" + ioe.getMessage());
        }
        return obiektyzPliku;
    }

    public IObiekty odczytElementu(String nazwaPliku, IObiekty o) {
        //testy
//        System.out.println("Wyswietlamy nazwe pliku i obiekt: " + nazwaPliku + " ||| " + o.getRodzajZapytania());
//        IObiekty test = nowyObiekt(klasa);
//        test.setDane(new String[]{"2", "test", "10"});
//        test.setRodzajZapytania(o.getRodzajZapytania());
//        System.out.println("Dane biektu w metodzie odczytElementu: " + test.get(0) + " | " + test.get(1));
//        System.out.println("Sprawdzamy czy poprawnie dodano rodzaj zapytania: " + test.getRodzajZapytania());

//odczyt obiektu po Id
        ArrayList<IObiekty> obiekty = odczytWszystkichElementow(nazwaPliku, o);
        IObiekty obiektDlaOdpowiedzi = nowyObiekt(klasa);
        obiektDlaOdpowiedzi.setRodzajZapytania(o.getRodzajZapytania());
        for (IObiekty ob : obiekty) {
            System.out.println(ob.get(0) + " | " + o.get(0));
            if (ob.get(0).equals(o.get(0))) {
                System.out.println("Podany obiekt istnieje");
                return ob;
            }
        }
        return obiektDlaOdpowiedzi;
    }
//sprawdzanie czy obiekt istnieje i usuwanie
    public IObiekty usunElement(String nazwaPliku, IObiekty o) {
        ArrayList<IObiekty> obiekty = odczytWszystkichElementow(nazwaPliku, o);
        IObiekty obiektDlaOdpowiedzi = nowyObiekt(klasa);
        obiektDlaOdpowiedzi.setRodzajZapytania(o.getRodzajZapytania());
        for (IObiekty ob : obiekty) {
            if (ob.get(0).equals(o.get(0))) {
                System.out.println("Obiekt id: " + ob.get(0) + "istnieje");
                obiekty.remove(ob);
                zapiszPlik(nazwaPliku, obiekty);
                return obiektDlaOdpowiedzi;
            }
        }
        return null;
    }

    private IObiekty aktualizujElement(String nazwaPliku, IObiekty o) {
        ArrayList<IObiekty> obiekty = odczytWszystkichElementow(nazwaPliku, o);
        IObiekty obiektDlaOdpowiedzi = nowyObiekt(klasa);
        obiektDlaOdpowiedzi.setRodzajZapytania(o.getRodzajZapytania());

        int i = 0;
        for (IObiekty ob : obiekty) {
            if (ob.get(0).equals(o.get(0))) {
                //test
                System.out.println("Obiekt id: " + ob.get(0) + " istnieje");
                obiekty.set(i, o);
                if (zapiszPlik(nazwaPliku, obiekty)) {
                    return o;
                }
            }
            i++;
        }
        return obiektDlaOdpowiedzi;
    }
}
