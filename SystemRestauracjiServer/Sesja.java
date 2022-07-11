package systemrestauracjiklientserver_v5.SystemRestauracjiServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import systemrestauracjiklientserver_v5.IObiekty;
import systemrestauracjiklientserver_v5.RodzajZapytania;
import systemrestauracjiklientserver_v5.systemrestauracjiserver.KomunikacjaObiektyPliki;

/**
 *
 * @author Piotr Utrata
 */
public //twozenie wątku uzytkownika 
class Sesja extends Thread {

    private final Socket socketKlient;
    ObjectInputStream strumienWejsciaObiektu;
    ObjectOutputStream strumienWyjsciaObiektu;
//otwarcie strumieni, wykorzystałem tutaj strumienie obiektów (komunikacja z plikami jest stworzona na strumieniach tekstowych)
    public Sesja(Socket socket) {
        this.socketKlient = socket;
        System.out.println("Test, wątek wystartował ");
        try {
            System.out.println("Przygotowujemy strumienie dla klienta");
            this.strumienWyjsciaObiektu = new ObjectOutputStream(socket.getOutputStream());
            try {
                this.strumienWejsciaObiektu = new ObjectInputStream(socket.getInputStream());
                System.out.println("Strumienie dla klienta gotowe do pracy");
            } catch (IOException | NullPointerException ex) {
                System.out.print("Wyjątek 1 tworzenia strumieni: " + ex);
            }
        } catch (IOException ex) {
            System.out.print("Wyjątek 2 tworzenia strumieni: " + ex);
        }
    }

    @Override
    public void run() {
        //czekamy na zapytania od klienta
        while (true) {
            System.out.println("Czekamy na pytania klienta ");
            try {
                System.out.println("Proba odczytu obiektu od klienta ");
                IObiekty obiekt = (IObiekty) strumienWejsciaObiektu.readObject();
                System.out.println("Odczytano obiekt ");
                if (obiekt != null) {
                    //test
                    System.out.println("Test, mamy obiekt od klienta ");
                    System.out.println("Obiekt od Klienta: " + obiekt);
                    //jeśli mamy LOGOUT to kończymy wątek
                    if (obiekt.getRodzajZapytania() != RodzajZapytania.LOGOUT) {
                        //nowa komunikacja obiekty pliki
                        KomunikacjaObiektyPliki komunikacja = new KomunikacjaObiektyPliki(obiekt);
                        komunikacja.realizujZapytanie();
                        
                        //jeśli mamy GETALL to pobieramy liste od komunikacji
                        if (obiekt.getRodzajZapytania() == RodzajZapytania.GETALL) {
                            //test
                            for (var o : komunikacja.listaPowrotna) {
                                System.out.println(o.get(0) + " | " + o.get(1) + " | " + o.get(2));
                            }
                            strumienWyjsciaObiektu.writeObject(komunikacja.listaPowrotna);
                        } else {
                            //test
                            System.out.println(komunikacja.obiektPowrotny.get(0) + " | " + komunikacja.obiektPowrotny.get(1));
                            strumienWyjsciaObiektu.writeObject(komunikacja.obiektPowrotny);
                            //jesli pytaniem było LOGIN to sprawdzamy czy kod uzytkownika wystepuje w pliku, jeśli nie ma go to kończymy wątek
                            if (obiekt.getRodzajZapytania() == RodzajZapytania.LOGIN) {
                                if (!komunikacja.obiektPowrotny.get(2).equals(obiekt.get(2))) {
                                    Thread.currentThread().interrupt();
                                    break;
                                }
                            }
                        }
                        System.out.println("Wysyłamy do klienta odpowiedz");
                    } else {
                        //jeśli mamy rodzaj zapytania LOGOUT to kończymy wątek
                        strumienWyjsciaObiektu.writeObject(obiekt);
                        Thread.currentThread().interrupt();
                        System.out.println("Zatrzymano wątek");
                        break;
                    }
                }
            } catch (NullPointerException exc) {
                System.out.println("Brak informacji jaki rodzaj zapytania." + exc.getMessage());
            } catch (ClassNotFoundException ex) {
                System.out.println("Nie znaleziono klasy obiektu przesłanego przez klienta" + ex.getMessage());
            } catch (IOException e) {
                //zrobić logi do pliku
            }
        }
    }
}
