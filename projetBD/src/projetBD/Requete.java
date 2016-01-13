package projetBD;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Statement;

public class Requete {

	public void creerTable(Statement stmt) {

		String s = new String();
		StringBuffer sb = new StringBuffer();

		FileReader fr;
		try {
			fr = new FileReader(new File("table.sql"));

			// be sure to not have line starting with "--" or "/*" or any other
			// non aplhabetical character

			BufferedReader br = new BufferedReader(fr);

			while ((s = br.readLine()) != null) {
				sb.append(s);
			}
			br.close();

			// here is our splitter ! We use ";" as a delimiter for each request
			// then we are sure to have well formed statements
			String[] inst = sb.toString().split(";");

			for (int i = 0; i < inst.length; i++) {
				// we ensure that there is no spaces before or after the request
				// string
				// in order to not execute empty statements
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
}