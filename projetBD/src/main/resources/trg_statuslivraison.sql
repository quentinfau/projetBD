Create or Replace TRIGGER trg_statuslivraison
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