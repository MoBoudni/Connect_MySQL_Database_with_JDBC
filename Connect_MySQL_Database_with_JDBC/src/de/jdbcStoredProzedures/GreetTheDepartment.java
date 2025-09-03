package de.jdbcStoredProzedures;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

/**
 * Demonstriert den Aufruf einer gespeicherten Prozedur mit einem INOUT-Parameter.
 * 
 * Diese Klasse zeigt, wie man:
 * - Gespeicherte Prozeduren mit INOUT-Parametern aufruft
 * - INOUT-Parameter sowohl für Eingabe als auch Ausgabe registriert
 * - Veränderte Werte aus INOUT-Parametern abruft
 * 
 * @author MoBoudni
 * @version 2.0
 */
public class GreetTheDepartment {
    
    /** Datenbankverbindungs-URL */
    private static final String DB_URL = "jdbc:mysql://localhost:3306/demo";
    /** Datenbank-Benutzername */
    private static final String DB_USER = "root";
    /** Datenbank-Passwort */
    private static final String DB_PASSWORD = "Chakeb1978&";
    
    /**
     * Hauptmethode, die den Aufruf einer gespeicherten Prozedur mit INOUT-Parameter demonstriert.
     * Ruft 'greet_the_department' auf, die den Eingabeparameterwert modifiziert.
     * 
     * @param args Kommandozeilenargumente (werden nicht verwendet)
     * @throws Exception wenn ein Datenbankzugriffsfehler auftritt
     */
    public static void main(String[] args) throws Exception {
        Connection verbindung = null;
        CallableStatement statement = null;
        
        try {
            // Datenbankverbindung herstellen
            verbindung = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            
            String zielAbteilung = "Engineering";
            
            // Aufruf der gespeicherten Prozedur vorbereiten
            statement = verbindung.prepareCall("{call greet_the_department(?)}");
            
            // INOUT-Parameter registrieren (fungiert sowohl als Eingabe als auch als Ausgabe)
            statement.registerOutParameter(1, Types.VARCHAR);
            statement.setString(1, zielAbteilung);
            
            // Gespeicherte Prozedur ausführen
            System.out.println("Rufe gespeicherte Prozedur auf: greet_the_department('" 
                             + zielAbteilung + "')");
            statement.execute();
            System.out.println("Aufruf der gespeicherten Prozedur beendet");
            
            // Veränderten INOUT-Parameter-Wert abrufen
            String grussErgebnis = statement.getString(1);
            System.out.println("\nGruß-Ergebnis: " + grussErgebnis);
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(verbindung, statement);
        }
    }
    
    /**
     * Schließt Datenbankressourcen sicher, um Resource-Leaks zu verhindern.
     * 
     * @param verbindung die zu schließende Datenbankverbindung
     * @param statement das zu schließende Callable Statement
     * @throws SQLException wenn ein Datenbankzugriffsfehler beim Schließen auftritt
     */
    private static void close(Connection verbindung, Statement statement) 
            throws SQLException {
        if (statement != null) {
            statement.close();
        }
        if (verbindung != null) {
            verbindung.close();
        }
    }
}
