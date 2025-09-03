package de.jdbcStoredProzedures;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Demonstriert die Verwendung von JDBC PreparedStatement für Mitarbeiterabfragen
 * mit parametrisierten Queries für verbesserte Sicherheit und Performance.
 * 
 * Diese Klasse zeigt, wie man:
 * - Datenbankverbindungen aufbaut
 * - PreparedStatement mit Parametern verwendet
 * - Prepared Statements mit verschiedenen Parametern wiederverwendet
 * - Ressourcen ordnungsgemäß mit try-finally-Blöcken verwaltet
 * 
 * @author MoBoudni
 * @version 2.0
 */
public class Driver {
    
    /** Datenbank-URL für MySQL-Verbindung */
    private static final String DB_URL = "jdbc:mysql://localhost:3306/demo";
    /** Datenbank-Benutzername */
    private static final String DB_USER = "root";
    /** Datenbank-Passwort */
    private static final String DB_PASSWORD = "Chakeb1978&";
    
    /**
     * Hauptmethode, die die Verwendung von PreparedStatement mit Mitarbeiterabfragen demonstriert.
     * Führt zwei Abfragen mit verschiedenen Parametern aus, um die Wiederverwendung zu zeigen.
     * 
     * @param args Kommandozeilenargumente (werden nicht verwendet)
     * @throws SQLException wenn ein Datenbankzugriffsfehler auftritt
     */
    public static void main(String[] args) throws SQLException {
        Connection verbindung = null;
        PreparedStatement statement = null;
        ResultSet ergebnisSet = null;
        
        try {
            // Datenbankverbindung herstellen
            verbindung = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            
            // Parametrisierte Abfrage für Mitarbeitersuche vorbereiten
            statement = verbindung.prepareStatement(
                "SELECT * FROM employees WHERE salary > ? AND department = ?");
            
            // Erste Abfrage ausführen: Legal-Abteilung mit Gehalt > 80000
            statement.setDouble(1, 80000);
            statement.setString(2, "Legal");
            ergebnisSet = statement.executeQuery();
            
            System.out.println("Mitarbeiter in der Legal-Abteilung mit Gehalt > 80000:");
            anzeigen(ergebnisSet);
            
            // Prepared Statement mit anderen Parametern wiederverwenden
            System.out.println("\n\nMitarbeiter in der HR-Abteilung mit Gehalt > 25000:");
            statement.setDouble(1, 25000);
            statement.setString(2, "HR");
            ergebnisSet = statement.executeQuery();
            
            anzeigen(ergebnisSet);
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Ressourcen in umgekehrter Reihenfolge der Erstellung schließen
            ressourcenSchliessen(ergebnisSet, statement, verbindung);
        }
    }
    
    /**
     * Zeigt Mitarbeiterinformationen aus dem ResultSet in formatierter Form an.
     * 
     * @param ergebnisSet das ResultSet mit Mitarbeiterdaten
     * @throws SQLException wenn ein Datenbankzugriffsfehler auftritt
     */
    private static void anzeigen(ResultSet ergebnisSet) throws SQLException {
        while (ergebnisSet.next()) {
            String nachname = ergebnisSet.getString("last_name");
            String vorname = ergebnisSet.getString("first_name");
            double gehalt = ergebnisSet.getDouble("salary");
            String abteilung = ergebnisSet.getString("department");
            
            System.out.printf("%s, %s, %.2f, %s%n", nachname, vorname, gehalt, abteilung);
        }
    }
    
    /**
     * Schließt Datenbankressourcen sicher, um Resource-Leaks zu verhindern.
     * 
     * @param ergebnisSet das zu schließende ResultSet
     * @param statement das zu schließende Statement
     * @param verbindung die zu schließende Verbindung
     */
    private static void ressourcenSchliessen(ResultSet ergebnisSet, PreparedStatement statement, 
                                           Connection verbindung) {
        try {
            if (ergebnisSet != null) {
                ergebnisSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (verbindung != null) {
                verbindung.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
