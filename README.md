# kaminski-stemmer
### Opis

Kaminski-stemmer to elastyczny stemmer, który pozwala na wykorzystanie dowolnego słownika 
dla reguł stemujących. Zadaniem aplikacji stemmującej jest przetworzenie dowolnego 
tekstu (zwłaszcza słów w nim występujących) w dowolnym języku do tekstu składającego się z
termów sprowadzonych do jak najprostszej formy.

Kaminski-stemmer domyślnie wykorzystuje słownik morfologiczny języka polskiego o nazwie PoliMorf 
(SGJP + Morfologik).
Stemmer może być wykorzystywany jako niezależna aplikacja bądź wchodzić w skład istniejących aplikacji implementowanych 
w językach bazujących na wirtualnej maszynie javy jako dołączona biblioteka.

### Uruchomienie

##### Przygotowanie do uruchomienia
Przed uruchomieniem aplikacji należy upewnić się, że posiadamy zainstalowaną wirtualną 
maszynę javy w wersji 9. W systemie powinna być zdefiniowana zmienna JAVA_HOME wskazująca
na miejsce instalacji Javy.
```
$ echo $JAVA_HOME
```
Aplikacja bazuje na systemie budowania o nazwie gradle. Nie jest jednak potrzebna jego instalacja
ze względu na zastosowanie tzw. wrappera, który autoamtycznie pobierze z Internetu wszystkie
potrzebne zasoby do zbudowania stemmera.

##### Szybkie uruchomienie
Pierwsze uruchomienie aplikacji może trwać dłużej ze względu na automatyczne pobranie
niezbędnych bibliotek do zbudowania kodu aplikacji i jej działania.

Stemmer możemy uruchomić następująco (zakładajac plik źródłowy `example` zawierający tekst w języku polskim):
```
$ ./runner.sh -stem -source ./example -dest ./stemmed-example
```

##### Problemy
Jeżeli w wyniku uruchomienia otrzymamy następujący komunikat:
```
Exception in thread "main" java.lang.UnsupportedClassVersionError: com/mkaminski/stemmer/KaminskiStemmerRunner has been compiled by a more recent version of the Java Runtime (class file version 53.0), this version of the Java Runtime only recognizes class file versions up to 52.0
```
W takim przypadku zmienna środowiskowa `JAVA_HOME` nie została poprawnie skonfigurowana i nie
wskazuje na oprogramowanie `Java` w wersji 9.

## Dodatkowa konfiguracja
