package projetBD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Requete {

	static final String CONN_URL = "jdbc:oracle:thin:@im2ag-oracle.e.ujf-grenoble.fr:1521:ufrima";

	static final String USER = "fauq";
	static final String PASSWD = "bd2015";
	static Connection conn;
	static Statement stmt;

	private static void menu() {

		System.out.println("*** Choisir une action a effectuer : ***");
		System.out.println("0 : Quitter");
		System.out.println("1 : Ajouter un nouvel animal");
		System.out.println("2 : Déplacer un animal de cage.");
		System.out.println("3 : Affecter un gardien à une cage");
		System.out.println("4 : Modifier l’affectation d’un gardien");
		System.out
				.println("5 : Déclarer une nouvelle maladie pour un animal (et mettre à jour son nombre de maladie)");
		System.out.println("6 : Commit");
		System.out.println("7 : Test DeadLock");
		System.out.println("8 : setIsolation");
		System.out.println("9 : Rollback");
	}

	private static void Q1() throws SQLException {
		stmt.executeQuery(
				"insert into LesAnimaux values ('Alexis', 'male', 'chien','domestique', 'Suisse', 2012, 2 , 2)");
	}

	private static void Q2() throws SQLException {
		int var = LectureClavier.lireEntier("nouvelle valeur");
		stmt.executeUpdate("update LesAnimaux set noCage=" + var + " where nomA='Alexis'");

	}

	private static void Q3() throws SQLException {
		stmt.executeQuery("insert into LesGardiens values (12, 'Spinnard')");

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

			// Enregistrement du driver Oracle
			System.out.print("Loading Oracle driver... ");
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

			while (!exit) {
				menu();
				action = LectureClavier.lireEntier("votre choix ?");
				switch (action) {
				case 0:
					exit = true;
					break;
				case 1:
					Q1();
					break;
				case 2:
					Q2();
					break;
				case 3:
					Q3();
					break;
				case 4:
					Q4();
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