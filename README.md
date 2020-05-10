## **Osteroids**

Aiheena on Asteroids-tyyppinen selviytymispeli.

### Javan versio

Sovelluksessa on koko kehityksen ajan käytetty Javan versiota 11.

### **Dokumentaatio**
[Vaatimuusmäärittely](https://github.com/ArtKoski/ot-harjoitustyo/blob/master/Osteroids/dokumentaatio/maarittelydokumentti.md)  
[Työaikakirjanpito](https://github.com/ArtKoski/ot-harjoitustyo/blob/master/Osteroids/dokumentaatio/tuntikirjanpito.md)  
[Arkkitehtuurikuvaus](https://github.com/ArtKoski/ot-harjoitustyo/blob/master/Osteroids/dokumentaatio/arkkitehtuuri.md)  
[Käyttöohje](https://github.com/ArtKoski/ot-harjoitustyo/blob/master/Osteroids/dokumentaatio/kayttoohje.md)  
[Testausdokumentti](https://github.com/ArtKoski/ot-harjoitustyo/blob/master/Osteroids/dokumentaatio/testausdokumentti.md)
### Releaset
[FinalRelease](https://github.com/ArtKoski/ot-harjoitustyo/releases/tag/loppupalautus)


### Komentorivitoiminnot


#### Testit: 
```
mvn test
```
```
mvn test jacoco:report
```
Luo raportin kohteeseen target/site/jacoco/index.html

#### Jarin generointi:
```
mvn package
```
Luo jar-tiedoston target-hakemistoon. Tarkemmat ohjeet suorittamiseen Käyttöohje-dokumentissa.

#### Javadoc && Checkstyle
```
mvn javadoc:javadoc
```
Luo javadocin jonka löytää kohteesta target/site/apidocs/index.html

```
mvn jxr:jxr checkstyle:checkstyle
```
Checkstyle-tarkistus, johon sisältyy javadoc-tarkistukset
