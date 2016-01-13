insert into Client (IdClient, FirstName, LastName, Mail, Password, Address) Values(1,'Alexis','Salem','alexis@salem.fr','secret','grenoble');
insert into Client (IdClient, FirstName, LastName, Mail, Password, Address) values(2,'Quentin','Fau','Quentin@fau.fr','hello','grenoble');
insert into Client values(3,'Mani','To','maniToo@fau.fr','hello','Paris');

insert into Formats (IdFormat, Label, Price, ResolutionMin, Speed, Stock) values(1,'A3',2,10,1000,20);
insert into Formats (IdFormat, Label, Price, ResolutionMin, Speed, Stock) values(2,'A4',0.2,6,1700,1000);
insert into Formats values(3,'A5',2,4,'20000','1000');

insert into Prestataire (IdPrestataire, NamePresta, AddressPresta, Preference) values(1,'ImprimeFAU','Annecy',1);
insert into Prestataire (IdPrestataire, NamePresta, AddressPresta, Preference) values(2,'PrinterSA','Grenoble',2);
insert into Prestataire (IdPrestataire, NamePresta, AddressPresta, Preference) values(3,'PrintSociety','Paris',3);

insert into Image (IdImage, IdClient, PathImage, Shared, ResolutionImage, Info) values(1,1,'./image', 1,5,'Vancances plage ');
insert into Image (IdImage, IdClient, PathImage, Shared, ResolutionImage, Info) values(2,2,'./image',1,20,'Vancances Berlin ');
insert into Image (IdImage, IdClient, PathImage, Shared, ResolutionImage, Info) values(3,2,'./images',0,16,'Vancances hiver ');
