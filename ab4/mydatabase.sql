CREATE TABLE Localitate (
id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
Tara VARCHAR(30) NOT NULL,
Judetul VARCHAR(30) NOT NULL,
Orasul VARCHAR(50) NOT NULL,
Locatie VARCHAR(50) NOT NULL,
Pret INT(6) NOT NULL,
Start_data DATE NOT NULL,
End_data  DATE NOT NULL,
Trase_montane BOOLEAN,
Plaja BOOLEAN,
Inot BOOLEAN,
Muzeu BOOLEAN,
Teatru BOOLEAN,
Turistic BOOLEAN
);


INSERT INTO Localitate(Tara, Judetul, Orasul, Locatie, Pret, Start_data, End_data, Trase_montane, Plaja, Inot, Muzeu, Teatru, Turistic)
		VALUES("Romania", "Prahova", "Sinaia", "Marea Neagra", 300, '2018-10-1', "2019-3-1", true, false, false, false, false, true);
INSERT INTO Localitate(Tara, Judetul, Orasul, Locatie, Pret, Start_data, End_data, Trase_montane, Plaja, Inot, Muzeu, Teatru, Turistic)
		VALUES("Romania", "Prahova", "Sinaia", "Rina", 360, '2018-10-1', "2019-3-1", true, false, false, false, false, true);

INSERT INTO Localitate(Tara, Judetul, Orasul, Locatie, Pret, Start_data, End_data, Trase_montane, Plaja, Inot, Muzeu, Teatru, Turistic)
		VALUES("Romania", "Hunedoara", "Retezat" , "Colt", 250, '2018-10-1', "2019-3-1", true, false, false, false, false, true);
INSERT INTO Localitate(Tara, Judetul, Orasul, Locatie, Pret, Start_data, End_data, Trase_montane, Plaja, Inot, Muzeu, Teatru, Turistic)
		VALUES("Romania", "Hunedoara", "Retezat" , "Cabana", 220, '2018-10-1', "2019-3-1", true, false, false, false, false, true);



INSERT INTO Localitate(Tara, Judetul, Orasul, Locatie, Pret, Start_data, End_data, Trase_montane, Plaja, Inot, Muzeu, Teatru, Turistic)
			VALUES("Romania", "Constanta", "Constanta", "Ibis", 600, '2018-5-1', "2018-10-1", false, true, true, false, false, false);
INSERT INTO Localitate(Tara, Judetul, Orasul, Locatie, Pret, Start_data, End_data, Trase_montane, Plaja, Inot, Muzeu, Teatru, Turistic)
			VALUES("Romania", "Constanta", "Constanta", "Carol", 400, '2018-5-1', "2018-10-1", false, true, true, false, false, false);

INSERT INTO Localitate(Tara, Judetul, Orasul, Locatie, Pret, Start_data, End_data, Trase_montane, Plaja, Inot, Muzeu, Teatru, Turistic)
		 	VALUES("Ucraina", "Bugaz", "Zatoka", "Adam & Eva", 200, '2018-5-1', "2018-10-1", false, true, true, false, false, false);
INSERT INTO Localitate(Tara, Judetul, Orasul, Locatie, Pret, Start_data, End_data, Trase_montane, Plaja, Inot, Muzeu, Teatru, Turistic)
		 	VALUES("Ucraina", "Bugaz", "Zatoka", "Akunamatata", 150, '2018-5-1', "2018-10-1", false, true, true, false, false, false);


INSERT INTO Localitate(Tara, Judetul, Orasul, Locatie, Pret, Start_data, End_data, Trase_montane, Plaja, Inot, Muzeu, Teatru, Turistic)
		 	VALUES("Romania", "Bucuresti", "Bucuresti", "Ramada", 550, '2018-1-1', "2019-12-31", false, false, false, true, true, true);
INSERT INTO Localitate(Tara, Judetul, Orasul, Locatie, Pret, Start_data, End_data, Trase_montane, Plaja, Inot, Muzeu, Teatru, Turistic)
		 	VALUES("Romania", "Bucuresti", "Bucuresti", "Rin", 650, '2018-1-1', "2019-12-31", false, false, false, true, true, true);

INSERT INTO Localitate(Tara, Judetul, Orasul, Locatie, Pret, Start_data, End_data, Trase_montane, Plaja, Inot, Muzeu, Teatru, Turistic)
	 		VALUES("Rusia", "Moscova", "Moscova", "Cosmos", 850, "2018-1-1", "2019-12-31", false, false, false, true, true, true);
INSERT INTO Localitate(Tara, Judetul, Orasul, Locatie, Pret, Start_data, End_data, Trase_montane, Plaja, Inot, Muzeu, Teatru, Turistic)
	 		VALUES("Rusia", "Moscova", "Moscova", "Sadovnicheskaya", 900, '2018-1-1', "2019-12-31", false, false, false, true, true, true);
