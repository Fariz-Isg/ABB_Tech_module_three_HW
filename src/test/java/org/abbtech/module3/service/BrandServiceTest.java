package org.abbtech.module3.service;

import org.abbtech.module3.dto.BrandRequestDto;
import org.abbtech.module3.dto.BrandResponseDto;
import org.abbtech.module3.exception.CarErrorEnum;
import org.abbtech.module3.exception.CarException;
import org.abbtech.module3.logger.LoggerService;
import org.abbtech.module3.model.Brand;
import org.abbtech.module3.repository.BrandRepository;
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
class BrandServiceTest {

    @Mock
    private BrandRepository brandRepository;

    @Mock
    private LoggerService loggerService;

    @InjectMocks
    private BrandService brandService;

    private Brand brand;
    private BrandRequestDto requestDto;

    @BeforeEach
    void setUp() {
        brand = Brand.builder()
                .id(1L)
                .name("Toyota")
                .country("Japan")
                .foundedYear(1937)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        requestDto = new BrandRequestDto("Toyota", "Japan", 1937);
    }

    @Test
    void getAllBrands_ShouldReturnAllBrands() {
        List<Brand> brands = Arrays.asList(brand);
        when(brandRepository.findAll()).thenReturn(brands);

        List<BrandResponseDto> result = brandService.getAllBrands();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Toyota", result.get(0).name());
        verify(brandRepository, times(1)).findAll();
    }

    @Test
    void getBrandById_WhenBrandExists_ShouldReturnBrand() {
        when(brandRepository.findById(1L)).thenReturn(Optional.of(brand));

        BrandResponseDto result = brandService.getBrandById(1L);

        assertNotNull(result);
        assertEquals("Toyota", result.name());
        assertEquals("Japan", result.country());
        verify(brandRepository, times(1)).findById(1L);
    }

    @Test
    void getBrandById_WhenBrandNotFound_ShouldThrowException() {
        when(brandRepository.findById(999L)).thenReturn(Optional.empty());

        CarException exception = assertThrows(CarException.class,
                () -> brandService.getBrandById(999L));

        assertEquals(CarErrorEnum.BRAND_NOT_FOUND, exception.baseErrorService);
        verify(brandRepository, times(1)).findById(999L);
    }

    @Test
    void createBrand_WhenBrandDoesNotExist_ShouldCreateBrand() {
        when(brandRepository.existsByName("Toyota")).thenReturn(false);
        when(brandRepository.save(any(Brand.class))).thenReturn(brand);

        BrandResponseDto result = brandService.createBrand(requestDto);

        assertNotNull(result);
        assertEquals("Toyota", result.name());
        verify(brandRepository, times(1)).existsByName("Toyota");
        verify(brandRepository, times(1)).save(any(Brand.class));
    }

    @Test
    void createBrand_WhenBrandExists_ShouldThrowException() {
        when(brandRepository.existsByName("Toyota")).thenReturn(true);

        CarException exception = assertThrows(CarException.class,
                () -> brandService.createBrand(requestDto));

        assertEquals(CarErrorEnum.BRAND_ALREADY_EXISTS, exception.baseErrorService);
        verify(brandRepository, times(1)).existsByName("Toyota");
        verify(brandRepository, never()).save(any(Brand.class));
    }

    @Test
    void updateBrand_WhenBrandExists_ShouldUpdateBrand() {
        BrandRequestDto updateDto = new BrandRequestDto("Honda", "Japan", 1948);
        when(brandRepository.findById(1L)).thenReturn(Optional.of(brand));
        when(brandRepository.save(any(Brand.class))).thenReturn(brand);

        BrandResponseDto result = brandService.updateBrand(1L, updateDto);

        assertNotNull(result);
        verify(brandRepository, times(1)).findById(1L);
        verify(brandRepository, times(1)).save(any(Brand.class));
    }

    @Test
    void updateBrand_WhenBrandNotFound_ShouldThrowException() {
        when(brandRepository.findById(999L)).thenReturn(Optional.empty());

        CarException exception = assertThrows(CarException.class,
                () -> brandService.updateBrand(999L, requestDto));

        assertEquals(CarErrorEnum.BRAND_NOT_FOUND, exception.baseErrorService);
        verify(brandRepository, times(1)).findById(999L);
    }

    @Test
    void deleteBrand_WhenBrandExists_ShouldDeleteBrand() {
        when(brandRepository.existsById(1L)).thenReturn(true);

        brandService.deleteBrand(1L);

        verify(brandRepository, times(1)).existsById(1L);
        verify(brandRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteBrand_WhenBrandNotFound_ShouldThrowException() {
        when(brandRepository.existsById(999L)).thenReturn(false);

        CarException exception = assertThrows(CarException.class,
                () -> brandService.deleteBrand(999L));

        assertEquals(CarErrorEnum.BRAND_NOT_FOUND, exception.baseErrorService);
        verify(brandRepository, times(1)).existsById(999L);
        verify(brandRepository, never()).deleteById(anyLong());
    }
}