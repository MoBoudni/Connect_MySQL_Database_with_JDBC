package de.jdbcStoredProzedures;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

/**
 * Demonstriert den Aufruf einer gespeicherten Prozedur, die einen Zählwert 
 * über einen OUT-Parameter zurückgibt.
 * 
 * Diese Klasse zeigt, wie man:
 * - Gespeicherte Prozeduren mit OUT-Parametern aufruft
 * - OUT-Parameter mit spezifischen SQL-Typen registriert
 * - Werte aus OUT-Parametern nach der Ausführung abruft
 * 
 * @author MoBoudni
 * @version 2.0
 */
public class GetCountForDepartment {
    
    /** Datenbankverbindungs-URL */
    private static final String DB_URL = "jdbc:mysql://localhost:3306/demo";
    /** Datenbank-Benutzername */
    private static final String DB_USER = "root";
    /** Datenbank-Passwort */
    private static final String DB_PASSWORD = "Chakeb1978&";
    
    /**
     * Hauptmethode, die den Aufruf einer gespeicherten Prozedur mit OUT-Parameter demonstriert.
     * Ruft 'get_count_for_department' auf, um die Mitarbeiteranzahl für eine bestimmte Abteilung zu erhalten.
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
            
            // Aufruf der gespeicherten Prozedur mit IN- und OUT-Parametern vorbereiten
            statement = verbindung.prepareCall("{call get_count_for_department(?, ?)}");
            
            // Eingabeparameter setzen (Abteilungsname)
            statement.setString(1, zielAbteilung);
            
            // Ausgabeparameter registrieren (Mitarbeiteranzahl)
            statement.registerOutParameter(2, Types.INTEGER);
            
            // Gespeicherte Prozedur ausführen
            System.out.println("Rufe gespeicherte Prozedur auf: get_count_for_department('" 
                             + zielAbteilung + "', ?)");
            statement.execute();
            System.out.println("Aufruf der gespeicherten Prozedur beendet");
            
            // OUT-Parameter-Wert abrufen
            int mitarbeiterAnzahl = statement.getInt(2);
            System.out.println("\nMitarbeiteranzahl in der Abteilung " + zielAbteilung + ": " 
                             + mitarbeiterAnzahl);
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ressourcenSchliessen(verbindung, statement);
        }
    }
    
    /**
     * Schließt Datenbankressourcen sicher, um Resource-Leaks zu verhindern.
     * 
     * @param verbindung die zu schließende Datenbankverbindung
     * @param statement das zu schließende Callable Statement
     * @throws SQLException wenn ein Datenbankzugriffsfehler beim Schließen auftritt
     */
    private static void ressourcenSchliessen(Connection verbindung, Statement statement) 
            throws SQLException {
        if (statement != null) {
            statement.close();
        }
        if (verbindung != null) {
            verbindung.close();
        }
    }
}