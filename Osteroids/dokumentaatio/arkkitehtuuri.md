# Arkkitehtuurikuvaus
### Pakkausrakenne 
<p>
Ohjelman pakkausrakenne on seuraavanlainen:
</p>
<img src="https://github.com/ArtKoski/ot-harjoitustyo/blob/master/Osteroids/dokumentaatio/kuvat/Draft.png">
<p>
GUI sisältää graafisen käyttöliittymän sekä tarvittavat JavaFX-toiminnot peliä
varten. Pelin logiikka, elikkä Sprite-oliot, löytyvät gamelogic-pakkauksesta. Muu logiikka, esim. pistetuloksiin liittyvät,
löytyy applogic-pakkauksesta.
</p>


### Logiikka eri näkymille
<p>
Alla oleva sekvennsikaavio kuvaa sovelluksen toiminnallisuutta graafisen käyttöliittymän kannalta.
Scene-laatikot ovat siis omia skene-olioita joiden välillä voidaan hyppiä nappien (kuvassa nuolia) avulla.
Poikkeuksena 'LOSE/WIN' -nuoli, joka kuvaa pelin päätöstä. Monissa skeneissä myös on Exit-nappi, joka jätettiin
kuvasta pois.
</p>
<img src="https://github.com/ArtKoski/ot-harjoitustyo/blob/master/Osteroids/dokumentaatio/kuvat/GUI.png">


### Liikkuvien objektien toteutus
<p>
Kaikki liikkuvat objektit perivät Sprite-luokan. Tämä helpottaa peliruudun toiminnallisuutta suuresti, sillä nyt GUI-luokka voi käydä
läpi muutamaa ArrayList<Sprite>-listaa ja tehdä päätöksiä esim. olioiden "healthin" perusteella. Sekä vihollinen että ammuttu ammus esimerkiksi
voivat käyttää samaa die()-metodia, jonka avulla ne voidaan hävittää ruudulta.
</p>

### Tietojen tallenus/haku
-Sovellus käyttää Google Sheets -palvelua tulosten säilyttämiseen. Applogic-pakkauksen luokka SheetsLeaderBoards on vastuussa yhteyden muodostamisesta
google-spreadsheetsiin. Tähän tarvitaan tiedostoja properties.config, credentials.json sekä StoredCrendentials, jotka löytyvät projektin hakemistosta.  Kun tuloksia halutaan päivittää, niin 
luokka ensin hakee taulukossa olevat tulokset itselleen prioriteettijonoon. Tämän jälkeen uusi tulos lisätään jonoon, jonka jälkeen tulokset päivitetään taulukkoon. Tulosten
vertailu perustuu Score-luokan compareTo-metodiin (Kierrokset>pisteet>aika), ja näin prioriteettijono pitää tulokset järjestyksessä. 

Alla havainnollistus config.properties tietojensäilytyksestä  
 
``
multiplierEasy=1  

multiplierHard=3
``
Eli kun valitaan vaikeustasoksi easy, niin tiedostosta haetaan multiplierEasy arvo.

## Ohjelman rakenteen potentiaaliset miinukset
<p
GUI-luokka sisältää jonkin verran loogisia operaatioita, jotka voitaisiin hoitaa muualla.
AnimationTimer-lohko on suuri, joka johti minut kyseenalaistamaan toteutustani.
Tietojen tallennus sekä haku Sheets-taulusta on raskaasti toteutettu. Toisaalta maksimikoko taulukon alkioille on vain 5.
loadDifficulty-luokan tiedostonluku ja arvojen palautus GUI:lle ei ollut ihan optimaalinen.

En ehtinyt perehtyä Googlen API:n käyttöön tarpeeksi huolellisesti, ja tämän takia käyttäjän todennus on hyvin kyseenalainen. Koska tietoturva ei kuitenkaan ole arvosteltava asia, niin 
jätin asian sikseen. (Ja tämän takia siis tarvitaan sekä tiedosto credentials.json että tokens/StoredCredentials).
</p>

