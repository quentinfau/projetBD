Create or Replace TRIGGER trg_deleteimage
after Delete or Update on Orders
for each row
declare
	statusOrder varchar2(20);
	idCmd int;
BEGIN
	statusOrder := :new.status;
	idCmd := :new.idOrder;
	
	if (statusOrder='envoi complet')
		then 
		FOR j IN (select idImage from TempImageForDelete where idImage in ( select idImage from Photo where idAlbum in (select idAlbum from Article where idOrder=idCmd))) LOOP
   			delete from TempImageForDelete where idImage=j.idImage ;
			delete from Image where idImage=j.idImage ;	
		END LOOP;
	end if;
END;