# Testausdokmentti

## Testauskattavuus

Kuvassa Jacoco Report.
<img src="https://github.com/ArtKoski/ot-harjoitustyo/blob/master/Osteroids/dokumentaatio/kuvat/TotalMissed.png">

Applogic sisälsi yhden metodin joka muokkasi varsinaisia leaderboards-tuloksia, joten jätin sen testaamatta.
Toinen metodi jonka päätin jättää testaamatta liittyi JavaFX komponenttiin. Jos näitä kahta metodia ei lasketa
raporttiin, niin applogic kattavuus nousee melkein sataan, jolloin kokonaiskattavuus on ~90%.

GUI-luokka tosiaan otettu pois raportista.

## Yksikkötesteistä

Yksi testi-luokka (SpriteTest) testaa Sprite-olioiden perustoiminnallisuutta. Suurin osa niiden toiminnallisuudesta kuitenkin tuli testattua
pelaamalla ja kokeilemalla, sillä pelissä oliot liikkuvat nuolinäppäimillä.

Toinen testiluokka (configLoadTest) testaa, että config tiedosto löydetään, ja että se tuo mukanaan jotain arvoja.

Kolmas testiluokka (scoreAndLeaderboardTest) testaa piste-luokkaa sekä leaderboards-toiminnallisuutta. 
Jälkimmäisen testaus osoittautui hankalaksi, ja päätin tehdä testisyötteille oman taulukon ja metodin. 
Parhaat testit leaderboardsin toteuttamiseen osoittautuivat olevan lopulta käytännön kokeiluja.

## CheckStyle
Viimeisimmässä versiossa checkstyle-virheitä löytyi 5 kappaletta, joista lähes kaikki liittyivät 'liian' suuriin metodeihin.


## Järjestelmätestaus
Ohjelmaa on testattu vain Unix-järjestelmällä.  
