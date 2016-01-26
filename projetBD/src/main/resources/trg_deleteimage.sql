Create or Replace TRIGGER trg_deleteimage
after Delete or Update on Orders
declare
	var varchar2(20);
	stat int;
	idImg int;
BEGIN
	select idImage into idImg from TempImageForDelete;
	select count(status) into stat from orders where status='en cours' and idOrder in (select idOrder from Article where idAlbum in (select idAlbum from Photo where idImage=IdImg));
	if (stat = 0)
	then
		delete from TempImageForDelete where idImage=idImg ;
		delete from Image where idImage=idImg ;	
		end if;
END;