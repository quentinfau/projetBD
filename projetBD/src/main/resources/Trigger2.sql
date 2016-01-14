create or replace trigger trg_Resolution
After Insert or update ON Formats
Declare 
v1 integer;
v2 integer; 
Begin 

Select Image.ResolutionImage into v1, Formats.ResolutionMin into v2 From Image, Formats 
                                                         Where (Formats.IdFormat = Article.IdFormat
														    and Article.IdAlbum=Album.IdAlbum
														    and Album.IdClient= Client.IdClient 
														    and Client.IdClient = Image.IdImage);
							If 	(v1 < v2) then
							 raise_application_error(-20105,'Probleme de Résolution');
							end if;
End;
/
								 					   