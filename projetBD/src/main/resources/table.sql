create table Client (
	IdClient int, 
	FirstName varchar(50), 
	LastName varchar(50), 
	Mail varchar(50),
	Password varchar(50), 
	Address varchar(50),
	primary key (IdClient)
);
CREATE SEQUENCE IdClient
 START WITH     1
 INCREMENT BY   1
 Cache 50;

create table Supply  (
	IdSupply int, 
	DateSup date,
	StatusSup varchar(50) CHECK( StatusSup IN ('en cours','envoye','annule') ),
	primary key(IdSupply)
);
CREATE SEQUENCE IdSupply
 START WITH     1
 INCREMENT BY   1
 Cache 50;

create table Formats  (
	IdFormat int, 
	Label varchar(50), 
	Price long, 
	ResolutionMin int, 
	Speed int, 
	Stock int,
	primary key (IdFormat)
);
CREATE SEQUENCE IdFormat
 START WITH     1
 INCREMENT BY   1
 Cache 50;

create table Prestataire (
	IdPrestataire int, 
	NamePresta varchar(50), 
	AddressPresta varchar(50), 
	Preference int,
	primary key(IdPrestataire)
);
CREATE SEQUENCE IdPrestataire
 START WITH     1
 INCREMENT BY   1
 Cache 50;

create table CodePromo(
	IdPromo int NOT NULL, 
	Amount long, 
	IdClient int,
	primary key (IdPromo),
	constraint CodePromo_C1 foreign key (IdClient) references Client(IdClient) 
);
CREATE SEQUENCE IdPromo
 START WITH     1
 INCREMENT BY   1
 Cache 50;

create table Orders  (
	IdOrder int NOT NULL, 
	DateOrder date, 
	TotalPrice long, 
	IdClient int,
	Status varchar(50) CHECK( Status IN ('en cours','envoi complet', 'envoi partiel', 'annulee') ), 
	primary key (IdOrder),
	constraint Orders_C1 foreign key (IdClient) references Client(IdClient) 
);
CREATE SEQUENCE IdOrder
 START WITH     1
 INCREMENT BY   1
 Cache 50;


create table Album  (
	IdAlbum int NOT NULL, 
	IdClient int NOT NULL, 
	NbPages int,
	NameAlbum varchar(50), 
	primary key (IdAlbum),
	constraint Album_C1 foreign key (IdClient) references Client(IdClient) 
);
CREATE SEQUENCE IdAlbum
 START WITH     1
 INCREMENT BY   1
 Cache 50;

create table Article (
	IdArticle int NOT NULL, 
	IdOrder int NOT NULL,
	IdAlbum int NOT NULL, 
	IdSupply int NOT NULL, 
	IdFormat int NOT NULL,
	Quantity int,
	primary key(IdArticle),
	constraint ArticleFormat_C1 foreign key (IdFormat) references Formats(IdFormat),
	constraint ArticleOrder_C2 foreign key (IdOrder) references Orders(IdOrder),
	constraint ArticleAlbum_C3 foreign key (IdAlbum) references Album(IdAlbum),
	constraint ArticleSupply_C4 foreign key (IdSupply) references Supply(IdSupply)
);
CREATE SEQUENCE IdArticle
 START WITH     1
 INCREMENT BY   1
 Cache 50;

create table Image (
	IdImage int NOT NULL,
	IdClient int NOT NULL, 
	PathImage varchar(255) NOT NULL, 
	Shared number(1) CHECK(Shared in (0,1)), 
	ResolutionImage int,
	Info varchar(255),
	primary key(IdImage),
	constraint Image_C1 foreign key (IdClient) references Client(IdClient)
);
CREATE SEQUENCE IdImage
 START WITH     1
 INCREMENT BY   1
 Cache 50;


create table Contact (
	IdPrestataire int,
	IdFormat int, 
	LimitTime int,
	primary key (IdPrestataire,IdFormat),
	constraint ContactPrestaire_C1 foreign key (IdPrestataire) references Prestataire(IdPrestataire) on delete cascade,
	constraint ContactFormat_C2 foreign key (IdFormat) references Formats(IdFormat) on delete cascade
);

create table Photo (
	NumPage int NOT NULL,
	IdAlbum int NOT NULL,
	IdImage int, 
	Title varchar(50),
	Comments varchar(50),
	primary key(NumPage, IdAlbum),
	constraint PhotoAlbum_C1 foreign key (IdAlbum) references Album(IdAlbum),
	constraint PhotoImage_C2 foreign key (IdImage) references Image(IdImage)
);


create table Calendar (
	IdAlbum int, 
	TypeCalendar varchar(50) CHECK( TypeCalendar IN ('Bureau','Mural') ), 
	primary key(IdAlbum),
	constraint Calendar_C1 foreign key (IdAlbum) references Album(IdAlbum) on delete cascade
);

create table Agenda (
	IdAlbum int, 
	TypeAgenda VARCHAR2(10) CHECK( TypeAgenda IN ('52s','365j') ),
	primary key(IdAlbum),
	constraint Agenda_C1 foreign key (IdAlbum) references Album(IdAlbum) on delete cascade
);

create table Book (
	IdAlbum int, 
	Preface varchar(50),
	PostFace varchar(50),
	BookTitle varchar(50), 
	primary key(IdAlbum),
	constraint Book_C1 foreign key (IdAlbum) references Album(IdAlbum) on delete cascade
);
