package pjoz.advert.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import pjoz.advert.feign.UserClient;
import pjoz.advert.model.Advert;
import pjoz.advert.model.AdvertRepository;
import pjoz.advert.service.AdvertService;

import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(AdvertController.class)
public class AdvertControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdvertService advertService;

    @MockBean
    private AdvertRepository advertRepository;
    @MockBean
    private UserClient userClient;

    @Test
    void shouldReturnAdvertById() throws Exception {
        int advertId = 1;
        Advert mockAdvert = new Advert();
        when(advertRepository.findById(advertId)).thenReturn(Optional.of(mockAdvert));

        mockMvc.perform(get("/api/adverts/{id}", advertId))
                .andExpect(status().isOk());

        verify(advertRepository, times(1)).findById(advertId);
    }

    @Test
    void shouldReturn4xxWhenAdvertNotExists() throws Exception {
        int advertId = 1;
        Advert mockAdvert = new Advert();
        when(advertRepository.findById(advertId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/adverts/{id}", advertId))
                .andExpect(status().is4xxClientError());

        verify(advertRepository, times(1)).findById(advertId);
    }

    @Test
    void shouldReturnAllAdverts() throws Exception {

        List<Advert> mockAdverts = List.of(new Advert());
        when(advertRepository.findAll()).thenReturn(mockAdverts);

        mockMvc.perform(get("/api/adverts/all"))
                .andExpect(status().isOk());

        verify(advertRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnNoContentWhenAdvertsNotExists() throws Exception {

        List<Advert> mockAdverts = new ArrayList<>();
        when(advertRepository.findAll()).thenReturn(mockAdverts);

        mockMvc.perform(get("/api/adverts/all"))
                .andExpect(status().isNoContent());

        verify(advertRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnAllAdvertsByUserId() throws Exception {
        int userId = 1;
        List<Advert> mockAdverts = List.of(new Advert());
        when(advertRepository.findAllByUserId(userId)).thenReturn(Optional.of(mockAdverts));

        mockMvc.perform(get("/api/adverts/users/{id}", userId))
                .andExpect(status().isOk());

        verify(advertRepository, times(1)).findAllByUserId(userId);
    }

    @Test
    void shouldReturn4xxWhenAdvertsNotExistsByUserId() throws Exception {
        int userId = 1;

        when(advertRepository.findAllByUserId(userId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/adverts/users/{id}", userId))
                .andExpect(status().is4xxClientError());

        verify(advertRepository, times(1)).findAllByUserId(userId);
    }

    @Test
    void shouldCreateAdvert() throws Exception {

        doNothing().when(advertService).saveAdvert(any());
        mockMvc.perform(post("/api/adverts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Advert: null has been successfully added"));

        verify(advertService, times(1)).saveAdvert(any());
    }

    @Test
    void shouldDeleteAdvert() throws Exception {
        int advertId = 1;
        when(advertService.deleteAdvert(advertId)).thenReturn(true);

        mockMvc.perform(delete("/api/adverts/{id}", advertId))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));

        verify(advertService, times(1)).deleteAdvert(advertId);
    }

    @Test
    void shouldNotDeleteAdvertWhenAdvertNotFound() throws Exception {
        int advertId = 1;
        when(advertService.deleteAdvert(advertId)).thenReturn(false);

        mockMvc.perform(delete("/api/adverts/{id}", advertId))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Advert not found."));

        verify(advertService, times(1)).deleteAdvert(advertId);
    }

    @Test
    void shouldDeleteAllAdvertsByUserId() throws Exception {
        int userId = 1;
        List<Advert> mockAdverts = List.of(new Advert());
        when(advertRepository.findAllByUserId(userId)).thenReturn(Optional.of(mockAdverts));
        when(advertService.deleteAdvert(anyInt())).thenReturn(true);
        mockMvc.perform(delete("/api/adverts/user/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(content().string("Adverts deleted for a user: " + userId));

        verify(advertRepository, times(1)).findAllByUserId(userId);
        verify(advertService, times(mockAdverts.size())).deleteAdvert(anyInt());
    }

    @Test
    void shouldNotDeleteAllAdvertsByUserIdWhenUserNotFound() throws Exception {
        int userId = 1;

        when(advertRepository.findAllByUserId(userId)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/adverts/user/{id}", userId))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Adverts not found."));

        verify(advertRepository, times(1)).findAllByUserId(userId);
        verify(advertService, times(0)).deleteAdvert(anyInt());
    }
}
