package projetBD;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class Requete {
	static final String RESOURCES = "src/main/resources/";

	public boolean createTrigger(Statement stmt) {

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
			return true;
		} catch (Exception e) {
			System.out.println("*** Error : " + e.toString());
			System.out.println("*** ");
			System.out.println("*** Error : ");
			e.printStackTrace();
			System.out.println("################################################");
			System.out.println(sb.toString());
			return false;
		}
	}

	public boolean executeFile(Statement stmt, String file) {
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
			return true;
		} catch (Exception e) {
			System.out.println("*** Error : " + e.toString());
			System.out.println("*** ");
			System.out.println("*** Error : ");
			e.printStackTrace();
			System.out.println("################################################");
			System.out.println(sb.toString());
			return false;
		}
	}

	public boolean createTable(Statement stmt) {
		dropTable(stmt);
		return executeFile(stmt, RESOURCES + "table.sql");
	}

	public boolean createClient(Statement stmt) {
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
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public String getDate(int jour) {
		Date aujourdhui = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Calendar c = Calendar.getInstance();
		c.setTime(aujourdhui);
		c.add(Calendar.DATE, jour); // number of days to add
		return sdf.format(c.getTime());
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

	public boolean createAlbum(Statement stmt, String IdClient) {
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
				System.out.println("Quel type de calendrier (Bureau ou Mural) ?");
				String typeCalendar = LectureClavier.lireChaine();
				System.out.println("Nom du calendrier ?");
				nameAlbum = LectureClavier.lireChaine();
				nbPage = "12";
				sql = "insert into Album values(IdAlbum.NEXTVAL," + IdClient + "," + nbPage + ",'" + nameAlbum + "')";

				stmt.executeQuery(sql);
				sql = "select IdAlbum.currval from dual";
				res = stmt.executeQuery(sql);
				res.next();
				sql = "insert into Calendar values(" + res.getString(1) + ",'" + typeCalendar + "')";
				stmt.executeQuery(sql);
				break;
			case "4":
				break;
			}
			return true;
		} catch (SQLException e1) {
			e1.printStackTrace();
			return false;
		}

	}

	public boolean AddImage(Statement stmt, String IdClient) {
		System.out.println("Entrer le chemin de votre image : ");
		String path = LectureClavier.lireChaine();
		System.out.println("Ajoutez des informations à votre image : ");
		String info = LectureClavier.lireChaine();
		System.out.println("Voulez-vous partager l'image ? oui --> 1   /  non --> 0 ");
		String share = LectureClavier.lireChaine();
		String sql = "insert into Image(IdImage, IdClient, PathImage, Shared, ResolutionImage, Info) "
				+ "values(IdImage.NEXTVAL,'" + IdClient + "','" + path + "'," + share + ",8,'" + info + "')";
		try {
			stmt.executeUpdate(sql);
			System.out.println("image ajouté");
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean incrementerNbPages(Statement stmt, String idAlbum) {
		try {
			stmt.executeUpdate("update Album set nbPages = nbPages + 1 where idAlbum=" + idAlbum);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean ajouterImageDansAlbum(Statement stmt, String idClient) {
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
			stmt.executeUpdate(sql);
			return incrementerNbPages(stmt, idAlbum);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean dropTable(Statement stmt) {
		return executeFile(stmt, RESOURCES + "drop.sql");
	}

	public boolean insertIntoTable(Statement stmt) {
		return executeFile(stmt, RESOURCES + "insert.sql");
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

	public boolean cleanImageAfterLogoff(Statement stmt) {
		ResultSet res;
		try {
			res = stmt.executeQuery(
					"select distinct(idImage) from image where idImage not in (select distinct(idImage) from Photo natural join image ) ");
			while (res.next()) {
				String idImage = res.getString("idImage");
				stmt.executeUpdate("delete from image where idImage=" + idImage);
			}
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

	}

	public void suiviCommande(Statement stmt1, String idClient) {
		try {
			Statement stmt2 = stmt1.getConnection().createStatement();
			System.out.println("Voici vos commandes : ");
			getContenuTable(stmt1, "Orders", idClient);
			System.out.println("De quel commande voulez vous voir le details: ");
			String idOrder = LectureClavier.lireChaine();
			ResultSet resArticle, res;

			resArticle = stmt1.executeQuery("select * from Article where idOrder=" + idOrder);
			System.out.println("cette commande concerne : ");
			while (resArticle.next()) {
				res = stmt2.executeQuery("select * from Album where idAlbum=" + resArticle.getString("idAlbum"));
				while (res.next()) {
					System.out.print("- L'album " + res.getString("nameAlbum"));
				}
				res = stmt2.executeQuery("select * from Formats where idFormat=" + resArticle.getString("idFormat"));
				while (res.next()) {
					System.out.print(" dans le format " + res.getString("label"));
				}
				res = stmt2.executeQuery("select * from Supply where idSupply=" + resArticle.getString("idSupply"));
				while (res.next()) {
					System.out.print(" --> livraison : " + res.getString("statusSup"));
				}
				System.out.println();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public boolean updatePrice(Statement stmt, String idClient, String idOrder) {
		ResultSet resArticle, res;
		Statement stmt2;
		String quantity, price = "0", nbPages = "0";
		Double totalPrice = 0.0;
		try {
			stmt2 = stmt.getConnection().createStatement();
			resArticle = stmt.executeQuery("select * from Article where idOrder=" + idOrder);
			while (resArticle.next()) {
				quantity = resArticle.getString("quantity");
				System.out.println(quantity);
				res = stmt2.executeQuery("select nbPages from Album where idAlbum=" + resArticle.getString("idAlbum"));
				while (res.next()) {
					nbPages = res.getString("nbPages");
				}
				res = stmt2
						.executeQuery("select price from Formats where idFormat=" + resArticle.getString("idFormat"));
				while (res.next()) {
					price = res.getString("price");
				}
				totalPrice = totalPrice + Double.valueOf(price) * Integer.valueOf(nbPages) * Integer.valueOf(quantity);

			}
			System.out.println("Le prix total de votre commande s'eleve à " + totalPrice
					+ " , voulez vous validé votre commande et le paiement ? (y or n)");
			String choix = LectureClavier.lireChaine();
			if (choix.equals("y")) {
				stmt.executeUpdate(
						"update Orders set totalPrice=" + totalPrice.shortValue() + " where idOrder=" + idOrder);
				System.out.println("Commande effectué");
				return true;
			} else {
				System.out.println("Commande annulée");
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean passerCommande(Statement stmt, String idClient) {
		ResultSet res;
		try {
			System.out.println(getDate(0));
			stmt.executeUpdate("insert into Orders values (IdOrder.NEXTVAL, TO_DATE('" + getDate(0)
					+ "', 'DD/MM/YYYY') , 0, " + idClient + ", 'en cours')");
			res = stmt.executeQuery("select IdOrder.currval from dual");
			res.next();
			String idOrder = res.getString(1);

			String continuer = "y";
			String idAlbum, idFormat, quantity, idSupply;
			while (continuer.equals("y")) {
				System.out.println("Voici vos albums : ");
				getContenuTable(stmt, "Album", idClient);
				System.out.println("Entrez l'id de l'album choisi : ");
				idAlbum = LectureClavier.lireChaine();
				System.out.println("Format disponible");
				getContenuTable(stmt, "Formats");
				System.out.println("Entrez l'id du format voulu");
				idFormat = LectureClavier.lireChaine();
				System.out.println("Quel quantité ?");
				quantity = LectureClavier.lireChaine();
				if (!idAlbum.equals("") || !idFormat.equals("") || !quantity.equals("")) {
					res = stmt.executeQuery("select nbPages from Album where idAlbum=" + idAlbum);
					res.next();
					int nbPages = res.getInt("nbPages");
					int jour = calculerProductionJournaliere(stmt, 0, idFormat, Integer.parseInt(quantity), nbPages);
					if (jour == -1) {
						System.err.println("ne peut pas etre traité car la vitesse n'est pas assez élevé");
					}
					System.out.println(getDate(jour));
					res = stmt.executeQuery(
							"select idPrestataire from prestataire where preference = (select Max(preference) from Prestataire)");
					res.next();
					String idPrestataire = res.getString("idPrestataire");
					res = stmt.executeQuery("select limitTime from Contact where idPrestataire=" + idPrestataire
							+ " AND idFormat=" + idFormat);
					res.next();
					int limitTime = res.getInt("limitTime");
					if (limitTime < jour) {
						stmt.executeUpdate(
								"insert into Supply (IdSupply, IdPrestataire, DateSup, StatusSup) values (IdSupply.NEXTVAL,'"
										+ idPrestataire + "', TO_DATE('" + getDate(0) + "+ " + limitTime
										+ "', 'DD/MM/YYYY') , 'en cours')");
						res = stmt.executeQuery("select IdSupply.currval from dual");
						res.next();
						idSupply = res.getString(1);
					} else {
						System.out.println("jour : " + jour + "date : " + getDate(jour));
						stmt.executeUpdate(
								"insert into Supply (IdSupply, DateSup, StatusSup) values (IdSupply.NEXTVAL, TO_DATE('"
										+ getDate(jour) + "', 'DD/MM/YYYY') , 'en cours')");
						res = stmt.executeQuery("select IdSupply.currval from dual");
						res.next();
						idSupply = res.getString(1);
						verifierStock(stmt, Integer.parseInt(quantity), idFormat, idAlbum, idSupply, idPrestataire,
								limitTime);
					}

					stmt.executeUpdate("insert into Article values (IdArticle.NEXTVAL, " + idOrder + "," + idAlbum + ","
							+ idSupply + ", " + idFormat + ", " + quantity + ")");

				} else {
					return false;
				}
				System.out.println("Ajouter d'autres articles ? (y or n)");
				continuer = LectureClavier.lireChaine();
			}
			return updatePrice(stmt, idClient, idOrder);

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	private void verifierStock(Statement stmt, int quantity, String idFormat, String idAlbum, String idSupply,
			String idPrestataire, int limitTime) {
		ResultSet res;
		try {
			res = stmt.executeQuery("select nbPages from Album where idAlbum=" + idAlbum);
			res.next();
			int nbPages = res.getInt("nbPages");
			res = stmt.executeQuery("select stock from Formats where idFormat=" + idFormat);
			res.next();
			int stock = res.getInt("stock");
			int total = quantity * nbPages;
			if (stock < total) {
				stmt.executeUpdate("update Supply set idPrestataire =" + idPrestataire + " where idSupply=" + idSupply);
				stmt.executeUpdate("update Supply set DateSup = (TO_DATE('" + getDate(0) + "', 'DD/MM/YY HH24:MI') + "
						+ limitTime + ") where idSupply=" + idSupply);
			} else {
				stmt.executeUpdate(
						"update Formats set Stock = (Stock - " + total + ") where Formats.IdFormat=" + idFormat);
				stmt.executeUpdate("update Supply set StatusSup= 'envoye' where idSupply=" + idSupply);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public int calculerProductionJournaliere(Statement stmt, int jour, String idFormat, int quantiteArticle,
			int nbPagesArticle) {
		try {
			System.out.println("JOUR : " + jour);
			ResultSet res, resArticle;
			Integer productionJour = quantiteArticle * nbPagesArticle, vitesse;
			Statement stmt2 = stmt.getConnection().createStatement();
			res = stmt.executeQuery("select speed from Formats where idFormat=" + idFormat);
			res.next();
			vitesse = res.getInt("speed");
			if (productionJour > vitesse) {
				return -1;
			}
			resArticle = stmt.executeQuery("select idArticle from Article where idFormat=" + idFormat
					+ " AND idSupply IN (select idSupply from Supply where idPrestataire is null AND dateSup=TO_DATE('"
					+ getDate(jour) + "', 'DD/MM/YYYY'))");
			while (resArticle.next()) {
				String idArticle = resArticle.getString("idArticle");
				res = stmt2.executeQuery("select quantity from Article where idArticle=" + idArticle);
				res.next();
				int quantity = res.getInt("quantity");
				res = stmt2.executeQuery(
						"select nbPages from Album where idAlbum = (select idAlbum from Article where idArticle="
								+ idArticle + ")");
				res.next();
				int nbPages = res.getInt("nbPages");
				System.out.println("quantite : " + quantity);
				System.out.println("nbPage : " + nbPages);
				productionJour = productionJour + (quantity * nbPages);

			}
			System.out.println("prod jour : " + productionJour);
			if (productionJour < vitesse) {
				return jour;
			} else {
				return calculerProductionJournaliere(stmt, jour + 1, idFormat, quantiteArticle, nbPagesArticle);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}

	public boolean supprimerImage(Statement stmt, String idClient) {
		ResultSet res;
		System.out.println("Quel image voulez vous supprimer ?");
		getContenuTableWithCondition(stmt, "Image", idClient,
				"idImage not in (select idImage from TempImageForDelete)");
		System.out.println("Entrez l'id de l'image a supprimé");
		String idImage = LectureClavier.lireChaine();
		try {
			res = stmt.executeQuery("select shared from Image where idImage=" + idImage);
			res.next();
			String shared = res.getString(1);
			if (shared.equals("1")) {
				String status = "";
				res = stmt.executeQuery(
						"select status from orders where idOrder in (select idOrder from Article where idAlbum in (select idAlbum from Photo where idImage="
								+ idImage + "))");

				if (res.next()) {
					do {
						if (res.getString("Status").equals("en cours")) {
							status = "en cours";
						}
					} while (res.next());
				} else {
					status = "";
				}
				res = stmt.executeQuery(
						"select * from Client where idClient in (select idClient from Album where idAlbum in (select idAlbum from Photo where idImage="
								+ idImage + "))");
				while (res.next()) {
					String mail = res.getString("Mail");
					System.out.println("envoie d'un mail à " + mail + " : l'image " + idImage + " a été supprimé");
				}
				if (status.equals("en cours")) {
					stmt.executeUpdate("insert into TempImageForDelete values (" + idImage + ")");
					System.out.println("L'image est présente dans une commande en cours, suppression mise en attente");
				} else {
					stmt.executeUpdate("delete from image where idImage=" + idImage);
					System.out.println("L'image a été supprimé");

				}
			} else {
				stmt.executeUpdate("delete from image where idImage=" + idImage);

			}
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/* Fonctions de Consultation */
	public void ListerAlbum(Statement stmt, String idClient) {
		try {
			System.out.println("Voici vos Albums : ");
			getContenuTable(stmt, "Album", idClient);
			ResultSet res;
			Statement stmt2 = stmt.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * from Album " + "WHERE IdClient = '" + idClient + "' ");
			while (rs.next()) {
				System.out.println("- L'album " + rs.getString("nameAlbum") + " NbPages " + rs.getString("NbPages"));
				res = stmt2.executeQuery(
						"select * from Photo natural join Image where idAlbum=" + rs.getString("idAlbum"));
				while (res.next()) {
					System.out.println(" _ Photo: " + res.getString("Title") + "  _Numero Page: "
							+ res.getString("NumPage") + "  Partage: " + TranslateShare(res.getString("shared")));
				}
				res.close();
			}
			rs.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void infoClient(Statement stmt1, String idClient) {
		try {
			Statement stmt2 = stmt1.getConnection().createStatement();
			System.out.println("Voici vos commandes : ");
			getContenuTable(stmt1, "Client", idClient);
			ResultSet resClient, res;
			resClient = stmt1.executeQuery("select * from Client where idClient=" + idClient);
			System.out.println("Informations du Client ");
			while (resClient.next()) {
				System.out.println("FirstName : " + resClient.getString("FirstName"));
				System.out.println("LastName : " + resClient.getString("LastName"));
				System.out.println("Mail : " + resClient.getString("Mail"));
				System.out.println("Address : " + resClient.getString("Address"));
				res = stmt2.executeQuery("select * from CodePromo where idClient=" + idClient);
				System.out.println("Liste des Codes promo:");
				while (res.next()) {
					System.out.println("numero Code: " + res.getString("IdPromo") + " ||  " + "Amount: "
							+ res.getString("Amount"));
					System.out.println();
				}
				System.out.println();
				res.close();
				stmt2.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private String TranslateShare(String s) {
		String n = "";
		if (s.equals("1"))
			n = "Oui";
		else
			n = "Non";
		return n;
	}

	public boolean AddPrestataire(Statement stmt) {

		System.out.println("Entrez le nom du prestataire");
		String NamePresta = LectureClavier.lireChaine();
		System.out.println("Entrez l'adresse du prestataire");
		String AddressPresta = LectureClavier.lireChaine();
		System.out.println("Entrez le numéro de préférence pour ce prestataire");
		String Preference = LectureClavier.lireChaine();

		try {
			stmt.executeUpdate(
					"insert into Prestataire (IdPrestataire, NamePresta, AddressPresta, Preference) values(IdPrestataire.NEXTVAL,'"
							+ NamePresta + "','" + AddressPresta + "'," + Preference + ")");
			System.out.println("Prestataire créé");
			return true;
		} catch (SQLException e) {
			System.out.println("Erreur lors de l'insertion : ");
			e.printStackTrace();
			return false;
		}

	}

	public void AddFormat(Statement stmt) {

		System.out.println("Entrez le nom du format");
		String label = LectureClavier.lireChaine();
		System.out.println("Entrez le prix du format");
		String price = LectureClavier.lireChaine();
		System.out.println("Entrez la résolution minimale du format");
		String reso = LectureClavier.lireChaine();
		System.out.println("Entrez le nombre d'impression possible par jour");
		String speed = LectureClavier.lireChaine();
		System.out.println("Entrez le stock de ce format");
		String stock = LectureClavier.lireChaine();

		try {
			stmt.executeUpdate(
					"insert into Formats (IdFormat, Label, Price, ResolutionMin, Speed, Stock) values(IdFormat.NEXTVAL,'"
							+ label + "'," + price + "," + reso + "," + speed + "," + stock + ")");
			System.out.println("Format créé");
		} catch (SQLException e) {
			System.out.println("Erreur lors de l'insertion : ");
			e.printStackTrace();
		}
	}

	public void UpdateFormat(Statement stmt) {

		try {
			stmt.executeUpdate("Update Formats SET stock=1000 Where IdFormat = 2");
			System.out.println("Stock modifié");
		} catch (SQLException e) {
			System.out.println("Erreur lors de la modification : ");
			e.printStackTrace();
		}
	}

	public boolean DeletePrestataire(Statement stmt) {

		System.out.println("Voici la liste des prestataires");
		getContenuTable(stmt, "Prestataire");
		System.out.println("Entrez l'id du prestataire à supprimer");
		String IdPresta = LectureClavier.lireChaine();
		return deleteElementTable(stmt, "Prestataire", "IdPrestataire = " + IdPresta);

	}

	public boolean DeletePhoto(Statement stmt, String IdClient) {
		System.out.println("Sélectionner l'album, entrez son ID ");
		getContenuTableWithCondition(stmt, "Album", "IdClient = " + IdClient);
		String IdAlbum = LectureClavier.lireChaine();
		System.out.println("Voici les photos présentes dans votre album :");
		getContenuTableWithCondition(stmt, "Photo", "IdAlbum=" + IdAlbum);
		System.out.println("Entrez le numéro de page de la photo que vous voulez supprimer de l'album ");
		String NumPage = LectureClavier.lireChaine();
		// DECREMENTATION NB PAGES
		return deleteElementTable(stmt, "Photo", "NumPage=" + NumPage);

	}

	public boolean DeleteAlbum(Statement stmt, String idClient) {
		System.out.println("Voici la liste de vos albums  : " + idClient);
		getContenuTable(stmt, "Album", idClient);
		System.out.println("Entrez l'id de l'album que vous voulez supprimer");
		String IdAlbum = LectureClavier.lireChaine();
		ResultSet res = null;
		try {
			deleteElementTable(stmt, "Photo", "IdAlbum=" + IdAlbum);
			deleteElementTable(stmt, "Article", "IdAlbum=" + IdAlbum);
			res = stmt.executeQuery("select IdAlbum FROM CALENDAR where IdAlbum=" + IdAlbum);
			if (!res.next()) {
				res = stmt.executeQuery("select IdAlbum FROM Agenda where IdAlbum=" + IdAlbum);
				if (!res.next()) {
					res = stmt.executeQuery("select IdAlbum FROM Book where IdAlbum=" + IdAlbum);
					if (res.next()) {

						deleteElementTable(stmt, "Book", "IdAlbum=" + IdAlbum);
					}
				} else {
					deleteElementTable(stmt, "Agenda", "IdAlbum=" + IdAlbum);
				}
			} else {
				deleteElementTable(stmt, "Calendar", "IdAlbum=" + IdAlbum);
			}
			deleteElementTable(stmt, "Album", "IdAlbum=" + IdAlbum);
			return true;
		} catch (SQLException e) {

			e.printStackTrace();
			return false;
		}

	}

	public boolean deleteElementTable(Statement stmt, String nomTable, String condition) {
		try {
			stmt.executeUpdate("DELETE FROM " + nomTable + " Where " + condition);
			System.out.println("Element supprimé");
			return true;
		} catch (SQLException e) {
			System.out.println("Erreur lors de la suppression");
			e.printStackTrace();
			return false;
		}

	}

	public boolean DeleteClient(Statement stmt) {
		System.out.println("Voici la liste des clients");
		getContenuTable(stmt, "Client");
		System.out.println("Entrez l'id du client à supprimer");
		String IdClient = LectureClavier.lireChaine();

		try {
			stmt.executeUpdate("insert into TempClientForDelete values (" + IdClient + ")");

			stmt.executeUpdate("UPDATE Image SET Shared=0 where IdClient=" + IdClient);
			return true;
			// ********Modife a faire pour image*******//
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

	}
}
