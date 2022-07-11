package systemrestauracjiklientserver_v5;

import systemrestauracjiklientserver_v5.SystemRestauracjiKlient.*;

/**
 *
 * @author Piotr Utrata
 * SystemRestauracjiKlient aplikacja kliencka wysyłająca zapytania do serwera i tworząca interfejs użytkownika
 * Aplikacja obsługująca zapytania, ale niedokończona ponieważ bardzo duzo czasu zajęła praca nad stroną serwera. Wymaga pracy nad logiką i interfejsem.
 * Podobnie jak strona serwera wykorzystująca interfejs IObiekty dzięki czemu nie powtarzamy kodu.
 * Wykorzystujemy w niej elementy interfejsu uzytkownika jak: JFrame, JDialog, JTable, JPanel, ActionListener, KeyListener, WindowListener
 * oraz strumien Obiektow dla komunikacji z serwerem
 * 
 */

public class SystemRestauracjiKlient_v5 {

    public static void main(String args[]) {
        OknoLogowania logowanie = new OknoLogowania();
    }
}
