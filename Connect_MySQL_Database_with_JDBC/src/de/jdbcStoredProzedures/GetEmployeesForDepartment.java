package de.jdbcStoredProzedures;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Demonstriert den Aufruf einer gespeicherten Prozedur, die ein ResultSet zurückgibt.
 * 
 * Diese Klasse zeigt, wie man:
 * - Gespeicherte Prozeduren aufruft, die ResultSets zurückgeben
 * - ResultSets verarbeitet, die von gespeicherten Prozeduren zurückgegeben werden
 * - Mehrere Szenarien der Ressourcenbereinigung handhabt
 * 
 * @author MoBoudni
 * @version 2.0
 */
public class GetEmployeesForDepartment {
    
    /** Datenbankverbindungs-URL */
    private static final String DB_URL = "jdbc:mysql://localhost:3306/demo";
    /** Datenbank-Benutzername */
    private static final String DB_USER = "root";
    /** Datenbank-Passwort */
    private static final String DB_PASSWORD = "Chakeb1978&";
    
    /**
     * Hauptmethode, die den Aufruf einer gespeicherten Prozedur demonstriert, die ein ResultSet zurückgibt.
     * Ruft 'get_employees_for_department' auf, um alle Mitarbeiter einer bestimmten Abteilung abzurufen.
     * 
     * @param args Kommandozeilenargumente (werden nicht verwendet)
     * @throws Exception wenn ein Datenbankzugriffsfehler auftritt
     */
    public static void main(String[] args) throws Exception {
        Connection verbindung = null;
        CallableStatement statement = null;
        ResultSet ergebnisSet = null;
        
        try {
            // Datenbankverbindung herstellen
            verbindung = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            
            String zielAbteilung = "Engineering";
            
            // Aufruf der gespeicherten Prozedur vorbereiten
            statement = verbindung.prepareCall("{call get_employees_for_department(?)}");
            
            // Eingabeparameter setzen (Abteilungsname)
            statement.setString(1, zielAbteilung);
            
            // Gespeicherte Prozedur ausführen
            System.out.println("Rufe gespeicherte Prozedur auf: get_employees_for_department('" 
                             + zielAbteilung + "')");
            statement.execute();
            System.out.println("Aufruf der gespeicherten Prozedur beendet.\n");
            
            // ResultSet abrufen und verarbeiten
            ergebnisSet = statement.getResultSet();
            anzeigen(ergebnisSet);
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(verbindung, statement, ergebnisSet);
        }
    }
    
    /**
     * Zeigt Mitarbeiterinformationen aus dem ResultSet in formatierter Form an.
     * 
     * @param ergebnisSet das ResultSet mit Mitarbeiterdaten
     * @throws SQLException wenn ein Datenbankzugriffsfehler auftritt
     */
    private static void anzeigen(ResultSet ergebnisSet) throws SQLException {
        System.out.println("Mitarbeiterliste:");
        System.out.println("Nachname, Vorname, Abteilung, Gehalt");
        System.out.println("------------------------------------");
        
        while (ergebnisSet.next()) {
            String nachname = ergebnisSet.getString("last_name");
            String vorname = ergebnisSet.getString("first_name");
            double gehalt = ergebnisSet.getDouble("salary");
            String abteilung = ergebnisSet.getString("department");
            
            System.out.printf("%s, %s, %s, %.2f%n", nachname, vorname, abteilung, gehalt);
        }
    }
    
    /**
     * Schließt alle Datenbankressourcen sicher, um Resource-Leaks zu verhindern.
     * 
     * @param verbindung die zu schließende Datenbankverbindung
     * @param statement das zu schließende Callable Statement
     * @param ergebnisSet das zu schließende ResultSet
     * @throws SQLException wenn ein Datenbankzugriffsfehler beim Schließen auftritt
     */
    private static void close(Connection verbindung, Statement statement,
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
    private static void close(Statement statement, ResultSet ergebnisSet) throws SQLException {
    	close(null, statement, ergebnisSet);
    }
}
