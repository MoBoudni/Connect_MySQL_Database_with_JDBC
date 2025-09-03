package de.javadbconnectivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Demonstriert das Aktualisieren bestehender Datensätze in einer MySQL-Datenbank mit JDBC.
 * 
 * Diese Klasse zeigt, wie man:
 * - UPDATE-Operationen mit SQL ausführt
 * - Daten vor und nach der Aktualisierung anzeigt
 * - PreparedStatement für sichere Abfragen verwendet
 * - Die Anzahl betroffener Zeilen überprüft
 * - Ordnungsgemäße Ressourcenverwaltung implementiert
 * 
 * @author MoBoudni
 * @version 2.0
 */
public class JdbcUpdateDemo {
    
    /** Datenbank-URL für MySQL-Verbindung */
    private static final String DB_URL = "jdbc:mysql://localhost:3306/demo";
    /** Datenbank-Benutzername */
    private static final String DB_USER = "root";
    /** Datenbank-Passwort */
    private static final String DB_PASSWORD = "Chakeb1978&";
    
    /**
     * Hauptmethode, die das Aktualisieren von Mitarbeiterdaten demonstriert.
     * Zeigt Mitarbeiterdaten vor und nach der Aktualisierung der E-Mail-Adresse an.
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
            
            System.out.println("Datenbankverbindung erfolgreich hergestellt!\n");
            
            // Statement für SQL-Operationen erstellen
            statement = verbindung.createStatement();
            
            // Hilfsmethode aufrufen, um Mitarbeiterinformationen vor der Aktualisierung anzuzeigen
            System.out.println("VOR DER AKTUALISIERUNG...");
            mitarbeiterAnzeigen(verbindung, "John", "Doe");
            
            // E-Mail-Adresse des Mitarbeiters aktualisieren
            System.out.println("\nFÜHRE AKTUALISIERUNG FÜR JOHN DOE AUS...\n");
            
            int betroffeneZeilen = statement.executeUpdate(
                "UPDATE employees " +
                "SET email='john.doe@luv2code.com' " +
                "WHERE last_name='Doe' AND first_name='John'");
            
            System.out.println("Anzahl aktualisierter Datensätze: " + betroffeneZeilen);
            
            // Hilfsmethode aufrufen, um die aktualisierten Mitarbeiterinformationen anzuzeigen
            System.out.println("\nNACH DER AKTUALISIERUNG...");
            mitarbeiterAnzeigen(verbindung, "John", "Doe");
            
        } catch (Exception e) {
            System.err.println("Fehler beim Datenbankzugriff:");
            e.printStackTrace();
        } finally {
            // Ressourcen sicher schließen
            close(verbindung, statement, ergebnisSet);
        }
    }
    
    /**
     * Zeigt Informationen eines bestimmten Mitarbeiters an.
     * Verwendet PreparedStatement für sichere parametrisierte Abfragen.
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
            // Parametrisierte Abfrage für Mitarbeitersuche vorbereiten
            statement = verbindung.prepareStatement(
                "SELECT last_name, first_name, email FROM employees WHERE last_name=? AND first_name=?");
            
            // Parameter setzen (Reihenfolge beachten: 1=Nachname, 2=Vorname)
            statement.setString(1, nachname);
            statement.setString(2, vorname);
            
            // SQL-Abfrage ausführen
            ergebnisSet = statement.executeQuery();
            
            // Ergebnisse verarbeiten und formatiert anzeigen
            boolean mitarbeiterGefunden = false;
            
            while (ergebnisSet.next()) {
                String derNachname = ergebnisSet.getString("last_name");
                String derVorname = ergebnisSet.getString("first_name");
                String email = ergebnisSet.getString("email");
                
                System.out.printf("Mitarbeiter: %s %s, E-Mail: %s%n", derVorname, derNachname, email);
                mitarbeiterGefunden = true;
            }
            
            // Meldung ausgeben, wenn kein Mitarbeiter gefunden wurde
            if (!mitarbeiterGefunden) {
                System.out.println("Mitarbeiter nicht gefunden: " + vorname + " " + nachname);
            }
            
        } catch (Exception e) {
            System.err.println("Fehler beim Anzeigen der Mitarbeiterdaten:");
            e.printStackTrace();
        } finally {
            // Lokale Ressourcen schließen
            close(statement, ergebnisSet);
        }
    }
    
    /**
     * Schließt alle Datenbankressourcen sicher, um Resource-Leaks zu verhindern.
     * Diese Methode wird im finally-Block der Hauptmethode aufgerufen.
     * 
     * @param verbindung die zu schließende Datenbankverbindung (kann null sein)
     * @param statement das zu schließende Statement (kann null sein)
     * @param ergebnisSet das zu schließende ResultSet (kann null sein)
     * @throws SQLException wenn ein Datenbankzugriffsfehler beim Schließen auftritt
     */
    private static void close(Connection verbindung, Statement statement,
                                           ResultSet ergebnisSet) throws SQLException {
        // Ressourcen in umgekehrter Reihenfolge der Erstellung schließen
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
     * Überladene Methode zum Schließen von Statement und ResultSet ohne Connection.
     * Wird für lokale Ressourcen in Hilfsmethoden verwendet.
     * 
     * @param statement das zu schließende Statement (kann null sein)
     * @param ergebnisSet das zu schließende ResultSet (kann null sein)
     * @throws SQLException wenn ein Datenbankzugriffsfehler beim Schließen auftritt
     */
    private static void close(Statement statement, ResultSet ergebnisSet)
            throws SQLException {
        // Delegiert an die Hauptmethode mit null als Connection
        close(null, statement, ergebnisSet);
    }
}