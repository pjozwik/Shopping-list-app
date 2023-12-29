package pjoz.advert.dto;

import lombok.*;

import javax.persistence.Column;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private int id;
    private String name;
    private String userName;
    private String surname;
    private String password;
    private String email;
    private String[] roles;
}

