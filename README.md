może z UUID trzeba kombinować bardziej

obrazu do uzycia mozna trzymac w resources w lokalnym folderze np home

mozna z zewnetrznego api zapisywac mozna nie zapisywac - moze byc ze raz dziennie pobiera - albo przy starcie aplikacji lub przy wywolaniu administratora
a tak jak to sa niezmienne dane to mozna zapisac w tabeli/lach (nawet lepiej)


https://api.open5e.com/v2

avatar == character -> postac grywalna 


param.error -> /home?error przykładowo

## TO DO:
* UUID zamiast ID
* może przy tworzeniu usera podwójnie wpisywane haslo?
* usunac pole ROLE u usera

* następnym krokiem będzie:
  * zczytanie z api przedmiotów
  * opcja przypisania przedmiotu do avatara
  * w sumie to my z damage-type tylko zczytujemyu jak sie nazywa, ale to wystarcza
    przy innych api nie będzie wystarczać

* jakoś inaczej odczytywać te dane o itemach bo to fest dużo dziwnego kodu
* W LOAD CONTROLLER TRZEBA W SERVISY I DTO ZAMIAST ENCJI
  http://localhost:8080/api/show/equipment/WEAPONS