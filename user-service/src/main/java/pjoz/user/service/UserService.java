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

    public Optional<User> getUserByUserName(String name) {
        Optional<User> user = userRepository.findByUserName(name);
        return user;
    }

    public boolean deleteUser(int id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Optional<Integer> updateUser(User user, Integer id) {
        Optional<User> userToUpdate = userRepository.findById(id);
        if(userToUpdate.isPresent()){
            User updateUser = userToUpdate.get();
            updateUser.setUserName(user.getUserName());
            updateUser.setEmail(user.getEmail());
            updateUser.setName(user.getName());
            updateUser.setSurname(user.getSurname());
            updateUser.setRoles(user.getRoles());
            return Optional.of(userRepository.saveAndFlush(updateUser).getId());
        }
        return Optional.empty();
    }
}
