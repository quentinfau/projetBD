package projetBD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Client {
	static final String CONN_URL = "jdbc:oracle:thin:@im2ag-oracle.e.ujf-grenoble.fr:1521:ufrima";

	static final String USER = "salema";
	static final String PASSWD = "bd2015";
	static final Requete req = new Requete();
	
	static String IdClient = null;

	private static void connexion() {

		System.out.print("Loading Oracle driver... ");
		try {
			// Enregistrement du driver Oracle
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			System.out.println("loaded");

			// Etablissement de la connection
			System.out.print("Connecting to the database... ");
			conn = DriverManager.getConnection(CONN_URL, USER, PASSWD);
			stmt = conn.createStatement();
			System.out.println("connected");

			// Desactivation de l'autocommit
			conn.setAutoCommit(false);
			System.out.println("Autocommit disabled");
		} catch (SQLException e) {

			e.printStackTrace();
		}

	}

	static Connection conn;
	static Statement stmt;
	

	private static void menu() {

		System.out.println("*** Choisir une action a effectuer : ***");
		System.out.println("0 : Quitter");
		System.out.println("1 : Creer Client");
		System.out.println("2 : Connexion");
		System.out.println("3 : Créer un album");
		System.out.println("4 : Télécharger une image");
		System.out.println("5 : DÃ©clarer une nouvelle maladie pour un animal (et mettre Ã  jour son nombre de maladie)");
		System.out.println("6 : Commit");
		System.out.println("7 : Test DeadLock");
		System.out.println("8 : setIsolation");
		System.out.println("9 : Rollback");
		System.out.println("10 : Drop table");
		System.out.println("11 : Test");
		System.out.println("12 : Rollback");
	}

	private static void creerClient() throws SQLException {
		req.createClient(stmt);
	}
	
	private static void connexionClient() throws SQLException {
		IdClient = req.connexionClient(stmt);
	}

	private static void creerAlbum() throws SQLException {
		req.createAlbum(stmt, IdClient);

	}

	private static void AddImage() throws SQLException {
		req.getContenuTable(stmt);
	}
		

	private static void dropTable() throws SQLException {
		req.dropTable(stmt);

	}
	
	private static void test() throws SQLException {
		stmt.executeQuery("drop table test");
		//req.executeFile(stmt, "src/main/resources/test.sql");

		//stmt.executeQuery("insert into test values (12, 'Spinnard')");
		//stmt.executeQuery("insert into test values (13, 'Spinnardo')");
	}

	private static void Q4() throws SQLException {
		int var = LectureClavier.lireEntier("nouvelle valeur");
		stmt.executeUpdate("update LesGardiens set noCage=" + var + " where nomE='Labbe' and noCage=12");
	}

	private static void Q5() throws SQLException {
		stmt.executeQuery("insert into LesMaladies values ('Alexis','Sida')");
		stmt.executeUpdate(
				"update LesAnimaux set nb_maladies=(select nb_maladies from LesAnimaux where nomA='Alexis')+1 where nomA='Alexis'");
	}

	private static void commit() throws SQLException {
		conn.commit();
	}

	private static void rollback() throws SQLException {
		conn.rollback();
	}

	private static void testDeadLock() throws SQLException {
		int var = LectureClavier.lireEntier("nouvelle valeur");
		stmt.executeUpdate("update LesAnimaux set noCage=" + var + " where nomA='Alexis'");
		var = LectureClavier.lireEntier("nouvelle valeur");
		stmt.executeUpdate("update LesAnimaux set noCage=" + var + " where nomA='Tintin'");
	}

	private static void setIsolation() throws SQLException {
		conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
	}

	public static void main(String args[]) {

		try {

			int action;
			boolean exit = false;

			connexion();

			while (!exit) {
				menu();
				action = LectureClavier.lireEntier("votre choix ?");
				switch (action) {
				case 0:
					exit = true;
					break;
				case 1:
					creerClient();
					break;
				case 2:
					connexionClient();
					break;
				case 3:
					creerAlbum();
					break;
				case 4:
					AddImage();
					break;
				case 5:
					Q5();
					break;
				case 6:
					commit();
					break;
				case 7:
					testDeadLock();
					break;
				case 8:
					setIsolation();
					break;
				case 9:
					rollback();
					break;
				case 10:
					dropTable();
					break;
				case 11:
					test();
					break;
				default:
					System.out.println("=> choix incorrect");
					menu();
				}
			}

			// Liberation des ressources et fermeture de la connexion...
			stmt.close();
			conn.close();

			System.out.println("au revoir");

			// traitement d'exception
		} catch (SQLException e) {
			System.err.println("failed");
			System.out.println("Affichage de la pile d'erreur");
			e.printStackTrace(System.err);
			System.out.println("Affichage du message d'erreur");
			System.out.println(e.getMessage());
			System.out.println("Affichage du code d'erreur");
			System.out.println(e.getErrorCode());

		}
	}
}
