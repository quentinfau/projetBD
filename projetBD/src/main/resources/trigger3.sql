create or replace trigger trg_dateLivraison
After Insert or update ON Orders
Declare 
v date;
s varchar(50);
Begin 

Select Orders.DateOrder, Orders.Status into v,s from Orders natural join Article ;
 
	if (s = 'en cours') then
insert into Supply(IdSupply, DateSup, StatusSup) values (1, v+2 ,'en cours'); 
     end if; 
End;
/
								 					   