package systemrestauracjiklientserver_v5;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import systemrestauracjiklientserver_v5.SystemRestauracjiServer.Sesja;

/**
 *
 * @author Piotr Utrata
 * SystemRestauracjiServer to program obsługujący zapytania od klientów wymienione w pliku RodzajZapytania.
 * Oparty jest o interfejs IObiekty który należy dołączyć do każdego modelu Obiektu który chcemy obsłużyć
 * Głównym wyzwaniem było zrealizowanie programu w taki sposób by dodając nowy rodzaj obiektu wymagało to 
 * od nas jak najmniej pracy i ingerencji w istniejący kod.
 * Program wymaga dopracowania niektórych funkcji lub być może zaimplementowania w inny sposób, ale działa.
 * Moim głównym celem tworzenia tego typu programu było zrozumienie i nauka następujących elementów: 
 * -wykorzystanie strumieni przesyłu obiektów (w pierwszej wersji programu przesył obiektu zrealizowany był 
 * również w komunikacji z plikami, ale postanowiłem wykorzystać różne strumienie)
 * -implementacja i realizacja zapytań w oparciu o interfejs uzyskując generyczną obsługę obiektów
 * -wykorzystanie wątków
 * -wykorzystanie różnego rodzaju instrukcji i typów np enum
 * -obsługa błędów i wyjątków (wykorzystując różne funkcjonalności jest ich bardzo dużo)
 * Program można rozwinąć np o mapowanie - wtedy bedzie można dzielić modele serwera i klientów
 */

public class SystemRestauracjiServer_v5 {

    public static final int portServera = 3030;
    ServerSocket socketServer;

    SystemRestauracjiServer_v5() {
        try {
            socketServer = new ServerSocket(portServera);
            System.out.println("Serwer uruchomiony");
        } catch (IOException e) {
            System.out.println("Nie można utworzyć gniazda");
            System.exit(1);
        }
    }
//Server działa nieustannie 
    void dzialaj() {
        Socket socketKlient = null;
        try {
            while (true) {
                //test
                System.out.println("Test, w działaj czekamy na klienta");
                socketKlient = socketServer.accept();
                new Sesja(socketKlient).start();
            }
        } catch (IOException e) {
            System.out.println("Problem połączenia z klientem " + e);
        } finally {
            try {
                socketServer.close();
            } catch (IOException e) {
                System.out.println("Problem z zamykaniem połączenia");
            }
        }
    }

    public static void main(String args[]) {
        SystemRestauracjiServer_v5 server = new SystemRestauracjiServer_v5();
        server.dzialaj();
    }

}
