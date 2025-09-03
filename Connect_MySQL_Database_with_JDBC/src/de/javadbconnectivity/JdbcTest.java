package de.javadbconnectivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Einfache JDBC-Testklasse zur Überprüfung der Datenbankverbindung.
 * 
 * Diese Klasse dient als:
 * - Grundlegender Verbindungstest zur MySQL-Datenbank
 * - Einfache Demonstration von JDBC-Grundlagen
 * - Template für weitere JDBC-Anwendungen
 * 
 * @author Ihr Name
 * @version 1.0
 */
public class JdbcTest {
    
    /** Datenbank-URL für MySQL-Verbindung */
    private static final String DB_URL = "jdbc:mysql://localhost:3306/demo";
    /** Datenbank-Benutzername */
    private static final String DB_USER = "root";
    /** Datenbank-Passwort */
    private static final String DB_PASSWORD = "Chakeb1978&";
    
    /**
     * Hauptmethode, die eine einfache Datenbankverbindung testet.
     * Verbindet zur Datenbank und zeigt alle Mitarbeiter an.
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
            
            System.out.println("Datenbankverbindung erfolgreich!\n");
            
            // Statement für SQL-Abfragen erstellen
            statement = verbindung.createStatement();
            
            // SQL-Abfrage ausführen - alle Mitarbeiter abrufen
            ergebnisSet = statement.executeQuery("SELECT * FROM employees");
            
            // Ergebnisse verarbeiten und anzeigen
            System.out.println("Mitarbeiterliste aus der Datenbank:");
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
            // Ressourcen ordnungsgemäß schließen
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