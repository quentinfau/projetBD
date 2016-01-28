package projetBD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Client {
	static final String CONN_URL = "jdbc:oracle:thin:@im2ag-oracle.e.ujf-grenoble.fr:1521:ufrima";

	static final String USER = "salema";
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
		System.out.println("15 : Scenario Passer Commande");
		System.out.println("16 : Scenario Delete Image");
		System.out.println("17 : Scénario Résolution Format");
		System.out.println("20 : Scénario Supression image partagé");
		System.out.println("30 : Scenario Passer Commande 2");
		System.out.println("31 : Modifier résolution image");
		System.out.println("40 : Scénario isolation Format prix");
		System.out.println("50 : Mettre à jour statut commandes");
		System.out.println("99 : roolback");
		System.out.println("100 : Afficher une table");
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

	private static void albumClient() throws SQLException {
		req.ListerAlbum(stmt, idClient);
	}

	private static void infoClient() throws SQLException {
		req.infoClient(stmt, idClient);
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

	private static void ModifierResoImage() throws SQLException {
		req.UpdateResoImage(stmt, idClient);

	}

	private static void setIsolation() throws SQLException {

		int action2;
		System.out.println("0 -> TRANSACTION_READ_COMMITTED");
		System.out.println("1 -> TRANSACTION_SERIALIZABLE");
		action2 = LectureClavier.lireEntier("votre choix de transactions ?");
		switch (action2) {
		case 0:
			conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			break;
		case 1:
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			break;
		default:
			System.out.println("=> choix incorrect");
		}
	}

	private static void scenarCommande() throws SQLException {

		scen.AddImage(stmt, idClient, "/Images/Martine.jpg", "0", 10, "Vancances martine");
		ResultSet res = stmt.executeQuery("select IdImage.currval from dual ");
		res.next();
		String im1 = res.getString(1);

		scen.AddImage(stmt, idClient, "/Images/ete.jpg", "0", 10, "Vancances été");
		res = stmt.executeQuery("select IdImage.currval from dual ");
		res.next();
		String im2 = res.getString(1);

		scen.AddImage(stmt, idClient, "/Images/hiver.jpg", "1", 18, "Vancances hiver");
		res = stmt.executeQuery("select IdImage.currval from dual ");
		res.next();
		String im3 = res.getString(1);

		scen.AddImage(stmt, idClient, "/Images/gateau.jpg", "1", 18, "Dessert");
		res = stmt.executeQuery("select IdImage.currval from dual ");
		res.next();
		String im4 = res.getString(1);

		scen.AddImage(stmt, idClient, "/Images/entree.jpg", "1", 18, "entree");
		res = stmt.executeQuery("select IdImage.currval from dual ");
		res.next();
		String im5 = res.getString(1);

		scen.AddImage(stmt, idClient, "/Images/chaud.jpg", "1", 18, "plat chaud");
		res = stmt.executeQuery("select IdImage.currval from dual ");
		res.next();
		String im6 = res.getString(1);

		System.out.println("Les images ont été ajoutées");
		req.getContenuTableWithCondition(stmt, "IMAGE", "IdClient=" + idClient);
		LectureClavier.lireChaine();

		scen.createAlbum(stmt, idClient, "Martine en vacances", "book", "0");
		res = stmt.executeQuery("select IdAlbum.currval from dual ");
		res.next();
		String idAlbum1 = res.getString(1);

		scen.createAlbum(stmt, idClient, "Martine fait la cuisine", "book", "0");
		res = stmt.executeQuery("select IdAlbum.currval from dual ");
		res.next();
		String idAlbum2 = res.getString(1);

		System.out.println("Les albums : ");
		req.getContenuTableWithCondition(stmt, "Album", "idClient=" + idClient);
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

		scen.passerCommande(stmt, idClient, "", idAlbum1, "2", "14");
		res = stmt.executeQuery("select IdOrder.currval from dual ");
		res.next();
		String idOrder = res.getString(1);
		scen.passerCommande(stmt, idClient, idOrder, idAlbum2, "2", "12");
		scen.passerCommande(stmt, idClient, idOrder, idAlbum1, "3", "65");
		scen.passerCommande(stmt, idClient, idOrder, idAlbum1, "2", "41");
		scen.passerCommande(stmt, idClient, idOrder, idAlbum2, "3", "65");

		scen.updatePrice(stmt, idClient, idOrder);
		req.mettreAJourStatusCommande(stmt);

		commit();

		req.suiviCommande(stmt, idClient);
		LectureClavier.lireChaine();

		System.out.println("Vos codes promotionnels : ");
		req.getContenuTableWithCondition(stmt, "CodePromo", "idClient=" + idClient);
		LectureClavier.lireChaine();

	}

	private static void scenarDeleteImage() throws SQLException {

		scen.AddImage(stmt, idClient, "/Images/MartineFoot.jpg", "0", 3, "foot");
		ResultSet res = stmt.executeQuery("select IdImage.currval from dual ");
		res.next();
		String im1 = res.getString(1);

		scen.AddImage(stmt, idClient, "/Images/bad.jpg", "0", 4, "bad");
		res = stmt.executeQuery("select IdImage.currval from dual ");
		res.next();
		String im2 = res.getString(1);

		scen.AddImage(stmt, idClient, "/Images/danse.jpg", "1", 18, "danse");
		res = stmt.executeQuery("select IdImage.currval from dual ");
		res.next();
		String im3 = res.getString(1);

		scen.AddImage(stmt, idClient, "/Images/nonUtilise.jpg", "1", 2, "Non utilisée");

		System.out.println("Les images ont été ajoutées");
		req.getContenuTableWithCondition(stmt, "IMAGE", "IdClient=" + idClient);
		LectureClavier.lireChaine();

		scen.createAlbum(stmt, idClient, "Martine au sport", "Album", "0");
		res = stmt.executeQuery("select IdAlbum.currval from dual ");
		res.next();
		String idAlbum1 = res.getString(1);

		scen.ajouterImageDansAlbum(stmt, idClient, idAlbum1, im1, "1", "Martine foot ", "Foot");
		scen.ajouterImageDansAlbum(stmt, idClient, idAlbum1, im2, "2", "Martine bad", "bad");
		scen.ajouterImageDansAlbum(stmt, idClient, idAlbum1, im3, "3", "Martine danse", "La danse");

		System.out.println("Ajout de 3 photos dans l'album Sport. Listes des albums : ");
		req.getContenuTableWithCondition(stmt, "Album", "idClient=" + idClient);
		LectureClavier.lireChaine();

		commit();

	}

	private static void scenarCommande2() throws SQLException {

		scen.AddImage(stmt, idClient, "/Images/Martine.jpg", "0", 10, "Vancances martine");
		ResultSet res = stmt.executeQuery("select IdImage.currval from dual ");
		res.next();
		String im1 = res.getString(1);

		scen.AddImage(stmt, idClient, "/Images/ete.jpg", "0", 10, "Vancances été");
		res = stmt.executeQuery("select IdImage.currval from dual ");
		res.next();
		String im2 = res.getString(1);

		scen.AddImage(stmt, idClient, "/Images/hiver.jpg", "1", 18, "Vancances hiver");
		res = stmt.executeQuery("select IdImage.currval from dual ");
		res.next();
		String im3 = res.getString(1);

		scen.AddImage(stmt, idClient, "/Images/gateau.jpg", "1", 18, "Dessert");
		res = stmt.executeQuery("select IdImage.currval from dual ");
		res.next();
		String im4 = res.getString(1);

		scen.AddImage(stmt, idClient, "/Images/entree.jpg", "1", 18, "entree");
		res = stmt.executeQuery("select IdImage.currval from dual ");
		res.next();
		String im5 = res.getString(1);

		scen.AddImage(stmt, idClient, "/Images/chaud.jpg", "1", 18, "plat chaud");
		res = stmt.executeQuery("select IdImage.currval from dual ");
		res.next();
		String im6 = res.getString(1);

		System.out.println("Les images ont été ajoutées");
		req.getContenuTableWithCondition(stmt, "IMAGE", "IdClient=" + idClient);
		LectureClavier.lireChaine();

		scen.createAlbum(stmt, idClient, "Martine en vacances", "book", "0");
		res = stmt.executeQuery("select IdAlbum.currval from dual ");
		res.next();
		String idAlbum1 = res.getString(1);

		scen.createAlbum(stmt, idClient, "Martine fait la cuisine", "book", "0");
		res = stmt.executeQuery("select IdAlbum.currval from dual ");
		res.next();
		String idAlbum2 = res.getString(1);

		System.out.println("Les albums : ");
		req.getContenuTableWithCondition(stmt, "Album", "idClient=" + idClient);
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

		scen.passerCommande(stmt, idClient, "", idAlbum1, "2", "14");
		res = stmt.executeQuery("select IdOrder.currval from dual ");
		res.next();
		String idOrder = res.getString(1);
		scen.passerCommande(stmt, idClient, idOrder, idAlbum2, "4", "12");
		scen.passerCommande(stmt, idClient, idOrder, idAlbum1, "4", "65");
		scen.passerCommande(stmt, idClient, idOrder, idAlbum1, "4", "41");
		scen.passerCommande(stmt, idClient, idOrder, idAlbum2, "3", "65");

		scen.updatePrice(stmt, idClient, idOrder);

		scen.passerCommande(stmt, idClient, "", idAlbum1, "4", "14");
		res = stmt.executeQuery("select IdOrder.currval from dual ");
		res.next();
		String idOrder2 = res.getString(1);
		scen.passerCommande(stmt, idClient, idOrder2, idAlbum2, "4", "12");
		scen.passerCommande(stmt, idClient, idOrder2, idAlbum1, "4", "65");
		scen.passerCommande(stmt, idClient, idOrder2, idAlbum1, "4", "41");
		scen.passerCommande(stmt, idClient, idOrder2, idAlbum2, "4", "65");

		scen.updatePrice(stmt, idClient, idOrder2);

		req.mettreAJourStatusCommande(stmt);

		commit();
		req.suiviCommande(stmt, idClient);
		LectureClavier.lireChaine();

		System.out.println("Vos codes promotionnels : ");
		req.getContenuTableWithCondition(stmt, "CodePromo", "idClient=" + idClient);
		LectureClavier.lireChaine();

	}

	public static void scenarFormatPrice() throws SQLException {
		System.out.println("Avec isolation ? (y or n)");
		String choix = LectureClavier.lireChaine();

		if (choix.equalsIgnoreCase("y")) {
			setIsolation();
		}
		ResultSet res = stmt.executeQuery("select price from Formats Where label='A4' ");
		res.next();

		Double price = Double.parseDouble(res.getString(1));

		res = stmt.executeQuery("select IdOrder from Orders Where IdOrder=2 ");
		res.next();
		String IdOrder = res.getString(1);

		res = stmt.executeQuery("select Quantity from Article Where IdOrder= " + IdOrder);
		res.next();
		Double quantity = Double.parseDouble(res.getString(1));
		System.out.println("Le prix du format : " + price + "   Le prix de l'article : " + price * quantity);

	}

	private static void scenarResolution() throws SQLException {
		ResultSet res = stmt.executeQuery("select idAlbum from Album where nameAlbum='Martine au sport'");
		res.next();
		String idAlbum1 = res.getString(1);
		req.getContenuTableWithCondition(stmt, "Album", "IdAlbum=" + idAlbum1);

		scen.passerCommande(stmt, idClient, "", idAlbum1, "4", "2");
	}

	private static void scenarSupressionImagePartage() throws SQLException {
		ResultSet res;
		String idClient = "2";
		scen.createAlbum(stmt, "3", "Martine au sport", "Album", "0");
		res = stmt.executeQuery("select IdAlbum.currval from dual ");
		res.next();
		String idAlbum1 = res.getString(1);

		scen.AddImage(stmt, idClient, "/Images/TestPourPartage.jpg", "1", 10, "foot");
		res = stmt.executeQuery("select IdImage.currval from dual ");
		res.next();
		String im1 = res.getString(1);

		scen.ajouterImageDansAlbum(stmt, "3", idAlbum1, im1, "10", "Test Partage ", "Foot");

		scen.supprimerImage(stmt, idClient, im1);

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
					scenarCommande();
					break;
				case 16:
					scenarDeleteImage();
					break;
				case 17:
					scenarResolution();
					break;
				case 20:
					scenarSupressionImagePartage();
					break;
				case 30:
					scenarCommande2();
					break;
				case 31:
					ModifierResoImage();
					break;
				case 40:
					scenarFormatPrice();
					break;
				case 50:
					req.mettreAJourStatusCommande(stmt);
					break;
				case 99:
					rollback();
					break;
				case 100:
					System.out.println("Quel table voulez vous afficher ?");
					req.listerTables(stmt);
					String nomTable = LectureClavier.lireChaine();
					req.getContenuTable(stmt, nomTable);
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
