# Cobiss - Hackathon
> Aplikacija za popis zaloge knjižničnega gradiva

## Naloga

* Cilj naloge je razviti program, ki čim hitreje prečita seznam pripravljenih črtnih kod, ki ga prejme vsak udeleženec.
* Prototipno aplikacijo je treba razviti za platformo Android.
    * Razvojno orodje je poljubno:
        * Android Studio, Flutter, Java, Kotlin …
* Aplikacija ima na osnovni strani enega ali dva gumba, ki sprožita začetek in konec inventure (npr. gumba **START** in **STOP**).
* Ko se začne inventura, aplikacija kliče metodo `REST ("/start")`.
    * Metoda zbriše seznam že skeniranih črtnih kod.
* Kamera začne skenirati črtne kode.
    * Za vsako prebrano številko se pokliče metoda `REST ("/scan/{number}")`:
        * metoda prikaže status OK ali LOANED (če je knjiga izposojena);
        * metoda prikaže napako DUPLICATE, če smo številko že skenirali;
        * metoda prikaže napako NOT_EXIST, če se prebere številka, ki ne obstaja.
* Ko uporabnik zaključi inventuro in pritisne gumb **STOP**, aplikacija kliče metodo `REST ("/stop")`.
    * Če niso prebrane vse črtne kode, se prikaže napaka NOT_ALL_READ.
    * Uporabnik mora nadaljevati skeniranje ali pa začeti novo inventuro (gumb **START**).

### Črtne kode

* Code 93
    * 391234501
    * 999999999,9
* UPC A (odrezati kontrolko)
    * 00000033116

### Struktura odgovora
* Vrača se JSON-strukturo z dvema atributoma:
    * status (celo število (integer))
        * OK = 0;
        * NOT_EXIST = 10; // scan
        * DUPLICATE = 11; // scan
        * LOANED = 12; // scan
        * NOT_STARTED = 20; // scan
        * NOT_ALL_READ = 21; // stop
        * INVALID_APIKEY = 30;
        * INVALID_TOKEN = 31;
    * sporočilo

## Uporabniške zahteve

* Ker knjižničar med inventuro ne bo gledal na zaslon, je treba predvajati zvočni posnetek:
    * ok.wav – če je črtna koda uspešno razpoznana in zabeležena na strežniku;
    * izposojeno.wav – če je gradivo izposojeno;
    * duplikat.wav – če je dvakrat prečitana ista številka;
    * napaka.wav – če strežnik ne deluje ali številka ne obstaja.
* Prepoznana številka naj se izpiše na zaslonu.
* Prepoznavanje oz. branje črtne kode mora biti **čim hitrejše**
    * najti inovativne metode/mehanizme za povečanje hitrosti branja.
