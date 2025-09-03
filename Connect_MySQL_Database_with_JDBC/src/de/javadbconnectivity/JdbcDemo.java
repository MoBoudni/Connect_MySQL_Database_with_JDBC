package de.javadbconnectivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Grundlegende JDBC-Demo-Klasse für einfache Datenbankverbindung und Abfragen.
 * 
 * Diese Klasse demonstriert:
 * - Aufbau einer einfachen Datenbankverbindung
 * - Erstellung und Ausführung von SQL-Statements
 * - Verarbeitung von ResultSets
 * - Ordnungsgemäße Ressourcenverwaltung
 * 
 * @author MoBoudni
 * @version 2.0
 */
public class JdbcDemo {
    
    /** Datenbank-URL mit SSL deaktiviert für lokale Entwicklung */
    private static final String DB_URL = "jdbc:mysql://localhost:3306/demo?useSSL=false";
    /** Datenbank-Benutzername (muss konfiguriert werden) */
    private static final String DB_USER = "root";
    /** Datenbank-Passwort (muss konfiguriert werden) */
    private static final String DB_PASSWORD = "password";
    
    /**
     * Hauptmethode, die eine einfache Datenbankabfrage demonstriert.
     * Verbindet zur Datenbank und zeigt alle Mitarbeiternamen an.
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
            
            // Statement für SQL-Abfragen erstellen
            statement = verbindung.createStatement();
            
            // SQL-Abfrage ausführen - alle Mitarbeiter abrufen
            ergebnisSet = statement.executeQuery("SELECT * FROM employees");
            
            // Ergebnisse verarbeiten und anzeigen
            System.out.println("Mitarbeiterliste:");
            System.out.println("Nachname, Vorname");
            System.out.println("------------------");
            
            while (ergebnisSet.next()) {
                String nachname = ergebnisSet.getString("last_name");
                String vorname = ergebnisSet.getString("first_name");
                System.out.println(nachname + ", " + vorname);
            }
            
        } catch (Exception e) {
            System.err.println("Fehler beim Datenbankzugriff:");
            e.printStackTrace();
        } finally {
            // Ressourcen in umgekehrter Reihenfolge schließen
            ressourcenSchliessen(ergebnisSet, statement, verbindung);
        }
    }
    
    /**
     * Schließt Datenbankressourcen sicher, um Resource-Leaks zu verhindern.
     * 
     * @param ergebnisSet das zu schließende ResultSet
     * @param statement das zu schließende Statement
     * @param verbindung die zu schließende Verbindung
     */
    private static void ressourcenSchliessen(ResultSet ergebnisSet, Statement statement, 
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
            System.err.println("Fehler beim Schließen der Datenbankressourcen:");
            e.printStackTrace();
        }
    }

}
