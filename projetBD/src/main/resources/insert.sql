insert into Client (IdClient, FirstName, LastName, Mail, Password, Address) Values(1,'Alexis','Salem','alexis@salem.fr','secret','grenoble');
insert into Client (IdClient, FirstName, LastName, Mail, Password, Address) values(2,'Quentin','Fau','Quentin@fau.fr','hello','grenoble');
insert into Client values(3,'Mani','To','maniToo@fau.fr','hello','Paris');

insert into Formats (IdFormat, Label, Price, Resolution, Speed, Stock) values(1,'A3','2euros','1024*1080','15secondes','20');
insert into Formats (IdFormat, Label, Price, Resolution, Speed, Stock) values(2,'A4','2centimes','16:9','5 secondes','1000');
insert into Formats values(3,'A5','20centimes','4:3','2 secondes','1000');

insert into Prestataire (IdPrestataire, NamePresta, Adresse, Preference) values(1,'ImprimeFAU','Annecy','1');
insert into Prestataire (IdPrestataire, NamePresta, Adresse, Preference) values(2,'PrinterSA','Grenoble','2');
insert into Prestataire (IdPrestataire, NamePresta, Adresse, Preference) values(3,'PrintSociety','Paris','3');

insert into Image (IdImage, IdClient, Source, Shared, Resolution, Info) values(1,1,'./image', 'true','16:9','Vancances plage ');
insert into Image (IdImage, IdClient, Source, Shared, Resolution, Info) values(2,2,'./image', 'true','16:9','Vancances Berlin ');
insert into Image (IdImage, IdClient, Source, Shared, Resolution, Info) values(3,2,'./images', 'false','16:9','Vancances hiver ');
