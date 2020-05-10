# Käyttöohje

### Config
Sovellus lukee tiedostosta *config.properties* sovelluksen tarvitsemia arvoja, liittyen leaderboards-toiminnallisuuteen
sekä vaikeustason määrittelemiseen. 
Sovellus olettaa sekä *config.properties*, *credentials.json* että *tokens/StoredCredential* -tiedostojen olevan käynnistyshakemistossa. Eli kun jar-tiedosto
suoritetaan, niin kyseisten tiedostojen pitää löytyä samasta kansiosta. (Tämän takia release on pakattu, ja kaikki tarvittava löytyy siis ladatusta kansiosta)


### Käynnistys

Kun olet ladannut zip-tiedoston, pura se. Tämän jälkeen ohjelma käynnistetään komennolla  
``
java -jar Osteroids-1.0.jar
``

### Peli

INSTRUCTIONS -napista pääset katsomaan peliin tarvittavat näppäimet.

START GAME -nappi aloittaa pelin. Tarkoitus on tuhota vihollinen ja selvitä mahdollisimman pitkälle mahdollisimman nopeasti.
Muista valita vaikeustaso ensin.

Pelin päätyttyä näet tuloksesi. Pääset tästä näkymästä katsomaan muita tuloksia ja halutessasi tallentamaan oman parhaan tuloksesi.   

EXIT-nappi sulkee sovelluksen.


