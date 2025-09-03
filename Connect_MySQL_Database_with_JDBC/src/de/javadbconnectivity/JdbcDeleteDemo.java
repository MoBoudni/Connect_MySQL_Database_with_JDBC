package de.javadbconnectivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Demonstriert das Löschen von Datensätzen aus einer MySQL-Datenbank mit JDBC.
 * 
 * Diese Klasse zeigt, wie man:
 * - DELETE-Operationen mit SQL ausführt
 * - Daten vor und nach dem Löschvorgang anzeigt
 * - PreparedStatement für sichere Abfragen verwendet
 * - Hilfsmethoden für Anzeige und Ressourcenverwaltung nutzt
 * 
 * @author MoBoudni
 * @version 2.0
 */
public class JdbcDeleteDemo {
    
    /** Datenbank-URL für MySQL-Verbindung */
    private static final String DB_URL = "jdbc:mysql://localhost:3306/demo";
    /** Datenbank-Benutzername */
    private static final String DB_USER = "root";
    /** Datenbank-Passwort */
    private static final String DB_PASSWORD = "Chakeb1978&";
    
    /**
     * Hauptmethode, die das Löschen eines Mitarbeiters demonstriert.
     * Zeigt den Mitarbeiter vor und nach dem Löschvorgang an.
     * 
     * @param args Kommandozeilenargumente (werden nicht verwendet)
     * @throws SQLException wenn ein Datenbankzugriffsfehler auftritt
     */
    public static void main(String[] args) throws SQLException {
        Connection verbindung = null;
        Statement statement = null;
        ResultSet ergebnisSet = null;
        
        try {
            // Datenbankverbindung herstellen
            verbindung = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            
            // Statement für SQL-Operationen erstellen
            statement = verbindung.createStatement();
            
            // Hilfsmethode aufrufen, um Mitarbeiterinformationen vor dem Löschen anzuzeigen
            System.out.println("VOR DEM LÖSCHEN...");
            mitarbeiterAnzeigen(verbindung, "John", "Doe");
            
            // Mitarbeiter löschen
            System.out.println("\nLÖSCHE MITARBEITER: John Doe\n");
            
            int betroffeneZeilen = statement.executeUpdate(
                    "DELETE FROM employees " +
                    "WHERE last_name='Doe' AND first_name='John'");
            
            System.out.println("Anzahl gelöschter Datensätze: " + betroffeneZeilen);
            
            // Hilfsmethode aufrufen, um zu prüfen, ob der Mitarbeiter gelöscht wurde
            System.out.println("\nNACH DEM LÖSCHEN...");
            mitarbeiterAnzeigen(verbindung, "John", "Doe");
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ressourcenSchliessen(verbindung, statement, ergebnisSet);
        }
    }
    
    /**
     * Zeigt Informationen eines bestimmten Mitarbeiters an oder meldet, wenn er nicht gefunden wurde.
     * 
     * @param verbindung die Datenbankverbindung
     * @param vorname der Vorname des zu suchenden Mitarbeiters
     * @param nachname der Nachname des zu suchenden Mitarbeiters
     * @throws SQLException wenn ein Datenbankzugriffsfehler auftritt
     */
    private static void mitarbeiterAnzeigen(Connection verbindung, String vorname, String nachname) 
            throws SQLException {
        PreparedStatement statement = null;
        ResultSet ergebnisSet = null;
        
        try {
            // Parametrisierte Abfrage vorbereiten
            statement = verbindung.prepareStatement(
                "SELECT last_name, first_name, email FROM employees WHERE last_name=? AND first_name=?");
            statement.setString(1, nachname);
            statement.setString(2, vorname);
            
            // SQL-Abfrage ausführen
            ergebnisSet = statement.executeQuery();
            
            // Ergebnisse verarbeiten
            boolean gefunden = false;
            
            while (ergebnisSet.next()) {
                String derNachname = ergebnisSet.getString("last_name");
                String derVorname = ergebnisSet.getString("first_name");
                String email = ergebnisSet.getString("email");
                
                System.out.printf("Mitarbeiter gefunden: %s %s, %s%n", derVorname, derNachname, email);
                gefunden = true;
            }
            
            if (!gefunden) {
                System.out.println("Mitarbeiter NICHT GEFUNDEN: " + vorname + " " + nachname);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ressourcenSchliessen(statement, ergebnisSet);
        }
    }
    
    /**
     * Schließt alle Datenbankressourcen sicher, um Resource-Leaks zu verhindern.
     * 
     * @param verbindung die zu schließende Datenbankverbindung
     * @param statement das zu schließende Statement
     * @param ergebnisSet das zu schließende ResultSet
     * @throws SQLException wenn ein Datenbankzugriffsfehler beim Schließen auftritt
     */
    private static void ressourcenSchliessen(Connection verbindung, Statement statement,
                                           ResultSet ergebnisSet) throws SQLException {
        if (ergebnisSet != null) {
            ergebnisSet.close();
        }
        if (statement != null) {
            statement.close();
        }
        if (verbindung != null) {
            verbindung.close();
        }
    }
    
    /**
     * Überladene Methode zum Schließen von Statement und ResultSet.
     * 
     * @param statement das zu schließende Statement
     * @param ergebnisSet das zu schließende ResultSet
     * @throws SQLException wenn ein Datenbankzugriffsfehler beim Schließen auftritt
     */
    private static void ressourcenSchliessen(Statement statement, ResultSet ergebnisSet)
            throws SQLException {
        ressourcenSchliessen(null, statement, ergebnisSet);
    }
}