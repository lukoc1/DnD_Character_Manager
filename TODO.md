# D&D Character Manager – plan rozwoju

## Założenia techniczne

- Backend: Java Spring Boot
- Komunikacja z API: WebClient
- Baza danych: MySQL + Hibernate/JPA
- Frontend: Thymeleaf (na start) 
- Aplikacja ma być przede wszystkim backendowa, a widoki są tylko warstwą prezentacji

## MVP

Projekt w pierwszej wersji powinien obejmować następujące obszary:

1. System użytkowników
   - rejestracja
   - logowanie
   - każdy użytkownik ma dostęp tylko do swoich postaci

2. Tworzenie postaci
   - wybór klasy
   - wybór rasy
   - nazwa postaci
   - poziom startowy

3. Karta postaci
   - imię
   - klasa
   - rasa
   - poziom
   - HP
   - statystyki
   - ekwipunek
   - znane zaklęcia

4. Ekwipunek
   - dodawanie przedmiotów
   - usuwanie przedmiotów
   - wyświetlanie posiadanych przedmiotów

5. Awans postaci
   - przycisk awansu
   - zwiększenie poziomu
   - dodanie dostępnych bonusów / zaklęć

6. Integracja z API
   - pobieranie danych o klasach, rasach, zaklęciach i przedmiotach
   - zapis tylko potrzebnych danych lokalnie w bazie

## Ograniczenia MVP

Z uwagi na ograniczony czas i wymaganą prostotę, w pierwszej wersji projekt ma zawierać tylko podstawowe mechaniki. Ograniczenia:

- tylko 3 klasy na start (np. Wojownik, Łotrzyk, Czarodziej)
- bez pełnej mechaniki walki, trudu i skomplikowanych reguł
- bez PDF na start
- bez „idealnie pełnej” implementacji wszystkich zasad D&D

## Architektura danych

### Dane w MySQL
Przechowywane lokalnie:
- użytkownicy
- postacie
- poziom postaci
- aktualne HP
- ekwipunek
- znane zaklęcia
- dane profilowe postaci

### Dane z API zewnętrznego
Dane pobierane i wykorzystywane z zewnętrznego API:
- opisy zaklęć
- cechy klas
- statystyki i właściwości klas
- przedmioty i ich parametry

## Struktura domeny

Najważniejsze encje / modele:

- User
  - id
  - username
  - password
  - role

- Avatar / Character
  - id
  - name
  - className
  - race
  - level
  - hp
  - userId

- EquipmentItem
  - id
  - name
  - description
  - quantity
  - avatarId

- Spell
  - id
  - name
  - description
  - level
  - avatarId

- CharacterProgression / LevelProgression
  - avatarId
  - currentLevel
  - xp / unlocked bonuses

## Warstwa backendowa

Backend ma być realizowany w następującej kolejności:

1. Model danych (encje + relacje)
2. Repozytoria (JPA Repository)
3. DTO
4. Serwisy
5. Kontrolery
6. UI (Thymeleaf)

## DTO

Na pewno będą potrzebne obiekty typu:

- UserRegistrationDTO
- UserLoginDTO
- AvatarCreateDTO
- AvatarResponseDTO
- EquipmentItemDTO
- SpellDTO
- LevelUpDTO

## Endpointy – rekomendowany układ

### Autoryzacja / użytkownik
- GET /login
- POST /login
- GET /register
- POST /users
- POST /logout

### Strona główna
- GET /home
- /home powinien wyświetlać listę postaci zalogowanego użytkownika

### Postacie
- GET /avatars/{id}
  - szczegóły postaci
- GET /avatars/{id}/sheet
  - karta postaci
- GET /avatars/{id}/edit
  - formularz edycji
- POST /avatars/{id}/edit
  - zapis edycji
- GET /avatars/new
  - formularz tworzenia postaci
- POST /avatars
  - zapis nowej postaci

### Ekwipunek
- GET /avatars/{id}/equipment
  - podgląd wyposażenia
- POST /avatars/{id}/equipment
  - dodanie przedmiotu
- DELETE /avatars/{id}/equipment/{itemId}
  - usunięcie przedmiotu

### Awans poziomu
- GET /avatars/{id}/level-up
  - formularz awansu
- POST /avatars/{id}/level-up
  - zapis awansu

## Wskazówki do nazw endpointów

Dobrze działać według zasady:

- /home = główna lista postaci
- /avatars/{id} = konkretna postać
- /avatars/{id}/sheet = karta postaci
- /avatars/{id}/edit = formularz edycji
- /avatars/{id}/equipment = ekwipunek
- /avatars/{id}/level-up = awans poziomu

Nie należy używać nazw typu:
- /addUser
- /showAvatar
- /doUpdate
- /handleLevelUp

Zamiast tego preferować nazwy zgodne z REST i semantyką zasobu.

## Kierunek architektury

Dobrze jest przyjąć taki model:

- Controller -> przyjmuje request
- Service -> zawiera logikę biznesową
- Repository -> pracuje z MySQL
- DTO -> przekazuje dane do widoku i wchodzące do backendu
- Thymeleaf -> renderuje widoki i formularze

Czyli frontend nie powinien być „centrum aplikacji”; backend ma decydować o stanie danych.

## Iteracyjny plan wdrożenia

### Iteracja 1 – użytkownik i postać
- rejestracja
- logowanie
- tworzenie postaci
- wyświetlenie listy postaci na /home

### Iteracja 2 – karta postaci
- dane podstawowe
- statystyki
- HP
- poziom
- podgląd karty w Thymeleaf

### Iteracja 3 – ekwipunek
- dodawanie i usuwanie przedmiotów
- wyświetlanie listy

### Iteracja 4 – zaklęcia i awans
- wybór zaklęć
- awans poziomu
- odświeżenie danych postaci

### Iteracja 5 – integracja z API
- pobieranie klas, ras, zaklęć i przedmiotów
- mapowanie danych z API do modelu aplikacji

## Co warto odrzucić na początek

- generowanie PDF
- pełna mechanika bitewna
- pełna rejestracja i uprawnienia w skali enterprise
- zbyt rozbudowana architektura “na przyszłość”
- zbyt duża liczba klas i ras przed testem MVP

## Podsumowanie

Najważniejsze jest zbudowanie małej, działającej, spójnej aplikacji backendowej, która pozwoli użytkownikowi:
- zalogować się,
- utworzyć postać,
- przejść do karty postaci,
- edytować ją,
- zarządzać ekwipunkiem,
- awansować,
- oraz wykorzystać dane z zewnętrznego API jako źródło dodatkowych informacji.

To daje dobry fundament pod dalszy rozwój bez przesady z architekturą i niepotrzebnym ryzykiem.




# wybór skili itd można przed modale może