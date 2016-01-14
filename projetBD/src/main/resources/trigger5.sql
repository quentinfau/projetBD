--Lorsqu’une commande est effectué, et que le format sélectionné ne contient plus de stock (stock = 0 plus de papier) ,
--la livraison passe au statut en cours, sinon elle sera au statut envoyé. (SGBD trigger sur commande insert)

Create or Replace TRIGGER G5
After insert or Update on Orders
For each row
declare 
	n int;
BEGIN
	select stock into n from Formats, Article, Supply, Orders where
	Formats.IdFormat = Article.IdFormat and
	Article.IdSupply = Supply.IdSupply and 
	Article.IdOrder = Orders.IdOrder and
	Orders.Status = 'en cours';
	if (n > 0) then
		 update Supply set StatusSup= 'envoyé';
	end if;	
	if (n = 0) then
		 update Supply set StatusSup= 'en cours';
	end if;	
END;
/