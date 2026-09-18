# DnD Character Manager

Aplikacja webowa do tworzenia i zarządzania postaciami do gry D&D (zasady SRD 2024).
Prowadzi użytkownika przez kreator postaci krok po kroku (klasa, umiejętności, cechy, tło, ekwipunek),
a dane z gry (klasy, tła, gatunki, przedmioty, typy obrażeń) są importowane bezpośrednio z API open5e.

## Jaki problem rozwiązuje

Ręczne prowadzenie karty postaci D&D na papierze jest żmudne - trzeba pamiętać o modyfikatorach,
biegłościach, punktach życia i wszelakich bonusach np. z gatunku. Aplikacja liczy to automatycznie i pokazuje
gotową kartę postaci, korzystając z danych SRD 2024.

## Technologie

- Java 17, Spring Boot 4.1.1
- Spring Security (logowanie sesyjne, role USER/ADMIN)
- Spring Data JPA + Hibernate, MySQL
- Thymeleaf (widoki)
- Jackson
- Maven

## Konfiguracja

Ustawienia w `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/Dnd_Character_Manager?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=twoje_haslo
spring.jpa.hibernate.ddl-auto=update
server.port=8080
```

## Jak uruchomić

1. **Dostęp do API open5e w ApiClient.java pod `http://127.0.0.1:8000/v2`.**
   Ewentualnie w razie gdyby server nie był dostępny, lokalnie, w Dockerze:
   ```bash
   docker run -d -p 8000:8888 --name open5e-api open5e-api:latest
   ```

2. **Uruchom MySQL** i upewnij się, że dane logowania zgadzają się z `application.properties`.

3. **Zbuduj i uruchom aplikację**

4. Zarejestruj się pod `http://localhost:8080/user/register`.

5. **Zaimportuj dane z open5e** (wymaga roli ADMIN - zarejestrowany użytkownik
   ma domyślnie rolę USER, więc trzeba ją ręcznie ustawić w bazie):
   ```
   GET http://localhost:8080/api/import/all
   ```
   Importuje klasy, subklasy, cechy klas, tła, gatunki, przedmioty i typy obrażeń.

6. Zaloguj i stwórz pierwszą postać.

## Przykładowe endpointy

| Metoda | URL | Opis | Dostęp |
|---|---|---|---|
| GET | `/home` | Lista postaci zalogowanego użytkownika | zalogowany |
| GET | `/avatar/create` | Start kreatora nowej postaci | zalogowany |
| GET | `/avatar/select/{id}/showcard` | Karta postaci | zalogowany |
| POST | `/avatar/{id}/edit` | Edycja HP/złota/ekwipunku na karcie | zalogowany |
| GET | `/api/classes` | Lista zaimportowanych klas (JSON) | ADMIN |
| GET | `/api/equipment` | Lista przedmiotów (JSON) | ADMIN |
| GET | `/api/import/all` | Import wszystkich danych z open5e | ADMIN |

### Autoryzacja

Endpointy `/api/**` wymagają roli **ADMIN** i logowania sesyjnego - najpierw zaloguj się
przez `/login`, przeglądarka/klient HTTP musi zachować ciasteczko sesji z tego żądania
i wysyłać je dalej.

Reszta aplikacji (`/avatar/**`, `/home`, `/user/**`) to zwykłe strony Thymeleaf, nie REST API -
z nich korzysta się przez przeglądarkę, nie przez wywołania HTTP z zewnątrz.

## Dalszy rozwój

Projekt jest rozwijany dalej, dalsze plany obejmują system rzucania zaklęć (spell slots) i
wyliczanie Armor Class na podstawie noszonej zbroi.
