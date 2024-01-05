package pjoz.user.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pjoz.user.model.User;
import pjoz.user.model.UserRepository;
import pjoz.user.service.UserService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    @GetMapping(value = "/{id}")
    public ResponseEntity<User> getUserById(@PathVariable int id){
        return ResponseEntity.of(userRepository.findById(id));
    }

    @GetMapping(value = "/loggedUser")
    public Optional<User> getLoggedInUserDetails(){
        var auth = SecurityContextHolder.getContext().getAuthentication();
        return userService.getUserByUserName(auth.getName());
    }
    @GetMapping("/all")
    public ResponseEntity<List<User>> getUsers() {
        List<User> users = userRepository.findAll();
        if(users.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(users);
    }
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable int id) {
        boolean isRemoved = userService.deleteUser(id);
        if (!isRemoved) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
        return new ResponseEntity<>(id, HttpStatus.OK);
    }
    @PutMapping("/{id}")
    ResponseEntity<Integer> updateUser(@RequestBody User user, @PathVariable Integer id) {
        return ResponseEntity.of(userService.updateUser(user, id));
    }
    @PostMapping(value = "/register")
    public ResponseEntity<String> crateNewUser(@RequestBody User user){
        userService.saveUser(user);
        return ResponseEntity.ok("User has been successfully added");
    }
}
