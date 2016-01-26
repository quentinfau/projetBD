create or replace trigger trg_Resolution
After Insert or update ON Article
for each row
Declare 
resImage integer;
resMin integer;
idF integer;
idA integer;
Begin 
idF := :new.idFormat;
idA := :new.idAlbum;
	
select Formats.ResolutionMin into resMin From Formats where Formats.IdFormat = idF ;
 FOR j IN (select idImage from Photo where idAlbum=idA) LOOP
    select Image.ResolutionImage into resImage From Image where Image.idImage = j.idImage;
							If 	(resImage < resMin) then
							 raise_application_error(-20105,'Probleme de Resolution');
							end if;
  END LOOP;

End;
/