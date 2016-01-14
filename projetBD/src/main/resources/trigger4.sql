Create or Replace TRIGGER trigger4
After insert or Update on Orders
For each row
BEGIN
	if (Orders.TotalPrice > 100)
		 update CodePromo set amout= TotalPrice * 0.05 where CodePromo.IdClient = Client. IdClient 
		 and Client.IdClient = Orders.IdCleint;
	end if;	
END;
/