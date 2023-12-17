package pjoz.advert.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pjoz.advert.model.Advert;
import pjoz.advert.model.AdvertRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AdvertService {
    private final AdvertRepository advertRepository;

    public void saveAdvert(Advert advert) {
        Advert advertToSave = Advert.builder()
                .price(advert.getPrice())
                .title(advert.getTitle())
                .creationDate(LocalDateTime.now())
                .description(advert.getDescription())
                .contactDetails(advert.getContactDetails())
                .userId(advert.getUserId())
                .userName(advert.getUserName())
                .build();

        advertRepository.save(advertToSave);
    }

    public Optional<Integer> updateAdvert(Advert advert, int advertId) {
        Optional<Advert> advertOptional = advertRepository.findById(advertId);
        if (advertOptional.isPresent()) {
            Advert advertToUpdate = advertOptional.get();
            advertToUpdate.setTitle(advert.getTitle());
            advertToUpdate.setPrice(advert.getPrice());
            advertToUpdate.setDescription(advert.getDescription());
            advertToUpdate.setContactDetails(advert.getContactDetails());
            return Optional.of(advertRepository.saveAndFlush(advertToUpdate).getId());
        }
        return Optional.empty();
    }

    public boolean deleteAdvert(int id) {
        Optional<Advert> advertOptional = advertRepository.findById(id);
        if (advertOptional.isPresent()) {
            advertRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
