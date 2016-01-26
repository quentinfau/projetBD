Create or Replace TRIGGER G4
After insert or Update on Orders
For each row
declare
	n int;
	p number;
BEGIN
	n := :new.idClient;
	p := :new.TotalPrice;
	if (p > 100) then
	insert into CodePromo values (IdPromo.NEXTVAL,p*0.05,n);
	end if;	
END;
/