package pjoz.user.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pjoz.user.dto.UserDto;
import pjoz.user.model.User;
import pjoz.user.model.UserRepository;
import pjoz.user.service.UserService;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    @PostMapping(value = "/register")
    public ResponseEntity<String> crateNewUser(@RequestBody User user){
        //TODO check if user already exists
        userService.saveUser(user);
        return ResponseEntity.ok("User has been successfully added");
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<User> getUserById(@PathVariable int id){
        return ResponseEntity.of(userRepository.findById(id));
    }

    @GetMapping(value = "/loggedUser")
    public Optional<UserDto> getLoggedInUserDetails(){
        var auth = SecurityContextHolder.getContext().getAuthentication();
        return userService.getUserByUserName(auth.getName());
    }

}
