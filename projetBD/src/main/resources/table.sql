

create table Client (
	IdClient int, 
	FirstName varchar(50), 
	LastName varchar(50), 
	Mail varchar(50),
	Password varchar(50), 
	Address varchar(50),
	primary key (IdClient)
);

create table Supply  (
	IdSupply int, 
	DateSUp date,
	StatutSup varchar(50),
	primary key(IdSupply)
);

create table Formats  (
	IdFormat int, 
	Label varchar(50), 
	Price varchar(50), 
	Resolution varchar(50), 
	Speed varchar(50), 
	Stock varchar(50),
	primary key (IdFormat)
);

create table Prestataire (
	IdPrestataire int, 
	NamePresta varchar(50), 
	Adresse varchar(50), 
	Preference varchar(50),
	primary key(IdPrestataire)
);

create table CodePromo(
	IdPromo int NOT NULL, 
	Amount varchar(50), 
	IdClient int,
	primary key (IdPromo),
	constraint CodePromo_C1 foreign key (IdClient) references Client(IdClient) 
);

create table Orders  (
	IdOrder int NOT NULL, 
	DateOrder date, 
	TotalPrice varchar(50), 
	Statut varchar(50), 
	primary key (IdOrder),
	IdClient int,
	constraint Orders_C1 foreign key (IdClient) references Client(IdClient) 
);

create table Album  (
	IdAlbum int NOT NULL, 
	IdClient int, 
	NbPages varchar(50),
	primary key (IdAlbum),
	constraint Album_C1 foreign key (IdClient) references Client(IdClient) 
);



create table Article (
	IdArticle int, 
	IdOrder int,
	IdAlbum int, 
	IdSupply int, 
	IdFormat int, 
	Quantity varchar(50), 
	UnitPrice varchar(50),
	primary key(IdArticle),
	constraint ArticleFormat_C1 foreign key (IdFormat) references Formats(IdFormat),
	constraint ArticleOrder_C2 foreign key (IdOrder) references Orders(IdOrder),
	constraint ArticleAlbum_C3 foreign key (IdAlbum) references Album(IdAlbum),
	constraint ArticleSupply_C4 foreign key (IdSupply) references Supply(IdSupply)
);

create table Image (
	IdImage int,
	IdClient int, 
	Source varchar(50), 
	Shared varchar(50), 
	Resolution varchar(50),
	Info varchar(50),
	primary key(IdImage),
	constraint Image_C1 foreign key (IdClient) references Client(IdClient)
);


create table Contact (
	IdPrestataire int,
	IdFormat int, 
	LimitTime varchar(50),
	primary key (IdPrestataire,IdFormat),
	constraint ContactPrestaire_C1 foreign key (IdPrestataire) references Prestataire(IdPrestataire) on delete cascade,
	constraint ContactFormat_C2 foreign key (IdFormat) references Formats(IdFormat) on delete cascade
);

create table Photo (
	NumPage varchar(50),
	IdAlbum int,
	IdImage int, 
	Title varchar(50),
	Comments varchar(50),
	primary key(NumPage),
	constraint PhotoAlbum_C1 foreign key (IdAlbum) references Album(IdAlbum),
	constraint PhotoImage_C2 foreign key (IdImage) references Image(IdImage)
);

create table Calendar (
	IdAlbum int, 
	TypeCalendar varchar(50), 
	primary key(IdAlbum),
	constraint Calendar_C1 foreign key (IdAlbum) references Album(IdAlbum) on delete cascade
);

create table Agenda (
	IdAlbum int, 
	TypeAgenda varchar(50), 
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
