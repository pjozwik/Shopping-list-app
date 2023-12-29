package pjoz.advert.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import pjoz.advert.dto.UserDto;

import java.util.Optional;

@FeignClient("USER-SERVICE")
public interface UserClient {
    @GetMapping(value = "/api/users/loggedUser")
    public Optional<UserDto> getLoggedInUserDetails();
}
