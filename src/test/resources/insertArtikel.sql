insert into artikels(naam, aankoopprijs, verkoopprijs, soort, houdbaarheid) values ('foodartikel', 100, 120, 'F', 7);
insert into artikels(naam, aankoopprijs, verkoopprijs, soort, garantie) values ('nonfoodartikel', 100, 120, 'NF', 30);
insert into kortingen(artikelid, vanafaantal, percentage) values ((select id from artikels where naam ='foodartikel'), 20, 5);