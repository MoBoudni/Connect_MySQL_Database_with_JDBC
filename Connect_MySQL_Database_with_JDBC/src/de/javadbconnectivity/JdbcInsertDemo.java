package de.javadbconnectivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Demonstriert das Einfügen neuer Datensätze in eine MySQL-Datenbank mit JDBC.
 * 
 * Diese Klasse zeigt, wie man:
 * - INSERT-Operationen mit SQL ausführt
 * - Die Anzahl betroffener Zeilen überprüft
 * - Nach dem Einfügen die aktualisierten Daten anzeigt
 * - Ordnungsgemäße Fehlerbehandlung und Ressourcenverwaltung implementiert
 * 
 * @author MoBoudni
 * @version 2.0
 */
public class JdbcInsertDemo {
    
    /** Datenbank-URL für MySQL-Verbindung */
    private static final String DB_URL = "jdbc:mysql://localhost:3306/demo";
    /** Datenbank-Benutzername */
    private static final String DB_USER = "root";
    /** Datenbank-Passwort */
    private static final String DB_PASSWORD = "Chakeb1978&";
    
    /**
     * Hauptmethode, die das Einfügen eines neuen Mitarbeiters demonstriert.
     * Fügt einen Mitarbeiter hinzu und zeigt anschließend alle Mitarbeiter an.
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
            
            // Neuen Mitarbeiter einfügen
            System.out.println("Füge neuen Mitarbeiter in die Datenbank ein...\n");
            
            int betroffeneZeilen = statement.executeUpdate(
                "INSERT INTO employees " +
                "(last_name, first_name, email, department, salary) " + 
                "VALUES " + 
                "('Wright', 'Eric', 'eric.wright@foo.com', 'HR', 33000.00)");
            
            System.out.println("Anzahl eingefügter Datensätze: " + betroffeneZeilen + "\n");
            
            // Prüfung durch Abrufen aller Mitarbeiter (sortiert nach Nachname)
            System.out.println("Aktualisierte Mitarbeiterliste:");
            System.out.println("Nachname, Vorname");
            System.out.println("------------------");
            
            ergebnisSet = statement.executeQuery("SELECT * FROM employees ORDER BY last_name");
            
            // Ergebnisse verarbeiten und anzeigen
            while (ergebnisSet.next()) {
                String nachname = ergebnisSet.getString("last_name");
                String vorname = ergebnisSet.getString("first_name");
                System.out.println(nachname + ", " + vorname);
            }
            
        } catch (Exception e) {
            System.err.println("Fehler beim Datenbankzugriff:");
            e.printStackTrace();
        } finally {
            // Ressourcen sicher schließen
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
