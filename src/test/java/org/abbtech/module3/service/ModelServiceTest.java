package org.abbtech.module3.service;

import org.abbtech.module3.dto.ModelRequestDto;
import org.abbtech.module3.dto.ModelResponseDto;
import org.abbtech.module3.exception.CarErrorEnum;
import org.abbtech.module3.exception.CarException;
import org.abbtech.module3.logger.LoggerService;
import org.abbtech.module3.model.Brand;
import org.abbtech.module3.model.Model;
import org.abbtech.module3.repository.BrandRepository;
import org.abbtech.module3.repository.ModelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ModelServiceTest {

    @Mock
    private ModelRepository modelRepository;

    @Mock
    private BrandRepository brandRepository;

    @Mock
    private LoggerService loggerService;

    @InjectMocks
    private ModelService modelService;

    private Brand brand;
    private Model model;
    private ModelRequestDto requestDto;

    @BeforeEach
    void setUp() {
        brand = Brand.builder()
                .id(1L)
                .name("Toyota")
                .country("Japan")
                .foundedYear(1937)
                .build();

        model = Model.builder()
                .id(1L)
                .name("Camry")
                .modelYear(2023)
                .bodyType("Sedan")
                .brand(brand)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        requestDto = new ModelRequestDto("Camry", 1L, 2023, "Sedan");
    }

    @Test
    void getAllModels_ShouldReturnAllModels() {
        List<Model> models = Arrays.asList(model);
        when(modelRepository.findAll()).thenReturn(models);

        List<ModelResponseDto> result = modelService.getAllModels();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Camry", result.get(0).name());
        verify(modelRepository, times(1)).findAll();
    }

    @Test
    void getModelById_WhenModelExists_ShouldReturnModel() {
        when(modelRepository.findById(1L)).thenReturn(Optional.of(model));

        ModelResponseDto result = modelService.getModelById(1L);

        assertNotNull(result);
        assertEquals("Camry", result.name());
        assertEquals("Toyota", result.brandName());
        verify(modelRepository, times(1)).findById(1L);
    }

    @Test
    void getModelById_WhenModelNotFound_ShouldThrowException() {
        when(modelRepository.findById(999L)).thenReturn(Optional.empty());

        CarException exception = assertThrows(CarException.class,
                () -> modelService.getModelById(999L));

        assertEquals(CarErrorEnum.MODEL_NOT_FOUND, exception.baseErrorService);
        verify(modelRepository, times(1)).findById(999L);
    }

    @Test
    void getModelsByBrandId_ShouldReturnModels() {
        List<Model> models = Arrays.asList(model);
        when(modelRepository.findByBrandId(1L)).thenReturn(models);

        List<ModelResponseDto> result = modelService.getModelsByBrandId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(modelRepository, times(1)).findByBrandId(1L);
    }

    @Test
    void createModel_WhenBrandExists_ShouldCreateModel() {
        when(brandRepository.findById(1L)).thenReturn(Optional.of(brand));
        when(modelRepository.save(any(Model.class))).thenReturn(model);

        ModelResponseDto result = modelService.createModel(requestDto);

        assertNotNull(result);
        assertEquals("Camry", result.name());
        verify(brandRepository, times(1)).findById(1L);
        verify(modelRepository, times(1)).save(any(Model.class));
    }

    @Test
    void createModel_WhenBrandNotFound_ShouldThrowException() {
        when(brandRepository.findById(999L)).thenReturn(Optional.empty());
        ModelRequestDto dto = new ModelRequestDto("Test", 999L, 2023, "Sedan");

        CarException exception = assertThrows(CarException.class,
                () -> modelService.createModel(dto));

        assertEquals(CarErrorEnum.BRAND_NOT_FOUND, exception.baseErrorService);
        verify(brandRepository, times(1)).findById(999L);
        verify(modelRepository, never()).save(any(Model.class));
    }

    @Test
    void updateModel_WhenModelAndBrandExist_ShouldUpdateModel() {
        when(modelRepository.findById(1L)).thenReturn(Optional.of(model));
        when(brandRepository.findById(1L)).thenReturn(Optional.of(brand));
        when(modelRepository.save(any(Model.class))).thenReturn(model);

        ModelResponseDto result = modelService.updateModel(1L, requestDto);

        assertNotNull(result);
        verify(modelRepository, times(1)).findById(1L);
        verify(brandRepository, times(1)).findById(1L);
        verify(modelRepository, times(1)).save(any(Model.class));
    }

    @Test
    void updateModel_WhenModelNotFound_ShouldThrowException() {
        when(modelRepository.findById(999L)).thenReturn(Optional.empty());

        CarException exception = assertThrows(CarException.class,
                () -> modelService.updateModel(999L, requestDto));

        assertEquals(CarErrorEnum.MODEL_NOT_FOUND, exception.baseErrorService);
        verify(modelRepository, times(1)).findById(999L);
    }

    @Test
    void deleteModel_WhenModelExists_ShouldDeleteModel() {
        when(modelRepository.existsById(1L)).thenReturn(true);

        modelService.deleteModel(1L);

        verify(modelRepository, times(1)).existsById(1L);
        verify(modelRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteModel_WhenModelNotFound_ShouldThrowException() {
        when(modelRepository.existsById(999L)).thenReturn(false);

        CarException exception = assertThrows(CarException.class,
                () -> modelService.deleteModel(999L));

        assertEquals(CarErrorEnum.MODEL_NOT_FOUND, exception.baseErrorService);
        verify(modelRepository, times(1)).existsById(999L);
        verify(modelRepository, never()).deleteById(anyLong());
    }
}