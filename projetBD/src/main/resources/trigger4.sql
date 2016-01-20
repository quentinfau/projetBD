Create or Replace TRIGGER G4
After insert or Update on Orders
For each row
declare
	n int;
	p number;
BEGIN
	select IdClient, totalprice into n,p from Orders natural join Client;
	if (p > 100) then
		insert into CodePromo values (1,p*0.05,n);
	end if;	
END;
/