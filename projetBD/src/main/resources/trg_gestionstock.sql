create or replace trigger trg_gestionstock 
After Insert or update ON Article
for each row
Declare
vitesse integer;
stockFormat integer;
q integer;
idF integer;
nbP integer;
idA integer;
nbStockPourPresta integer;
idPresta integer;
delai integer;
idS integer;
productionJour integer;
quantiteParCommande integer;
nbPageParCommande integer;
Begin 
q := :new.quantity; 
idF := :new.idFormat;
idA := :new.idAlbum;
idS := :new.idSupply;

select nbPages into nbP from Album where idAlbum=idA ;
select stock into stockFormat from Formats where idFormat=idF ;
select speed into vitesse from Formats where idFormat=idF;

FOR j IN (select idArticle from Article where idFormat=idF AND idOrder IN (select idOrder from Orders where dateOrder=SYSDATE)) LOOP
    select nbPages into nbPageParCommande from Album where idAlbum = (select idAlbum from Article where idArticle=j.idArticle) ;
	select quantity into quantiteParCommande from Article where idArticle=j.idArticle ;
	productionJour := productionJour + (quantiteParCommande*nbPageParCommande);
  END LOOP;

if (stockFormat<q*nbP OR productionJour>vitesse) then 
	select idPrestataire into idPresta from prestataire where preference = (select Max(preference) from Prestataire);
	select limitTime into delai from Contact where idPrestataire=idPresta AND idFormat=idF ;
	
	update Supply set DateSup = (DateSup + delai) where idSupply=idS;
	update Supply set StatusSup= 'en cours' where idSupply=idS;
else
	update Formats set Stock = (Stock - (q*nbP)) where Formats.IdFormat= idF;
	update Supply set StatusSup= 'envoyé' where idSupply=idS;
	
end if;
			
End;
/  