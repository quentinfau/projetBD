package projetBD;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
		String sql = "insert into Client values(IdClient.NEXTVAL,'" + prenom
				+ "','" + nom + "','" + mail + "','" + pw + "','" + adresse
				+ "');";
		try {
			stmt.executeUpdate(sql);
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

		String sql = "Select IdClient FROM Client Where FirstName='" + prenom
				+ "' AND password='" + pw + "';";
		ResultSet res;
		String retour = null;
		try {
			res = stmt.executeQuery(sql);
			res.next();
			retour = res.getString(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
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
		String sql= null;
		
		sql = "insert into Album values(IdAlbum.NEXTVAL,'" + IdClient+ "');";
		switch (choice) {
		case "1":
			
			break;
		case "2":
			sql = "insert into Album values(IdAlbum.NEXTVAL,'" + IdClient+ "');";

			break;
		case "3":
			sql = "insert into Album values(IdAlbum.NEXTVAL,'" + IdClient+ "');";
			break;
		case "4":
			sql = "insert into Album values(IdAlbum.NEXTVAL,'" + IdClient+ "');";
			break;
		}
		
		String trig ="IdAlbum";
		try {
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void AddImage(Statement stmt, String IdClient) {
		System.out.println("Entrer le chemin de votre image : ");
		String path = LectureClavier.lireChaine();
		System.out.println("Ajoutez des informations à votre image : ");
		String info = LectureClavier.lireChaine();
		System.out
				.println("Voulez-vous partager l'image ? oui --> 1   /  non --> 0 ");
		String share = LectureClavier.lireChaine();
		String sql = "insert into Image(IdImage, IdClient, PathImage, Shared, ResolutionImage, Info) "
				+ "values(IdImage.NEXTVAL,'"
				+ IdClient
				+ "','"
				+ path
				+ "','"
				+ share + "',16,'" + info + "');";
		try {
			stmt.executeUpdate(sql);
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

	public void getContenuTable(Statement stmt) {
		try {
			System.out.println("Quel table voulez vous afficher ?");
			listerTables(stmt);
			String nomTable = LectureClavier.lireChaine();

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
}
