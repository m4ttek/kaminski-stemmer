# kaminski-stemmer
### Opis

Kaminski-stemmer to aplikacja i jednocześnie biblioteka pełniąca rolę procesora tekstowego, 
której zadanie jest przeprowadzenie stemmingu dowolnego tekstu.
Pozwala na wykorzystanie dowolnego słownika dla reguł stemujących. 
Zadaniem stemmera jest przetworzenie dowolnego tekstu (zwłaszcza słów w nim występujących) w dowolnym języku
do tekstu wynikowego składającego się z słów - termów - sprowadzonych do prostej formy 
(np. dla czasownika będzie to formie bezokolicznika).

Domyślnie, kaminski-stemmer wykorzystuje słownik morfologiczny języka polskiego o nazwie PoliMorf
http://zil.ipipan.waw.pl/PoliMorf (połączenie SGJP + Morfologik).
Stemmer może być wykorzystywany jako niezależna aplikacja bądź wchodzić w skład istniejących aplikacji implementowanych 
w językach bazujących na wirtualnej maszynie javy jako dołączona biblioteka.
Dla maksymalnej wydajności przetwarzania zalecana jest druga opcja, gdzie aplikacja utrzymuje w pamięci
strukturę danych wymaganą do działania procesu stemmingu.

Aplikacja kaminski-stemmer dostarcza mechanizmu konwersji słownika w dowolnym języku na wewnętrzną reprezentację
dla stemmera. Domyślnie stemmer posiada wbudowany słownik języka polskiego i potrafi właśnie w tym języku
procesować tekst.

Kaminski-stemmer może z powodzeniem być wykorzystywany w systemach wykorzystujących Natural Language Processing (NLP),
czyli w systemach przetwarzających tekst naturalny.


##### Charakterystyka

W aktualnej implementacji, słownik jest reprezentowany w postaci
struktury danych o nazwie Patricia Tree, po polsku - w skompresowanym drzewie trie. Taka struktura jest
kompromisem wydajności wyszukiwania ciągów znaków do wymagań pamięci. 
Każdy kolejny znak może być oddzielnym węzłem, gdzie węzeł odpowiada formie prostej danego termu.

Kaminski-stemmer jest stemmerem, który opiera normalizację termów na uprzednio zdefiniowanym słowniku reguł.
W odróżnieniu od języka angielskiego, język polski jest fleksyjny. Praktycznie niemożliwe jest opracowanie
algorytmu stemującego tekst w języku fleksyjnym bez słownika morfologicznego i ortograficznego.

Innym waznym aspektem jest to, że stemmer normalizuje słowa bez rozróżnienia ich znaczenia w zdaniu. 
Z tego powodu znaczna ilość informacji może zostać utracona.

Kaminski-stemmer przetwarza tekst z pominięciem znaków interpunkcyjnych i znaków białych - pierwszym etapem działania
jest tokenizacja tekstu na pojedyncze. To zachowanie może być zmodyfikowane w przypadku wykorzystania stemmera jako bibliotekę.
Rezultatem tokenizacji jest podział tekstu na pojedyncze termy, gotowe do poddania się procesowi stemmingu.
Każdy term jest wyszukiwany w słowniku. Jeśli dla pewnego termu forma prosta nie zostanie znaleziona,
to jest stosowana funkcja "lowercase". W takiej formie term jest ponownie wyszukiwany w słowniku.

##### Wydajność

Wydajność aplikacji kaminski-stemmer została zmierzona dla różnych źródeł tekstowych. 
Dla porównania testy zostały przeprowadzone w identycznych przypadkach dla istniejącego już i rozwijanego przez wiele lat 
stemmera o nazwie morfologik http://www.github.com/morfologik/morfologik-stemming/wiki .

zbikowski-doktorat|98 ms|1979
pan_tadeusz|236 ms|9152
tadeusz-micinski-nauczycielka|25 ms|3608
sztuka-zdobywania-pieniedzy|8 ms|1198
ogniem-i-mieczem|355 ms|13002

zbikowski-doktorat|292 ms|4708
pan_tadeusz|331 ms|13520
tadeusz-micinski-nauczycielka|38 ms|4377
sztuka-zdobywania-pieniedzy|10 ms|1428
ogniem-i-mieczem|529 ms|20394

### Uruchomienie

Poniższa instrukcja zakłada uruchomienie aplikacji na dowolnym systemie operacyjnym UNIX-pochodnym.
Dla systemów Windows kroki wyglądają identycznie, należy jednak zwrócić uwagę na różnice w obsłudze dostępnego 
terminala.

##### Przygotowanie do uruchomienia
Przed uruchomieniem aplikacji należy upewnić się, że posiadamy zainstalowaną wirtualną 
maszynę javy co najmniej w wersji 8. W systemie powinna być zdefiniowana zmienna JAVA_HOME wskazująca
na miejsce instalacji Javy.
```bash
$ echo $JAVA_HOME
```
Aplikacja bazuje na systemie budowania o nazwie gradle. Nie jest jednak potrzebna jego instalacja
ze względu na zastosowanie tzw. wrappera, który autoamtycznie pobierze z Internetu wszystkie
potrzebne zasoby do zbudowania stemmera.

##### Szybkie uruchomienie
Pierwsze uruchomienie aplikacji może trwać dłużej ze względu na automatyczne pobranie
niezbędnych bibliotek do zbudowania kodu aplikacji i jej działania.

Stemmer możemy uruchomić następująco (zakładajac plik źródłowy `example` zawierający tekst w języku polskim):
```bash
$ ./runner.sh -stem -source ./example -dest ./stemmed-example
```

##### Problemy
Jeżeli w wyniku uruchomienia otrzymamy następujący komunikat:
```bash
Exception in thread "main" java.lang.UnsupportedClassVersionError: com/mkaminski/stemmer/KaminskiStemmerRunner has been compiled by a more recent version of the Java Runtime (class file version 52.0), this version of the Java Runtime only recognizes class file versions up to 52.0
```
W takim przypadku zmienna środowiskowa `JAVA_HOME` nie została poprawnie skonfigurowana i nie
wskazuje na oprogramowanie `Java` w wersji 8.

##### Konwersja słownika
Aplikacja kaminski-stemmer umożliwia konwersję dowolnego słownika w formie pliku TSV do wewnętrznej postaci,
wykorzystywanej do procesu stemmingu. Plik TSV to plik CSV gdzie kolejne wartości oddzielone są znakiem tabularycznym.
Pierwsza kolumna pliku TSV powinna zawierać termy w formie dowolnej, druga natomiast w formie prostej.
```bash
$ ./runner.sh -convert -source ./dict.tsv -dest ./dict.ser
$ ./runner.sh -stem -dict ./dict.ser -source ./example -dest ./stemmed-example
```

### Dalszy rozwój stemmera

##### Wydajność wczytywania słownika

Mimo usilnych starań wyeliminowania tego problemu, wczytanie ogromnego słownika z formy serializowalnej 
(ok. 122MB dla języka polskiego) do obiektu w pamięci maszyny wirtualnej jest czasochłonnym zadaniem 
dla komputerów klasy PC. Stąd dla maksymalnej wydajności rekomenduje się wykorzystanie kaminski-stemmer
jako biblioteki, gdzie obiekt słownika może zostać wczytany do pamięci jednokrotnie i wykorzystywany
przez wielu "klientów".

##### Pluginy do systemów Information Retrieval - silniki Apache Solr i Elasticsearch
Silniki Apache Solr jak i Elasticsearch są otwarte na zewnętrzne narzędzia. Ich API wymaga implementacji
ustalonego interfejsu w celu wykorzystania nowego stemmera.


