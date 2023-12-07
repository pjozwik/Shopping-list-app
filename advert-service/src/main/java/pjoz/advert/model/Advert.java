package pjoz.advert.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Max;
import java.time.LocalDateTime;

@Entity
@Table(name = "adverts")
@Builder
@AllArgsConstructor
@Data
@Getter
@Setter
@NoArgsConstructor
public class Advert {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private int userId;
    private int price;
    private LocalDateTime creationDate;
    private String title;
    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String description;
    private String contactDetails;
}
