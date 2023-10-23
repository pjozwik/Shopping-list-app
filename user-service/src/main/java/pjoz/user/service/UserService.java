package pjoz.user.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pjoz.user.dto.UserDto;
import pjoz.user.model.MyUserDetails;
import pjoz.user.model.User;
import pjoz.user.model.UserRepository;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void saveUser(User user){
        User userToSave = User.builder()
                .name(user.getName())
                .userName(user.getUserName())
                .surname(user.getSurname())
                .email(user.getEmail())
                .password(bCryptPasswordEncoder.encode(user.getPassword()))
                .roles(user.getRoles())
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

    public Optional<UserDto> getUserByUserName(String name) {
        Optional<User> user = userRepository.findByUserName(name);
        return user.map(this::mapUserToDTO);
    }

    public UserDto mapUserToDTO(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUserName())
                .roles(user.getRoles().split(","))
                .build();
    }
}
