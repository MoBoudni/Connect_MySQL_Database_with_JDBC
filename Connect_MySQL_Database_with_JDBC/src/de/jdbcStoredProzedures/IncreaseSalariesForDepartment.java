package de.jdbcStoredProzedures;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Demonstriert den Aufruf einer gespeicherten Prozedur, die Daten modifiziert,
 * und zeigt einen Vorher/Nachher-Vergleich der betroffenen Datensätze.
 * 
 * Diese Klasse zeigt, wie man:
 * - Gespeicherte Prozeduren aufruft, die Datenmodifikationen durchführen
 * - Daten vor und nach der Prozedurausführung anzeigt
 * - Hilfsmethoden für Datenanzeige und Ressourcenverwaltung verwendet
 * 
 * @author MoBoudni
 * @version 2.0
 */
public class IncreaseSalariesForDepartment {
    
    /** Datenbankverbindungs-URL */
    private static final String DB_URL = "jdbc:mysql://localhost:3306/demo";
    /** Datenbank-Benutzername */
    private static final String DB_USER = "root";
    /** Datenbank-Passwort */
    private static final String DB_PASSWORD = "Chakeb1978&";
    
    /**
     * Hauptmethode, die eine gespeichert
     * e Prozedur für Gehaltserhöhungen demonstriert.
     * Zeigt Mitarbeitergehälter vor und nach der Erhöhungsoperation an.
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
            int gehaltsErhoehung = 10000;
            
            // Aktuelle Gehälter vor der Modifikation anzeigen
            System.out.println("=== GEHÄLTER VOR DER ERHÖHUNG ===\n");
            gehaelterAnzeigen(verbindung, zielAbteilung);
            
            // Aufruf der gespeicherten Prozedur für Gehaltserhöhung vorbereiten
            statement = verbindung.prepareCall("{call increase_salaries_for_department(?, ?)}");
            
            // Prozedurparameter setzen
            statement.setString(1, zielAbteilung);
            statement.setDouble(2, gehaltsErhoehung);
            
            // Gespeicherte Prozedur ausführen
            System.out.println("\n\nRufe gespeicherte Prozedur auf: increase_salaries_for_department('" 
                             + zielAbteilung + "', " + gehaltsErhoehung + ")");
            statement.execute();
            System.out.println("Aufruf der gespeicherten Prozedur beendet");
            
            // Aktualisierte Gehälter nach der Modifikation anzeigen
            System.out.println("\n\n=== GEHÄLTER NACH DER ERHÖHUNG ===\n");
            gehaelterAnzeigen(verbindung, zielAbteilung);
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ressourcenSchliessen(verbindung, statement, null);
        }
    }
    
    /**
     * Zeigt aktuelle Gehaltsinformationen für Mitarbeiter der angegebenen Abteilung an.
     * 
     * @param verbindung die zu verwendende Datenbankverbindung
     * @param abteilungsName der Name der abzufragenden Abteilung
     * @throws SQLException wenn ein Datenbankzugriffsfehler auftritt
     */
    private static void gehaelterAnzeigen(Connection verbindung, String abteilungsName) 
            throws SQLException {
        PreparedStatement statement = null;
        ResultSet ergebnisSet = null;
        
        try {
            // Abfrage für Abteilungsmitarbeiter vorbereiten
            statement = verbindung.prepareStatement(
                "SELECT * FROM employees WHERE department = ? ORDER BY last_name");
            statement.setString(1, abteilungsName);
            
            // Abfrage ausführen und Ergebnisse verarbeiten
            ergebnisSet = statement.executeQuery();
            
            System.out.println("Abteilung: " + abteilungsName);
            System.out.println("Nachname, Vorname, Abteilung, Gehalt");
            System.out.println("----------------------------------");
            
            while (ergebnisSet.next()) {
                String nachname = ergebnisSet.getString("last_name");
                String vorname = ergebnisSet.getString("first_name");
                double gehalt = ergebnisSet.getDouble("salary");
                String abteilung = ergebnisSet.getString("department");
                
                System.out.printf("%s, %s, %s, %.2f%n", nachname, vorname, abteilung, gehalt);
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
     * @param verbindung die zu schließende Datenbankverbindung (kann null sein)
     * @param statement das zu schließende Statement (kann null sein)
     * @param ergebnisSet das zu schließende ResultSet (kann null sein)
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
     * Überladene Methode zum Schließen von Ressourcen, wenn keine Verbindung verfügbar ist.
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
