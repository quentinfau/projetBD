package projetBD;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.util.Date;

public class ReqScenario {
	static final String RESOURCES = "src/main/resources/";

	static final Requete req = new Requete();

	public boolean createClient(Statement stmt, String prenom, String nom, String mail, String password,
			String adresse) {

		String sql = "insert into Client values(IdClient.NEXTVAL,'" + prenom + "','" + nom + "','" + mail + "','"
				+ password + "','" + adresse + "')";
		try {
			stmt.executeUpdate(sql);
			System.out.println("Client créée");
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public String getDate() {
		Date aujourdhui = new Date();
		DateFormat shortDateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
		return shortDateFormat.format(aujourdhui);
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

	public boolean createAlbum(Statement stmt, String IdClient, String nameAlbum, String choice, String nbPage) {

		String sql = null;
		ResultSet res;
		try {

			switch (choice) {
			case "Album":
				System.out.println("Combien de pages ?");
				nbPage = LectureClavier.lireChaine();
				System.out.println("Nom de l'album ?");
				nameAlbum = LectureClavier.lireChaine();
				sql = "insert into Album values(IdAlbum.NEXTVAL," + IdClient + "," + nbPage + ",'" + nameAlbum + "')";
				stmt.executeQuery(sql);
				break;
			case "Agenda":

				sql = "insert into Album values(IdAlbum.NEXTVAL," + IdClient + "," + 52 + ",'" + nameAlbum + "')";

				stmt.executeQuery(sql);
				sql = "select IdAlbum.currval from dual";
				res = stmt.executeQuery(sql);
				res.next();
				sql = "insert into Agenda values(" + res.getString(1) + ",'" + "52s" + "')";
				stmt.executeQuery(sql);

				break;
			case "Article":

				nbPage = "12";
				sql = "insert into Album values(IdAlbum.NEXTVAL," + IdClient + "," + nbPage + ",'" + nameAlbum + "')";

				stmt.executeQuery(sql);
				sql = "select IdAlbum.currval from dual";
				res = stmt.executeQuery(sql);
				res.next();
				sql = "insert into Calendar values(" + res.getString(1) + ",'" + "Bureau" + "')";
				stmt.executeQuery(sql);
				break;
			case "book":
				sql = "insert into Album values(IdAlbum.NEXTVAL," + IdClient + "," + nbPage + ",'" + nameAlbum + "')";

				stmt.executeQuery(sql);
				sql = "select IdAlbum.currval from dual";
				res = stmt.executeQuery(sql);
				res.next();
				sql = " insert into Book (IdAlbum, Preface, PostFace, BookTitle) values(" + res.getString(1)
						+ ", 'Preface', 'PostFace', 'Mon Livre')";
				stmt.executeQuery(sql);
				break;
			}
			return true;
		} catch (SQLException e1) {
			e1.printStackTrace();
			return false;
		}

	}

	public boolean AddImage(Statement stmt, String IdClient, String path, String share, int resolution, String info) {

		String sql = "insert into Image(IdImage, IdClient, PathImage, Shared, ResolutionImage, Info) "
				+ "values(IdImage.NEXTVAL,'" + IdClient + "','" + path + "'," + share + "," + resolution + ",'" + info
				+ "')";
		try {
			stmt.executeUpdate(sql);
			System.out.println("image ajouté");
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean ajouterImageDansAlbum(Statement stmt, String idClient, String idAlbum, String idImage,
			String numPage, String titre, String com) {
		String sql = "insert into Photo values(" + numPage + "," + idAlbum + "," + idImage + ",'" + titre + "','" + com
				+ "')";

		try {
			stmt.executeUpdate(sql);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
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

	public boolean updatePrice(Statement stmt, String idClient, String idOrder) {
		ResultSet resArticle, res;
		Statement stmt2;
		String quantity, price = "0", nbPages = "0", IdPromo = "";
		Double totalPrice = 0.0;
		boolean codePromo;
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
			System.out.println("Le prix total de votre commande s'eleve à " + totalPrice);

			codePromo = stmt.execute("Select * From CodePromo WHERE idClient=" + idClient);
			if (codePromo) {
				System.out.println("Voulez vous utiliser un code promo ? (y or n)");
				String ChoixPromo = LectureClavier.lireChaine();
				if (ChoixPromo.equalsIgnoreCase("y")) {
					req.getContenuTableWithCondition(stmt, "CodePromo", "IdClient=" + idClient);
					System.out.println("Choisissez votre code promo en tapant son ID :");
					IdPromo = LectureClavier.lireChaine();
					res = stmt.executeQuery("select Amount from CodePromo where idPromo=" + IdPromo);
					String ReducPrice = null;
					while (res.next()) {
						ReducPrice = res.getString("Amount");
					}
					Double LaPromo = Double.parseDouble(ReducPrice);
					totalPrice = totalPrice - LaPromo;
					System.out.println("Le prix total de votre commande s'eleve à " + totalPrice);
				}
			}

			System.out.println("Voulez vous validé votre commande et le paiement ? (y or n)");
			String choix = LectureClavier.lireChaine();
			if (choix.equals("y")) {
				System.out.println(
						"update Orders set totalPrice=" + totalPrice.shortValue() + " where idOrder=" + idOrder);
				stmt.executeUpdate(
						"update Orders set totalPrice=" + totalPrice.shortValue() + " where idOrder=" + idOrder);
				if (!IdPromo.isEmpty()) {
					stmt.executeUpdate("Delete from CodePromo Where IDPromo=" + IdPromo);
				}

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

	public void passerCommande(Statement stmt, String idClient, String idAlbum, String idFormat, String quantity) {
		ResultSet res;
		try {
			stmt.executeUpdate("insert into Orders values (IdOrder.NEXTVAL, TO_DATE('" + getDate()
					+ "', 'DD/MM/YY HH24:MI') , 0, " + idClient + ", 'en cours')");
			res = stmt.executeQuery("select IdOrder.currval from dual");
			res.next();
			String idOrder = res.getString(1);

			if (!idAlbum.equals("") || !idFormat.equals("") || !quantity.equals("")) {

				stmt.executeUpdate("insert into Supply values (IdSupply.NEXTVAL, TO_DATE('" + getDate()
						+ "', 'DD/MM/YY HH24:MI') , 'en cours')");
				res = stmt.executeQuery("select IdSupply.currval from dual");
				res.next();
				String idSupply = res.getString(1);
				stmt.executeUpdate("insert into Article values (IdArticle.NEXTVAL, " + idOrder + "," + idAlbum + ","
						+ idSupply + ", " + idFormat + ", " + quantity + ")");

			}

		} catch (SQLException e) {
			e.printStackTrace();

		}
	}

	public void passerCommande(Statement stmt, String idClient, String idOrder, String idAlbum, String idFormat,
			String quantity) {
		ResultSet res;
		try {

			if (!idAlbum.equals("") || !idFormat.equals("") || !quantity.equals("")) {

				stmt.executeUpdate("insert into Supply values (IdSupply.NEXTVAL, TO_DATE('" + getDate()
						+ "', 'DD/MM/YY HH24:MI') , 'en cours')");
				res = stmt.executeQuery("select IdSupply.currval from dual");
				res.next();
				String idSupply = res.getString(1);
				stmt.executeUpdate("insert into Article values (IdArticle.NEXTVAL, " + idOrder + "," + idAlbum + ","
						+ idSupply + ", " + idFormat + ", " + quantity + ")");

			}

		} catch (SQLException e) {
			e.printStackTrace();

		}
	}

	public boolean supprimerImage(Statement stmt, String idClient, String idImage) {
		ResultSet res;
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

	public boolean AddPrestataire(Statement stmt, String NamePresta, String AddressPresta, String Preference) {

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

	public void AddFormat(Statement stmt, String label, String price, String reso, String speed, String stock) {

		/*
		 * System.out.println("Entrez le nom du format"); String label =
		 * LectureClavier.lireChaine(); System.out.println(
		 * "Entrez le prix du format"); String price =
		 * LectureClavier.lireChaine(); System.out.println(
		 * "Entrez la résolution minimale du format"); String reso =
		 * LectureClavier.lireChaine(); System.out.println(
		 * "Entrez le nombre d'impression possible par jour" ); String speed =
		 * LectureClavier.lireChaine(); System.out.println(
		 * "Entrez le stock de ce format"); String stock =
		 * LectureClavier.lireChaine();
		 */

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

	public boolean DeletePrestataire(Statement stmt, String IdPresta) {

		return deleteElementTable(stmt, "Prestataire", "IdPrestataire = " + IdPresta);

	}

	public boolean DeletePhoto(Statement stmt, String IdClient, String IdAlbum, String NumPage) {

		return deleteElementTable(stmt, "Photo", "NumPage=" + NumPage);

	}

	public boolean DeleteAlbum(Statement stmt, String idClient, String IdAlbum) {

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

	public boolean DeleteClient(Statement stmt, String IdClient) {

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
