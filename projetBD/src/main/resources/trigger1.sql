create or replace trigger trg_logoff
BEFORE LOGOFF 
on UFRIMA.DBIMA2AG.UJF-GRENOBLE.FR
for each row
Declare
ids int(3);
begin
  select distinct(idImage) into ids from image where idImage not in (select distinct(idImage) from Photo natural join image ) ;
  delete from image where idImage=ids;
end;
/