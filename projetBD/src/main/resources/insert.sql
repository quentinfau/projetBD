insert into Client (IdClient, FirstName, LastName, Mail, Password, Address) values(IdClient.NEXTVAL,'Alexis','Salem','alexis@salem.fr','secret','grenoble');
insert into Client (IdClient, FirstName, LastName, Mail, Password, Address) values(IdClient.NEXTVAL,'Quentin','Fau','Quentin@fau.fr','hello','grenoble');
insert into Client values(IdClient.NEXTVAL,'Mani','To','maniToo@fau.fr','hello','Paris');
insert into Client (IdClient, FirstName, LastName, Mail, Password, Address) values(IdClient.NEXTVAL,'Jean','MI','Jami@fau.fr','hello','Paris');

insert into Formats (IdFormat, Label, Price, ResolutionMin, Speed, Stock) values(IdFormat.NEXTVAL,'A3',2,10,1500,5000);
insert into Formats (IdFormat, Label, Price, ResolutionMin, Speed, Stock) values(IdFormat.NEXTVAL,'A4',0.2,6,2000,10000);
insert into Formats values(IdFormat.NEXTVAL,'A5',2,4,500,2000);


insert into Prestataire (IdPrestataire, NamePresta, AddressPresta, Preference) values(IdPrestataire.NEXTVAL,'ImprimeFAU','Annecy',1);
insert into Prestataire (IdPrestataire, NamePresta, AddressPresta, Preference) values(IdPrestataire.NEXTVAL,'PrinterSA','Grenoble',2);
insert into Prestataire (IdPrestataire, NamePresta, AddressPresta, Preference) values(IdPrestataire.NEXTVAL,'PrintSociety','Paris',3);

insert into Image (IdImage, IdClient, PathImage, Shared, ResolutionImage, Info) values(IdImage.NEXTVAL,3,'./image', 0,20,'Vancances plage ');
insert into Image (IdImage, IdClient, PathImage, Shared, ResolutionImage, Info) values(IdImage.NEXTVAL,2,'./image',1,20,'Vancances Berlin ');
insert into Image (IdImage, IdClient, PathImage, Shared, ResolutionImage, Info) values(IdImage.NEXTVAL,2,'./images',1,16,'Vancances hiver ');
	
insert into Contact (IdPrestataire, IdFormat , LimitTime) values (3, 3, 2);	
insert into Contact (IdPrestataire, IdFormat , LimitTime) values (3, 2, 3);
insert into Contact (IdPrestataire, IdFormat , LimitTime) values (3, 4, 2);
insert into Contact (IdPrestataire, IdFormat , LimitTime) values (4, 3, 3);
insert into Contact (IdPrestataire, IdFormat , LimitTime) values (4, 2, 2);
insert into Contact (IdPrestataire, IdFormat , LimitTime) values (4, 4, 2);
insert into Contact (IdPrestataire, IdFormat , LimitTime) values (2, 3, 2);
insert into Contact (IdPrestataire, IdFormat , LimitTime) values (2, 2, 3);
insert into Contact (IdPrestataire, IdFormat , LimitTime) values (2, 4, 2);
