package projetBD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Application {

	static final String CONN_URL = "jdbc:oracle:thin:@im2ag-oracle.e.ujf-grenoble.fr:1521:ufrima";

	static final String USER = "fauq";
	static final String PASSWD = "bd2015";
	static final Requete req = new Requete();
	static final ReqScenario reqSc = new ReqScenario();
	static Connection conn;
	static Statement stmt;
	static final ReqScenario scen = new ReqScenario();

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
		System.out.println("8 : Commit");
		System.out.println("9 : Rollback");
		System.out.println("10 : Drop table");
		System.out.println("11 : Mettre à jour les status de commande et de livraison");
		System.out.println("12 : Modifier l'isolation");
		System.out.println("13 : Transaction format prix");
		System.out.println("14 : scenaIsolation 1");
	}

	private static void commit() throws SQLException {
		conn.commit();
	}

	private static void rollback() throws SQLException {
		conn.rollback();
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
	
	private static void Isolation1() throws SQLException{
		String cond = "label= 'A5'";
		req.getContenuTableWithCondition(stmt, "Formats", cond);
		System.out.println("Voulez vs modifier le stock(y/n): ");
		String rep = LectureClavier.lireChaine();
		if(rep.equals("y")){
			
			reqSc.UpdateStockFormats(stmt);
			System.out.println("Maj effectuee");
		}
		else
			System.out.println("NTM");
		
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
	public static void scenarioFormat() throws SQLException {
		System.out.println("Avec isolation ? (y or n)");
		String choix = LectureClavier.lireChaine();
		if (choix.equalsIgnoreCase("y")) {
			setIsolation();

		}

		ResultSet res = stmt
				.executeQuery("select IdFormat, price from Formats Where label='A4' ");
		res.next();
		String idFormat = res.getString(1);
		Double price = Double.parseDouble(res.getString(2));

		System.out.println("Prix du format A4: " + price);

		LectureClavier.lireChaine();
		scen.UpdateFormatPrice(stmt, idFormat, price + 2);

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
					if (req.createTrigger(stmt, "trg_codepromo.sql") && req.createTrigger(stmt, "trg_resolution.sql")
							&& req.createTrigger(stmt, "trg_deleteimage.sql")) {
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
					commit();
					break;
				case 9:
					rollback();
					break;
				case 10:
					if (req.dropTable(stmt)) {
						commit();
					} else {
						rollback();
					}
					break;
				case 11:
					req.mettreAJourStatusCommande(stmt);
					break;
				case 12:
					setIsolation();
					break;
				case 13:
					scenarioFormat();
					break;
				case 14:
					Isolation1();
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