--Pour une commande dépassant les 100 euros, un code promo est généré.
--Sa valeur est de 5% de la commande ayant donné lieu à ce code. (SGBD trigger) 

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