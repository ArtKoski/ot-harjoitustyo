## **Osteroids**

Aiheena on Asteroids-tyyppinen selviytymispeli.

### Huom 
Kaikissa napeissa ei ole vielä haluttua toiminnallisuutta.
Ohjeet peliin löytyvät itse sovelluksesta.

### **Dokumentaatio**
[Vaatimuusmäärittely](https://github.com/ArtKoski/ot-harjoitustyo/blob/master/Osteroids/dokumentaatio/maarittelydokumentti.md)  
[Työaikakirjanpito](https://github.com/ArtKoski/ot-harjoitustyo/blob/master/Osteroids/dokumentaatio/tuntikirjanpito.md)  
[Arkkitehtuurikuvaus](https://github.com/ArtKoski/ot-harjoitustyo/blob/master/Osteroids/dokumentaatio/arkkitehtuuri.md)
[Käyttöohje](https://github.com/ArtKoski/ot-harjoitustyo/blob/master/Osteroids/dokumentaatio/kayttoohje.md)

### Releaset
[Viikko 6](https://github.com/ArtKoski/ot-harjoitustyo/releases/tag/viikko6)


### Komentorivitoiminnot
#### Testit: 
```
mvn test
```
```
mvn jacoco:report
```
Luo raportin kohteeseen target/site/jacoco/index.html

#### Jarin generointi:
```
mvn package
```
Luo jar-tiedoston target-hakemistoon

#### Javadoc && Checkstyle
```
mvn javadoc:javadoc
```
Luo javadocin jonka löytää kohteesta target/site/apidocs/index.html

```
mvn jxr:jxr checkstyle:checkstyle
```
Checkstyle-tarkistus, johon sisältyy javadoc-tarkistukset
