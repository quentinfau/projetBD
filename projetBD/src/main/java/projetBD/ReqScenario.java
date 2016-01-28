package projetBD;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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

	public boolean incrementerNbPages(Statement stmt, String idAlbum) {
		try {
			stmt.executeUpdate("update Album set nbPages = nbPages + 1 where idAlbum=" + idAlbum);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
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
			return incrementerNbPages(stmt, idAlbum);
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

			res = stmt.executeQuery("Select idPromo From CodePromo WHERE idClient=" + idClient);

			if (res.next()) {
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
				if (!IdPromo.equals("")) {
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

	public String getDate(int jour) {
		Date aujourdhui = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Calendar c = Calendar.getInstance();
		c.setTime(aujourdhui);
		c.add(Calendar.DATE, jour); // number of days to add
		return sdf.format(c.getTime());
	}

	public void passerCommande(Statement stmt, String idClient, String idOrder, String idAlbum, String idFormat,
			String quantity) {
		ResultSet res;
		try {
			if (idOrder.equals("")) {
				stmt.executeUpdate("insert into Orders values (IdOrder.NEXTVAL, TO_DATE('" + getDate(0)
						+ "', 'DD/MM/YYYY') , 0, " + idClient + ", 'en cours')");
				res = stmt.executeQuery("select IdOrder.currval from dual");
				res.next();
				idOrder = res.getString(1);
			}
			String idSupply;
			res = stmt.executeQuery("select nbPages from Album where idAlbum=" + idAlbum);
			res.next();
			int nbPages = res.getInt("nbPages");
			int jour = calculerProductionJournaliere(stmt, 0, idFormat, Integer.parseInt(quantity), nbPages);
			if (jour == -1) {
				System.err.println("ne peut pas etre traité car la vitesse n'est pas assez élevé");
			} else {
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
					System.out.println("avant verif stock");
					verifierStock(stmt, Integer.parseInt(quantity), idFormat, idAlbum, idSupply, idPrestataire,
							limitTime);
				}
				System.out.println("avant insert article");
				stmt.executeUpdate("insert into Article values (IdArticle.NEXTVAL, " + idOrder + "," + idAlbum + ","
						+ idSupply + ", " + idFormat + ", " + quantity + ")");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void verifierStock(Statement stmt, int quantity, String idFormat, String idAlbum, String idSupply,
			String idPrestataire, int limitTime) {
		ResultSet res;
		try {
			System.out.println("dans verif stocl");
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
