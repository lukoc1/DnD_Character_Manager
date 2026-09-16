package pl.visa.dndCM.user;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
public class UserAdminController {

    private final UserService userService;

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {

        UserDTO userDTO = userService.findById(id);

        if (userDTO.getRole() == Role.ADMIN) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Cannot delete admin account.");
        }

        userService.deleteUserById(id);

        return ResponseEntity.noContent().build();
    }
}
