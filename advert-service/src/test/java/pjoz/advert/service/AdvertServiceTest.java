package pjoz.advert.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pjoz.advert.model.Advert;
import pjoz.advert.model.AdvertRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdvertServiceTest {

    @Mock
    private AdvertRepository advertRepository;

    @InjectMocks
    private AdvertService advertService;
    @Test
    void shouldSaveAdvert() throws Exception {

        Advert advert = new Advert();
        when(advertRepository.save(any())).thenReturn(advert);

        advertService.saveAdvert(advert);

        verify(advertRepository, times(1)).save(any());
    }

    @Test
    void shouldUpdateAdvert() throws Exception {
        int advertId = 0;
        Advert advert = new Advert();
        when(advertRepository.findById(advertId)).thenReturn(Optional.of(advert));
        when(advertRepository.saveAndFlush(any())).thenReturn(advert);

        Optional<Integer> result = advertService.updateAdvert(advert, advertId);

        assertTrue(result.isPresent());
        verify(advertRepository, times(1)).findById(advertId);
        verify(advertRepository, times(1)).saveAndFlush(any());
    }

    @Test
    void shouldNotUpdateAdvertWhenAdvertNotFound() throws Exception {
        int advertId = 0;
        Advert advert = new Advert();
        when(advertRepository.findById(advertId)).thenReturn(Optional.empty());

        Optional result = advertService.updateAdvert(advert, advertId);

        assertFalse(result.isPresent());
        verify(advertRepository, times(1)).findById(advertId);
        verify(advertRepository, never()).saveAndFlush(any());
    }
    @Test
    void shouldDeleteAdvert() throws Exception {
        int advertId = 0;
        Advert advert = new Advert();
        when(advertRepository.findById(advertId)).thenReturn(Optional.of(advert));

        boolean result = advertService.deleteAdvert(advertId);

        assertTrue(result);
        verify(advertRepository, times(1)).deleteById(advertId);
    }

    @Test
    void shouldNotDeleteAdvertWhenAdvertNotFound() throws Exception {
        int advertId = 0;
        Advert advert = new Advert();
        when(advertRepository.findById(advertId)).thenReturn(Optional.empty());

        boolean result = advertService.deleteAdvert(advertId);

        assertFalse(result);
        verify(advertRepository, never()).deleteById(advertId);
    }
}
