package pjoz.advert.controller;

import com.sun.xml.bind.v2.TODO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pjoz.advert.model.Advert;
import pjoz.advert.model.AdvertRepository;
import pjoz.advert.service.AdvertService;

import javax.ws.rs.Path;


@RestController("/api/adverts")
@AllArgsConstructor
public class AdvertController {

    private final AdvertRepository advertRepository;
    private final AdvertService advertService;

    @GetMapping("/{id}")
    ResponseEntity<Advert> getAdvertById(@PathVariable int id) {
        return ResponseEntity.of(advertRepository.findById(id));
    }

    @GetMapping("/users/{id}")
    ResponseEntity<Advert> getAdvertByUserId(@PathVariable int id) {
        //TODO: Fetch data from user service
        return ResponseEntity.of(advertRepository.findById(id));
    }

    @PostMapping
    ResponseEntity<String> createAdvert(@RequestBody Advert advert) {
        advertService.saveAdvert(advert);
        return ResponseEntity.ok("Advert: " + advert.getTitle() + " has been successfully added");
    }

    @PutMapping("/{id}")
    ResponseEntity<Integer> updateAdvert(@RequestBody Advert advert, @PathVariable Integer id) {
        return ResponseEntity.of(advertService.updateAdvert(advert, id));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteAdvert(@PathVariable int id) {
        boolean isRemoved = advertService.deleteAdvert(id);
        if (!isRemoved) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Advert not found.");
        }
        return new ResponseEntity<>(id, HttpStatus.OK);
    }
}
