package projetBD;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.chrono.IsoChronology;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Requete {
	static final String RESOURCES = "src/main/resources/";

	public void createTrigger(Statement stmt) {

		System.out.println("nom du fichier trigger.sql ?");
		String name = LectureClavier.lireChaine();
		String file = RESOURCES + name;

		String s = new String();
		StringBuffer sb = new StringBuffer();

		try {
			FileReader fr = new FileReader(new File(file));
			// be sure to not have line starting with "--" or "/*" or any other
			// non aplhabetical character

			BufferedReader br = new BufferedReader(fr);

			while ((s = br.readLine()) != null) {
				sb.append(s + " ");
			}
			br.close();
			stmt.execute(sb.toString());
			System.out.println(">>" + sb);

		} catch (Exception e) {
			System.out.println("*** Error : " + e.toString());
			System.out.println("*** ");
			System.out.println("*** Error : ");
			e.printStackTrace();
			System.out.println("################################################");
			System.out.println(sb.toString());
		}
	}

	public void executeFile(Statement stmt, String file) {
		String s = new String();
		StringBuffer sb = new StringBuffer();

		FileReader fr;
		try {
			fr = new FileReader(new File(file));

			BufferedReader br = new BufferedReader(fr);

			while ((s = br.readLine()) != null) {
				sb.append(s);
			}
			br.close();
			String[] inst = sb.toString().split(";");
			for (int i = 0; i < inst.length; i++) {
				if (!inst[i].trim().equals("")) {
					stmt.executeUpdate(inst[i]);
					System.out.println(">>" + inst[i]);
				}
			}
		} catch (Exception e) {
			System.out.println("*** Error : " + e.toString());
			System.out.println("*** ");
			System.out.println("*** Error : ");
			e.printStackTrace();
			System.out.println("################################################");
			System.out.println(sb.toString());
		}
	}

	public void createTable(Statement stmt) {
		dropTable(stmt);
		executeFile(stmt, RESOURCES + "table.sql");
	}

	public void createClient(Statement stmt) {
		System.out.println("Entrer le prénom du client");
		String prenom = LectureClavier.lireChaine();
		System.out.println("Entrer le nom du client");
		String nom = LectureClavier.lireChaine();
		System.out.println("Entrer le mail du client");
		String mail = LectureClavier.lireChaine();
		System.out.println("Entrer le mot de passe du client");
		String pw = LectureClavier.lireChaine();
		System.out.println("Entrer le adresse du client");
		String adresse = LectureClavier.lireChaine();
		String sql = "insert into Client values(IdClient.NEXTVAL,'" + prenom + "','" + nom + "','" + mail + "','" + pw
				+ "','" + adresse + "')";
		try {
			stmt.executeUpdate(sql);
			System.out.println("Client créée");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String connexionClient(Statement stmt) {
		System.out.println("Entrer votre prénom :");
		String prenom = LectureClavier.lireChaine();
		System.out.println("Entrer votre mot de passe :");
		String pw = LectureClavier.lireChaine();

		String sql = "Select IdClient FROM Client Where FirstName='" + prenom + "' AND password='" + pw + "'";
		ResultSet res;
		String retour = null;
		try {
			res = stmt.executeQuery(sql);
			if (!res.next()) {
				System.err.println("client inconnu");
				return null;
			}
			retour = res.getString(1);
			System.out.println("votre id client est " + retour);
			System.out.println("Bonjour " + prenom);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return retour;
	}

	public void createAlbum(Statement stmt, String IdClient) {
		System.out.println("Quel type voulez-vous ? ");
		System.out.println("1 : Album ");
		System.out.println("2 : Agenda ");
		System.out.println("3 : Calendrier ");
		System.out.println("4 : Livre ");
		String choice = LectureClavier.lireChaine();
		String nbPage = null, nameAlbum;
		String sql = null;
		ResultSet res;
		try {
			sql = "insert into Album values(IdAlbum.NEXTVAL,'" + IdClient + "')";
			switch (choice) {
			case "1":
				System.out.println("Combien de pages ?");
				nbPage = LectureClavier.lireChaine();
				System.out.println("Nom de l'album ?");
				nameAlbum = LectureClavier.lireChaine();
				sql = "insert into Album values(IdAlbum.NEXTVAL," + IdClient + "," + nbPage + ",'" + nameAlbum + "')";
				stmt.executeQuery(sql);
				break;
			case "2":
				System.out.println("Quel type d'agenda (52s ou 365j) ?");
				String typeAgenda = LectureClavier.lireChaine();
				System.out.println("Nom de l'agenda ?");
				nameAlbum = LectureClavier.lireChaine();
				if (typeAgenda.equals("52s")) {
					nbPage = "52";
				} else if (typeAgenda.equals("365j")) {
					nbPage = "365";
				} else {
					System.err.println("Type agenda invalide");
					break;
				}
				sql = "insert into Album values(IdAlbum.NEXTVAL," + IdClient + "," + nbPage + ",'" + nameAlbum + "')";

				stmt.executeQuery(sql);
				sql = "select IdAlbum.currval from dual";
				res = stmt.executeQuery(sql);
				res.next();
				sql = "insert into Agenda values(" + res.getString(1) + ",'" + typeAgenda + "')";
				stmt.executeQuery(sql);

				break;
			case "3":
				System.out.println("Quel type de calendrier (bureau ou mural) ?");
				String typeCalendar = LectureClavier.lireChaine();
				System.out.println("Nom du calendrier ?");
				nameAlbum = LectureClavier.lireChaine();
				nbPage = "12";
				sql = "insert into Album values(IdAlbum.NEXTVAL," + IdClient + "," + nbPage + ",'" + nameAlbum + "')";

				stmt.executeQuery(sql);
				sql = "select IdAlbum.currval from dual";
				res = stmt.executeQuery(sql);
				res.next();
				sql = "insert into Agenda values(" + res.getString(1) + ",'" + typeCalendar + "')";
				stmt.executeQuery(sql);
				break;
			case "4":
				break;
			}
		} catch (SQLException e1) {

			e1.printStackTrace();
		}

	}

	public void AddImage(Statement stmt, String IdClient) {
		System.out.println("Entrer le chemin de votre image : ");
		String path = LectureClavier.lireChaine();
		System.out.println("Ajoutez des informations à votre image : ");
		String info = LectureClavier.lireChaine();
		System.out.println("Voulez-vous partager l'image ? oui --> 1   /  non --> 0 ");
		String share = LectureClavier.lireChaine();
		String sql = "insert into Image(IdImage, IdClient, PathImage, Shared, ResolutionImage, Info) "
				+ "values(IdImage.NEXTVAL,'" + IdClient + "','" + path + "'," + share + ",16,'" + info + "')";
		try {
			stmt.executeUpdate(sql);
			System.out.println("image ajouté");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void ajouterImageDansAlbum(Statement stmt, String idClient) {
		System.out.println("Voici vos albums : ");
		getContenuTable(stmt, "Album", idClient);
		System.out.println("Entrez l'id de l'album concerné : ");
		String idAlbum = LectureClavier.lireChaine();
		System.out.println("Contenu de l'album");
		getContenuTableWithCondition(stmt, "Photo", "idAlbum=" + idAlbum);

		System.out.println("Voici les images disponibles pour vous : ");
		getContenuTableWithCondition(stmt, "Image", "shared=1 or idClient=" + idClient);
		System.out.println("Entrez l'id de l'image a ajouté");
		String idImage = LectureClavier.lireChaine();
		System.out.println("Entrez le numéro de page");
		String numPage = LectureClavier.lireChaine();
		System.out.println("Entrez un titre");
		String titre = LectureClavier.lireChaine();
		System.out.println("Entrez un commentaire");
		String com = LectureClavier.lireChaine();
		String sql = "insert into Photo values(" + numPage + "," + idAlbum + "," + idImage + ",'" + titre + "','" + com
				+ "')";

		try {
			stmt.executeQuery(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void dropTable(Statement stmt) {
		executeFile(stmt, RESOURCES + "drop.sql");
	}

	public void insertIntoTable(Statement stmt) {
		executeFile(stmt, RESOURCES + "insert.sql");
	}

	public void listerTables(Statement stmt) {

		try {
			ResultSet res = stmt.executeQuery("select table_name from user_tables");
			while (res.next()) {
				System.out.println(res.getString("table_name"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private Integer getNbColumn(Statement stmt, String nomTable) {
		Integer nbColumn = 0;
		try {
			ResultSet res = stmt.executeQuery(
					"select count(*) from user_tab_columns where table_name='" + nomTable.toUpperCase() + "'");
			res.next();
			nbColumn = res.getInt(1);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return nbColumn;
	}

	public void getContenuTable(Statement stmt, String nomTable, String idClient) {
		try {
			Integer nbColumn = getNbColumn(stmt, nomTable);

			ResultSet res = stmt.executeQuery("select * from " + nomTable + " where IdClient=" + idClient);

			ArrayList<List<String>> listeTuples = new ArrayList<>();

			while (res.next()) {
				ArrayList<String> tuple = new ArrayList<>();
				for (int i = 1; i <= nbColumn; i++) {
					tuple.add(res.getString(i));
				}
				listeTuples.add(tuple);
			}
			afficherTuples(listeTuples);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void getContenuTableWithCondition(Statement stmt, String nomTable, String condition) {
		try {
			Integer nbColumn = getNbColumn(stmt, nomTable);

			ResultSet res = stmt.executeQuery("select * from " + nomTable + " where " + condition);

			ArrayList<List<String>> listeTuples = new ArrayList<>();

			while (res.next()) {
				ArrayList<String> tuple = new ArrayList<>();
				for (int i = 1; i <= nbColumn; i++) {
					tuple.add(res.getString(i));
				}
				listeTuples.add(tuple);
			}
			afficherTuples(listeTuples);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void getContenuTableWithCondition(Statement stmt, String nomTable, String idClient, String condition) {
		try {
			Integer nbColumn = getNbColumn(stmt, nomTable);

			ResultSet res = stmt
					.executeQuery("select * from " + nomTable + " where IdClient=" + idClient + " AND " + condition);

			ArrayList<List<String>> listeTuples = new ArrayList<>();

			while (res.next()) {
				ArrayList<String> tuple = new ArrayList<>();
				for (int i = 1; i <= nbColumn; i++) {
					tuple.add(res.getString(i));
				}
				listeTuples.add(tuple);
			}
			afficherTuples(listeTuples);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void getContenuTable(Statement stmt, String nomTable) {
		try {
			Integer nbColumn = getNbColumn(stmt, nomTable);

			ResultSet res = stmt.executeQuery("select * from " + nomTable);

			ArrayList<List<String>> listeTuples = new ArrayList<>();

			while (res.next()) {
				ArrayList<String> tuple = new ArrayList<>();
				for (int i = 1; i <= nbColumn; i++) {
					tuple.add(res.getString(i));
				}
				listeTuples.add(tuple);
			}
			afficherTuples(listeTuples);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void afficherTuples(List<List<String>> listeTuples) {

		Iterator<List<String>> itListeTuples = listeTuples.iterator();
		while (itListeTuples.hasNext()) {
			List<String> tuple = itListeTuples.next();
			System.out.println(tuple);

		}
	}

	public void cleanImageAfterLogoff(Statement stmt) {

		ResultSet res;
		try {
			res = stmt.executeQuery(
					"select distinct(idImage) from image where idImage not in (select distinct(idImage) from Photo natural join image ) ");
			while (res.next()) {
				String idImage = res.getString("idImage");
				stmt.executeUpdate("delete from image where idImage=" + idImage);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void supprimerImage(Statement stmt, String idClient) {
		ResultSet res;
		System.out.println("Quel image voulez vous supprimer ?");
		getContenuTable(stmt, "Image", idClient);
		System.out.println("Entrez l'id de l'image a supprimé");
		String idImage = LectureClavier.lireChaine();
		try {
			res = stmt.executeQuery("select shared from Image where idImage=" + idImage);
			res.next();
			String shared = res.getString(1);
			if (shared.equals("1")) {
				System.out.println("1..");
				String status = "";
				res = stmt.executeQuery(
						"select status from orders where idOrder in (select idOrder from Article where idAlbum in (select idAlbum from Photo where idImage="
								+ idImage + "))");
				
				if (res.next()) {
				    do {
				    	if (res.getString("Status").equals("en cours")) {
							status = "en cours";
							}
				    } while(res.next());
				} else {
					status = "";
				}
				System.out.println("2..");
				res = stmt.executeQuery(
						"select * from Client where idClient in (select idClient from Album where idAlbum in (select idAlbum from Photo where idImage="
								+ idImage + "))");
				while (res.next()) {
					System.out.println("4..");
					String mail = res.getString("Mail");
					System.out.println("envoie d'un mail à " + mail + " : l'image " + idImage + " a été supprimé");
				}
				if (status.equals("en cours")) {
					stmt.executeUpdate("insert into TempImageForDelete values (" + idImage + ")");
				} else {
					stmt.executeUpdate("delete from image where idImage=" + idImage);
				}
				
				System.out.println("5..");
			} else {
				System.out.println("3..");
				stmt.executeUpdate("delete from image where idImage=" + idImage);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
}
