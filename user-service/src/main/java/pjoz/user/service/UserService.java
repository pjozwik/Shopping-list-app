package pjoz.user.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pjoz.user.model.MyUserDetails;
import pjoz.user.model.User;
import pjoz.user.model.UserRegistrationRequest;
import pjoz.user.model.UserRepository;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void saveUser(User user){
        User userToSave = User.builder()
                .name(user.getName())
                .userName(user.getUserName())
                .surname(user.getSurname())
                .email(user.getEmail())
                .password(user.getPassword())
                .build();

        userRepository.save(userToSave);
    }

    @Override
    public MyUserDetails loadUserByUsername(String username){
        Optional<User> user = userRepository.findByUserName(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("Not found: " + username);
        }
        return new MyUserDetails(user.get());
    }
}
