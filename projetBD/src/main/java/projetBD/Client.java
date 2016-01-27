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
	static final ReqScenario scen = new ReqScenario();

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
		System.out.println("13 : Informations d'un client");
		System.out.println("14 : Les Albums d'un Client");
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
	
	private static void infoClient() throws SQLException{
		req.infoClient(stmt, idClient);
	}
	
	private static void albumClient() throws SQLException{
		req.ListerAlbum(stmt, idClient);
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
					infoClient();
					break;
				case 14:
					albumClient();
					break;
				case 15:
					scenar1();
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
	
private static void scenar1() throws SQLException {
		
		scen.AddImage(stmt, idClient , "/Images/Martine.jpg", "0", 8, "Vancances martine");
		ResultSet res = stmt.executeQuery("select IdImage.currval from dual ");
		res.next();
		String im1=res.getString(1);
		
		scen.AddImage(stmt, idClient , "/Images/ete.jpg", "0", 10, "Vancances été");
		res = stmt.executeQuery("select IdImage.currval from dual ");
		res.next();
		String im2 = res.getString(1);
		
		scen.AddImage(stmt, idClient , "/Images/hiver.jpg", "1", 18, "Vancances hiver");
		res = stmt.executeQuery("select IdImage.currval from dual ");
		res.next();
		String im3 = res.getString(1);
		
		scen.AddImage(stmt, idClient , "/Images/gateau.jpg", "1", 18, "Dessert");
		res = stmt.executeQuery("select IdImage.currval from dual ");
		res.next();
		String im4 = res.getString(1);
		
		scen.AddImage(stmt, idClient , "/Images/entree.jpg", "1", 18, "entree");
		res = stmt.executeQuery("select IdImage.currval from dual ");
		res.next();
		String im5 = res.getString(1);
		
		scen.AddImage(stmt, idClient , "/Images/chaud.jpg", "1", 18, "plat chaud");
		res = stmt.executeQuery("select IdImage.currval from dual ");
		res.next();
		String im6 = res.getString(1);
		
		
		System.out.println("Les images ont été ajoutées");
		req.getContenuTableWithCondition(stmt, "IMAGE", "IdClient=" + idClient);
		LectureClavier.lireChaine();
		req.getContenuTableWithCondition(stmt, "Image", "idClient="+idClient);
		LectureClavier.lireChaine();
		
		
		scen.createAlbum(stmt, idClient, "Martine en vacances", "book", "154");
		res = stmt.executeQuery("select IdAlbum.currval from dual ");
		res.next();
		String idAlbum1 = res.getString(1);
		
		scen.createAlbum(stmt, idClient, "Martine fait la cuisine", "book", "154");
		res = stmt.executeQuery("select IdAlbum.currval from dual ");
		res.next();
		String idAlbum2 = res.getString(1);
		
		System.out.println("Les albums : ");
		req.getContenuTableWithCondition(stmt, "Album", "idClient="+idClient);
		LectureClavier.lireChaine();
		
		
		scen.ajouterImageDansAlbum(stmt, idClient, idAlbum1, im1, "1", "Martine ", "Les vacances");
		scen.ajouterImageDansAlbum(stmt, idClient, idAlbum1, im2, "2", "Martine ete", "Les vacances");
		scen.ajouterImageDansAlbum(stmt, idClient, idAlbum1, im3, "3", "Martine en hiver", "Les vacances");
		
		
		scen.ajouterImageDansAlbum(stmt, idClient, idAlbum2, im1, "1", "Martine ", "Les vacances");
		scen.ajouterImageDansAlbum(stmt, idClient, idAlbum2, im2, "2", "Martine ete", "Les vacances");
		scen.ajouterImageDansAlbum(stmt, idClient, idAlbum2, im3, "3", "Martine en hiver", "Les vacances");
		scen.ajouterImageDansAlbum(stmt, idClient, idAlbum2, im4, "4", "Martine repas", "Les plats desserts");
		scen.ajouterImageDansAlbum(stmt, idClient, idAlbum2, im5, "5", "Martine repas", "Les plats entrees");
		scen.ajouterImageDansAlbum(stmt, idClient, idAlbum2, im6, "6", "Martine repas", "Les plats chauds");
		
		
		
		scen.passerCommande(stmt, idClient, idAlbum1, "2", "455");
		res = stmt.executeQuery("select IdOrder.currval from dual ");
		res.next();
		String idOrder = res.getString(1);
		
		scen.passerCommande(stmt, idClient, idOrder, idAlbum2, "2", "255");
		scen.passerCommande(stmt, idClient, idOrder, idAlbum1, "3", "65");
		scen.passerCommande(stmt, idClient, idOrder, idAlbum1, "2", "255");
		scen.passerCommande(stmt, idClient, idOrder, idAlbum2, "3", "65");
		
		scen.updatePrice(stmt, idClient, idOrder);
		req.suiviCommande(stmt, idClient);
		LectureClavier.lireChaine();
		
		System.out.println("Vos codes promotionnels : ");
		req.getContenuTableWithCondition(stmt, "CodePromo", "idClient="+idClient);
		LectureClavier.lireChaine();
		
	}
	
	public void Scenario2() {
		scen.passerCommande(stmt, idClient, "2", "2", "455");
		scen.passerCommande(stmt, idClient, "2", "2", "14");
		scen.passerCommande(stmt, idClient, "2", "3", "45");
	}
}
