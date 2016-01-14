--Pour une commande d�passant les 100 euros, un code promo est g�n�r�.
--Sa valeur est de 5% de la commande ayant donn� lieu � ce code. (SGBD trigger) 

Create or Replace TRIGGER G4
After insert or Update on Orders
For each row
BEGIN
	if (Orders.TotalPrice > 100)
		 update CodePromo set amount = TotalPrice * 0.05 where CodePromo.IdClient = Client.IdClient 
		 and Client.IdClient = Orders.IdCleint;
	end if;	
END;
/