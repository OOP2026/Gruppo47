package database_connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Gestisce la connessione al database PostgreSQL utilizzando il pattern architetturale Singleton.
 * <p>
 * Questa classe garantisce che venga creata e mantenuta un'unica istanza di connessione
 * condivisa per l'intero ciclo di vita dell'applicazione. Tutti i Data Access Object (DAO)
 * richiedono la connessione a questa classe per eseguire le query SQL.
 * </p>
 */
public class ConnessioneDatabase {

    /**
     * L'unica istanza statica della classe (Pattern Singleton).
     */
    private static ConnessioneDatabase instance;

    /**
     * L'oggetto di connessione fisica al database.
     */
    public Connection connection = null;

    private String nome = "postgres";
    private String password = "***"; // <-- PASSWORD OSCURATA PER SICUREZZA
    private String url = "jdbc:postgresql://localhost:5432/Orario";
    private String driver = "org.postgresql.Driver";

    /**
     * Costruttore privato, accessibile solo dall'interno della classe stessa.
     * <p>
     * Si occupa di caricare in memoria il driver JDBC per PostgreSQL e di stabilire
     * la connessione fisica al database utilizzando le credenziali fornite.
     * </p>
     *
     * @throws SQLException Se si verifica un errore durante il tentativo di connessione al database
     *                      (es. credenziali errate o server offline).
     */
    private ConnessioneDatabase() throws SQLException {
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, nome, password);
        } catch (ClassNotFoundException ex) {
            System.out.println("Database Connection Creation Failed: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Restituisce l'istanza globale e univoca della classe {@code ConnessioneDatabase}.
     * <p>
     * Se l'istanza non esiste ancora, o se la connessione precedente è stata chiusa
     * inavvertitamente, il metodo si occupa di crearne una nuova. Altrimenti,
     * restituisce quella già attiva.
     * </p>
     *
     * @return L'istanza Singleton di {@code ConnessioneDatabase}.
     * @throws SQLException Se la creazione di una nuova connessione fallisce.
     */
    public static ConnessioneDatabase getInstance() throws SQLException {
        if (instance == null) {
            instance = new ConnessioneDatabase();
        } else if (instance.connection.isClosed()) {
            instance = new ConnessioneDatabase();
        }
        return instance;
    }

    /**
     * Restituisce l'oggetto connessione pronto per eseguire le query SQL (Statement e PreparedStatement).
     *
     * @return L'oggetto {@link Connection} attivo verso il database PostgreSQL.
     */
    public Connection getConnection() {
        return connection;
    }
}