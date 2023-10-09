package pjoz.advert.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "adverts")
@Builder
@AllArgsConstructor
@Data
@Getter
@Setter
public class Advert {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private int userId;
    private int price;
    private LocalDateTime creationDate;
    private String title;
    private String description;
    private String contactDetails;
}
