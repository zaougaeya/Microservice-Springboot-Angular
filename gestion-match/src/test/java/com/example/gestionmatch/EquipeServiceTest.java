package com.example.gestionmatch.service;

import com.example.gestionmatch.model.Equipe;
import com.example.gestionmatch.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EquipeServiceTest {

    @Mock
    private EquipeRepository equipeRepository;

    @Mock
    private JoueurRepository joueurRepository;

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private CloudinaryService cloudinaryService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private EquipeService equipeService;

    private Equipe equipe;

    @BeforeEach
    void setup() {
        equipe = new Equipe();
        equipe.setId("1");
        equipe.setNameEquipe("Test FC");
        equipe.setLogo("logo_url");
    }

    @Test
    void testGetAllEquipes() {
        when(equipeRepository.findAll()).thenReturn(List.of(equipe));

        List<Equipe> equipes = equipeService.getAllEquipes();
        assertEquals(1, equipes.size());
        assertEquals("Test FC", equipes.get(0).getNameEquipe());
    }

    @Test
    void testGetEquipeById_Found() {
        when(equipeRepository.findById("1")).thenReturn(Optional.of(equipe));

        Equipe result = equipeService.getEquipeById("1");
        assertNotNull(result);
        assertEquals("Test FC", result.getNameEquipe());
    }

    @Test
    void testGetEquipeById_NotFound() {
        when(equipeRepository.findById("2")).thenReturn(Optional.empty());

        Equipe result = equipeService.getEquipeById("2");
        assertNull(result);
    }

    @Test
    void testCreateEquipe() throws IOException {
        MultipartFile mockFile = mock(MultipartFile.class);
        when(cloudinaryService.uploadImage(mockFile)).thenReturn("cloudinary_url");
        when(equipeRepository.save(any(Equipe.class))).thenAnswer(i -> i.getArgument(0));

        Equipe result = equipeService.createEquipe("New Team", mockFile);

        assertEquals("New Team", result.getNameEquipe());
        assertEquals("cloudinary_url", result.getLogo());
    }

    @Test
    void testUpdateEquipe_Found() {
        Equipe update = new Equipe();
        update.setNameEquipe("Updated FC");
        update.setLogo("new_logo");

        when(equipeRepository.findById("1")).thenReturn(Optional.of(equipe));
        when(equipeRepository.save(any(Equipe.class))).thenAnswer(i -> i.getArgument(0));

        Optional<Equipe> result = equipeService.updateEquipe("1", update);

        assertTrue(result.isPresent());
        assertEquals("Updated FC", result.get().getNameEquipe());
        assertEquals("new_logo", result.get().getLogo());
    }

    @Test
    void testUpdateEquipe_NotFound() {
        when(equipeRepository.findById("999")).thenReturn(Optional.empty());

        Optional<Equipe> result = equipeService.updateEquipe("999", new Equipe());

        assertFalse(result.isPresent());
    }

    @Test
    void testDeleteEquipe_NotExists() {
        when(equipeRepository.existsById("99")).thenReturn(false);

        boolean result = equipeService.deleteEquipe("99");

        assertFalse(result);
    }
}
