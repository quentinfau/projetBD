package projetBD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Application {

	static final String CONN_URL = "jdbc:oracle:thin:@im2ag-oracle.e.ujf-grenoble.fr:1521:ufrima";

	static final String USER = "fauq";
	static final String PASSWD = "bd2015";
	static final Requete req = new Requete();
	static Connection conn;
	static Statement stmt;

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

	private static void menu() {

		System.out.println("*** Choisir une action a effectuer : ***");
		System.out.println("0 : Quitter");
		System.out.println("1 : Creer table");
		System.out.println("2 : Remplir table");
		System.out.println("3 : Afficher table");
		System.out.println("4 : Creer Trigger");
		System.out.println("5 : Ajouter un prestataire");
		System.out.println("6 : Supprimer un prestataire");
		System.out.println("7 : Supprimer un client");
		System.out.println("8 : Supprimer une Image");
		System.out.println("9 : Commit");
		System.out.println("10 : Rollback");
		System.out.println("11 : Drop table");
		System.out.println("12 : Test");
		
	}

	private static void test() throws SQLException {
		stmt.executeQuery("drop table test");
		// req.executeFile(stmt, "src/main/resources/test.sql");

		// stmt.executeQuery("insert into test values (12, 'Spinnard')");
		// stmt.executeQuery("insert into test values (13, 'Spinnardo')");
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

	private static void closeConnection(Statement stmt) {

		try {
			if (req.cleanImageAfterLogoff(stmt)) {
				commit();
			} else {
				rollback();
			}
			stmt.close();

			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private static void supprimerImage() throws SQLException {
		System.out.println("Entrez l'id du Client");
		String idClient = LectureClavier.lireChaine();
		if (req.supprimerImage(stmt, idClient)) {
			commit();
		} else {
			rollback();
		}
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
					if (req.createTable(stmt)) {
						commit();
					} else {
						rollback();
					}
					break;
				case 2:
					if (req.insertIntoTable(stmt)) {
						commit();
					} else {
						rollback();
					}
					break;
				case 3:
					System.out.println("Quel table voulez vous afficher ?");
					req.listerTables(stmt);
					String nomTable = LectureClavier.lireChaine();
					req.getContenuTable(stmt, nomTable);
					break;
				case 4:
					if (req.createTrigger(stmt)) {
						commit();
					} else {
						rollback();
					}
					break;
				case 5:
					if (req.AddPrestataire(stmt)) {
						commit();
					} else {
						rollback();
					}
					break;
				case 6:
					if (req.DeletePrestataire(stmt)) {
						commit();
					} else {
						rollback();
					}
					break;
				case 7:
					if (req.DeleteClient(stmt)) {
						commit();
					} else {
						rollback();
					}
					break;
				case 8:
					supprimerImage();
					break;
				case 9:
					commit();
					break;
				case 10:
					rollback();
					break;
				case 11:
					if (req.dropTable(stmt)) {
						commit();
					} else {
						rollback();
					}
					break;
				case 12:
					test();
					break;
				default:
					System.out.println("=> choix incorrect");
					menu();
				}
			}

			closeConnection(stmt);

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