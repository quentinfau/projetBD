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

insert into Image (IdImage, IdClient, PathImage, Shared, ResolutionImage, Info) values(IdImage.NEXTVAL,1,'./image', 0,5,'Vancances plage ');
insert into Image (IdImage, IdClient, PathImage, Shared, ResolutionImage, Info) values(IdImage.NEXTVAL,2,'./image',1,20,'Vancances Berlin ');
insert into Image (IdImage, IdClient, PathImage, Shared, ResolutionImage, Info) values(IdImage.NEXTVAL,2,'./images',0,16,'Vancances hiver ');


