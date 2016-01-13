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
}
