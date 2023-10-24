package pjoz.advert.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import pjoz.advert.dto.UserDto;

import java.util.Optional;

@FeignClient("USER-SERVICE")
public interface UserClient {
    @GetMapping(value = "/api/user")
    public Optional<UserDto> getLoggedInUserDetails();
}