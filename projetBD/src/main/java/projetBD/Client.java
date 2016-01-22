package projetBD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Client {
	static final String CONN_URL = "jdbc:oracle:thin:@im2ag-oracle.e.ujf-grenoble.fr:1521:ufrima";

	static final String USER = "fauq";
	static final String PASSWD = "bd2015";
	static final Requete req = new Requete();

	static String idClient = null;

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
		System.out.println("5 : Ajouter des images dans un album");
		System.out.println("6 : Commit");
		System.out.println("7 : Supprimer une image");
		System.out.println("8 : Passer une commande");
		System.out.println("9 : Suivre une commande");
		System.out.println("10 : Supprimer album");
		System.out.println("11 : Supprimer une photo");
		System.out.println("12 : Drop table");
	}

	private static void menuConnexion() {

		System.out.println("*** Choisir une action a effectuer : ***");
		System.out.println("0 : Quitter");
		System.out.println("1 : S'inscrire");
		System.out.println("2 : Se connecter");
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

	private static void creerClient() throws SQLException {
		if (req.createClient(stmt)) {
			commit();
		} else {
			rollback();
		}
		connexionClient();
	}

	private static void connexionClient() throws SQLException {
		idClient = req.connexionClient(stmt);

	}

	private static void creerAlbum() throws SQLException {
		if (req.createAlbum(stmt, idClient)) {
			commit();
		} else {
			rollback();
		}
	}

	private static void AddImage() throws SQLException {
		if (req.AddImage(stmt, idClient)) {
			commit();
		} else {
			rollback();
		}
	}

	private static void dropTable() throws SQLException {
		if (req.dropTable(stmt)) {
			commit();
		} else {
			rollback();
		}

	}

	private static void AjouterImageDansAlbum() throws SQLException {
		if (req.ajouterImageDansAlbum(stmt, idClient)) {
			commit();
		} else {
			rollback();
		}
	}

	private static void commit() throws SQLException {
		conn.commit();
	}

	private static void suiviCommande() {
		req.suiviCommande(stmt, idClient);
	}

	private static void rollback() throws SQLException {
		conn.rollback();
	}

	private static void supprimerImage() throws SQLException {
		if (req.supprimerImage(stmt, idClient)) {
			commit();
		} else {
			rollback();
		}
	}

	private static void passerCommande() throws SQLException {
		if (req.passerCommande(stmt, idClient)) {
			commit();
		} else {
			rollback();
		}
	}

	private static void supprimerAlbum() throws SQLException {
		if (req.DeleteAlbum(stmt, idClient)) {
			commit();
		} else {
			rollback();
		}
	}

	private static void supprimerPhoto() throws SQLException {
		if (req.DeletePhoto(stmt, idClient)) {
			commit();
		} else {
			rollback();
		}
	}

	private static void setIsolation() throws SQLException {
		conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
	}

	public static void main(String args[]) {

		try {

			int action;
			boolean exit = false;

			connexion();

			menuConnexion();
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
			default:
				System.out.println("=> choix incorrect");
				menu();
			}
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
					AjouterImageDansAlbum();
					break;
				case 6:
					commit();
					break;
				case 7:
					supprimerImage();
					break;
				case 8:
					passerCommande();
					break;
				case 9:
					suiviCommande();
					break;
				case 10:
					supprimerAlbum();
					break;
				case 11:
					supprimerPhoto();
					break;

				case 12:
					dropTable();
					break;
				case 13:
					ResultSet res = stmt.executeQuery(
							"Select Album.IdAlbum FROM Album LEFT join Calendar on Album.IdAlbum=Calendar.IdAlbum LEFT join Agenda on Agenda.IdAlbum=Album.IdAlbum LEFT join Book on Book.IdAlbum=Album.IdAlbum Where Calendar.IdAlbum is NULL AND Book.IdAlbum is NULL AND Agenda.IdAlbum is NULL");
					res.next();
					System.out.println(res.getString(1));
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
