insert into Client (IdClient, FirstName, LastName, Mail, Password, Address) Values(IdClient.NEXTVAL,'Alexis','Salem','alexis@salem.fr','secret','grenoble');
insert into Client (IdClient, FirstName, LastName, Mail, Password, Address) values(IdClient.NEXTVAL,'Quentin','Fau','Quentin@fau.fr','hello','grenoble');
insert into Client values(IdClient.NEXTVAL,'Mani','To','maniToo@fau.fr','hello','Paris');
insert into Client (IdClient, FirstName, LastName, Mail, Password, Address) values(IdClient.NEXTVAL,'Jean','MI','Jami@fau.fr','hello','Paris');

insert into Formats (IdFormat, Label, Price, ResolutionMin, Speed, Stock) values(IdFormat.NEXTVAL,'A3',2,10,1000,20);
insert into Formats (IdFormat, Label, Price, ResolutionMin, Speed, Stock) values(IdFormat.NEXTVAL,'A4',0.2,6,1700,1000);
insert into Formats values(IdFormat.NEXTVAL,'A5',2,4,'20000','1000');


insert into Prestataire (IdPrestataire, NamePresta, AddressPresta, Preference) values(IdPrestataire.NEXTVAL,'ImprimeFAU','Annecy',1);
insert into Prestataire (IdPrestataire, NamePresta, AddressPresta, Preference) values(IdPrestataire.NEXTVAL,'PrinterSA','Grenoble',2);
insert into Prestataire (IdPrestataire, NamePresta, AddressPresta, Preference) values(IdPrestataire.NEXTVAL,'PrintSociety','Paris',3);

insert into Image (IdImage, IdClient, PathImage, Shared, ResolutionImage, Info) values(IdImage.NEXTVAL,3,'./image', 0,5,'Vancances plage ');
insert into Image (IdImage, IdClient, PathImage, Shared, ResolutionImage, Info) values(IdImage.NEXTVAL,2,'./image',1,20,'Vancances Berlin ');
insert into Image (IdImage, IdClient, PathImage, Shared, ResolutionImage, Info) values(IdImage.NEXTVAL,2,'./images',0,16,'Vancances hiver ');


insert into Orders (IdOrder, DateOrder, TotalPrice, IdClient, Status ) values (IdOrder.NEXTVAL, SYSDATE , 90, 3, 'en cours');
insert into Orders (IdOrder, DateOrder, TotalPrice, IdClient, Status ) values (IdOrder.NEXTVAL, SYSDATE , 130, 2, 'en cours');
insert into Orders (IdOrder, DateOrder, TotalPrice, IdClient, Status ) values (IdOrder.NEXTVAL, SYSDATE , 40, 2, 'en cours');
insert into Orders (IdOrder, DateOrder, TotalPrice, IdClient, Status ) values (IdOrder.NEXTVAL, SYSDATE , 55, 3, 'en cours');


insert into Supply(IdSupply, DateSup, StatusSup) values (IdSupply.NEXTVAL, SYSDATE, 'en cours');
insert into Supply(IdSupply, DateSup, StatusSup) values (IdSupply.NEXTVAL, SYSDATE, 'en cours');
insert into Supply(IdSupply, DateSup, StatusSup) values (IdSupply.NEXTVAL, SYSDATE, 'envoye');


insert into CodePromo values(IdPromo.NEXTVAL, 10, 2);


insert into Album (IdAlbum, IdClient, NbPages, NameAlbum) values (IdAlbum.NEXTVAL, 3, 4, 'Voyage');
insert into Album (IdAlbum, IdClient, NbPages, NameAlbum) values (IdAlbum.NEXTVAL, 4, 6, 'souven');
insert into Album (IdAlbum, IdClient, NbPages, NameAlbum) values (IdAlbum.NEXTVAL, 2, 6, 'souvens');
insert into Album (IdAlbum, IdClient, NbPages, NameAlbum) values (IdAlbum.NEXTVAL, 4, 6, 'sounirs');


insert into Article (IdArticle, IdOrder, IdAlbum, IdSupply, IdFormat, Quantity) values (IdArticle.NEXTVAL, 3, 3, 3, 3, 2 );	
insert into Article (IdArticle, IdOrder, IdAlbum, IdSupply, IdFormat, Quantity) values (IdArticle.NEXTVAL, 2, 2, 2, 2, 1 );	


	
insert into Contact (IdPrestataire, IdFormat , LimitTime) values (3, 3, 24);	
insert into Contact (IdPrestataire, IdFormat , LimitTime) values (2, 2, 48);

	
insert into Photo (NumPage, IdAlbum, IdImage, Title, Comments ) values (2, 3, 3, 'photo identité', 'mise hier');		
insert into Photo (NumPage, IdAlbum, IdImage, Title, Comments ) values (3, 2, 3, 'photo identité', 'mise hier');		
insert into Photo (NumPage, IdAlbum, IdImage, Title, Comments ) values (1, 3, 4, 'photo identité', 'mise hier');		
	
	
insert into Calendar (IdAlbum, TypeCalendar) values (2, 'Mural');
	
insert into Agenda (IdAlbum, TypeAgenda) values (3, '52s'); 

 
insert into Book (IdAlbum, Preface, PostFace, BookTitle) values(4, 'Preface', 'PostFace', 'Mon Livre');
	
