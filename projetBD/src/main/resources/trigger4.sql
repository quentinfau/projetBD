Create or Replace TRIGGER G4
After insert or Update on Orders
For each row
declare
	n int;
	p number;
BEGIN
	select idClient, TotalPrice into n, p from Orders natural join client;
	if (p > 100) then
	insert into CodePromo values (1,p,n);
	end if;	
END;
/