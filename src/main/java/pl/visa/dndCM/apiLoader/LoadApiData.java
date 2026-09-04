//package pl.visa.dndCM.apiLoader;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//@Service
//public class LoadApiData {
//
//    private final RestTemplate restTemplate;
//    private final UserRepository userRepository;
//
//    @Autowired
//    public UserService(RestTemplate restTemplate, UserRepository userRepository) {
//        this.restTemplate = restTemplate;
//        this.userRepository = userRepository;
//    }
//
//    // Zapis pojedynczego obiektu
//    public User fetchAndSaveUser(String apiUrl) {
//        // 1. Pobranie danych jako ResponseEntity<User>
//        ResponseEntity<User> response = restTemplate.getForEntity(apiUrl, User.class);
//
//        // 2. Wyciągnięcie obiektu z ciała odpowiedzi
//        User user = response.getBody();
//
//        // 3. Zapis do bazy danych
//        if (user != null) {
//            return userRepository.save(user);
//        }
//        return null;
//    }
//
//    // Zapis listy obiektów (tablicy)
//    public void fetchAndSaveUserList(String apiUrl) {
//        // Zamiast kolekcji używamy tablicy, aby uniknąć problemów z erasure typów w Java
//        ResponseEntity<User[]> response = restTemplate.getForEntity(apiUrl, User[].class);
//
//        User[] users = response.getBody();
//
//        if (users != null) {
//            userRepository.saveAll(java.util.Arrays.asList(users));
//        }
//    }
//}