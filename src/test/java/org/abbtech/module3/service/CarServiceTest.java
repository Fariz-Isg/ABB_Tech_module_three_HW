package org.abbtech.module3.service;

import org.abbtech.module3.dto.CarRequestDto;
import org.abbtech.module3.dto.CarResponseDto;
import org.abbtech.module3.exception.CarErrorEnum;
import org.abbtech.module3.exception.CarException;
import org.abbtech.module3.logger.LoggerService;
import org.abbtech.module3.model.Brand;
import org.abbtech.module3.model.Car;
import org.abbtech.module3.model.Model;
import org.abbtech.module3.repository.CarRepository;
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
class CarServiceTest {

    @Mock
    private CarRepository carRepository;

    @Mock
    private ModelRepository modelRepository;

    @Mock
    private LoggerService loggerService;

    @InjectMocks
    private CarService carService;

    private Brand brand;
    private Model model;
    private Car car;
    private CarRequestDto requestDto;

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
                .build();

        car = Car.builder()
                .id(1L)
                .color("Red")
                .year(2023)
                .price(30000.0)
                .speed(200)
                .vinNumber("VIN123456")
                .model(model)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        requestDto = new CarRequestDto(1L, "Red", 2023, 30000.0, 200, "VIN123456");
    }

    @Test
    void getAllCars_ShouldReturnAllCars() {
        List<Car> cars = Arrays.asList(car);
        when(carRepository.findAll()).thenReturn(cars);

        List<CarResponseDto> result = carService.getAllCars();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Red", result.get(0).color());
        verify(carRepository, times(1)).findAll();
    }

    @Test
    void getCarById_WhenCarExists_ShouldReturnCar() {
        when(carRepository.findById(1L)).thenReturn(Optional.of(car));

        CarResponseDto result = carService.getCarById(1L);

        assertNotNull(result);
        assertEquals("Red", result.color());
        assertEquals("Camry", result.modelName());
        assertEquals("Toyota", result.brandName());
        verify(carRepository, times(1)).findById(1L);
    }

    @Test
    void getCarById_WhenCarNotFound_ShouldThrowException() {
        when(carRepository.findById(999L)).thenReturn(Optional.empty());

        CarException exception = assertThrows(CarException.class,
                () -> carService.getCarById(999L));

        assertEquals(CarErrorEnum.CAR_NOT_FOUND, exception.baseErrorService);
        verify(carRepository, times(1)).findById(999L);
    }

    @Test
    void getCarsByModelId_ShouldReturnCars() {
        List<Car> cars = Arrays.asList(car);
        when(carRepository.findByModelId(1L)).thenReturn(cars);

        List<CarResponseDto> result = carService.getCarsByModelId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(carRepository, times(1)).findByModelId(1L);
    }

    @Test
    void createCar_WhenModelExists_ShouldCreateCar() {
        when(modelRepository.findById(1L)).thenReturn(Optional.of(model));
        when(carRepository.save(any(Car.class))).thenReturn(car);

        CarResponseDto result = carService.createCar(requestDto);

        assertNotNull(result);
        assertEquals("Red", result.color());
        verify(modelRepository, times(1)).findById(1L);
        verify(carRepository, times(1)).save(any(Car.class));
    }

    @Test
    void createCar_WhenModelNotFound_ShouldThrowException() {
        when(modelRepository.findById(999L)).thenReturn(Optional.empty());
        CarRequestDto dto = new CarRequestDto(999L, "Red", 2023, 30000.0, 200, "VIN123");

        CarException exception = assertThrows(CarException.class,
                () -> carService.createCar(dto));

        assertEquals(CarErrorEnum.MODEL_NOT_FOUND, exception.baseErrorService);
        verify(modelRepository, times(1)).findById(999L);
        verify(carRepository, never()).save(any(Car.class));
    }

    @Test
    void updateCar_WhenCarAndModelExist_ShouldUpdateCar() {
        when(carRepository.findById(1L)).thenReturn(Optional.of(car));
        when(modelRepository.findById(1L)).thenReturn(Optional.of(model));
        when(carRepository.save(any(Car.class))).thenReturn(car);

        CarResponseDto result = carService.updateCar(1L, requestDto);

        assertNotNull(result);
        verify(carRepository, times(1)).findById(1L);
        verify(modelRepository, times(1)).findById(1L);
        verify(carRepository, times(1)).save(any(Car.class));
    }

    @Test
    void updateCar_WhenCarNotFound_ShouldThrowException() {
        when(carRepository.findById(999L)).thenReturn(Optional.empty());

        CarException exception = assertThrows(CarException.class,
                () -> carService.updateCar(999L, requestDto));

        assertEquals(CarErrorEnum.CAR_NOT_FOUND, exception.baseErrorService);
        verify(carRepository, times(1)).findById(999L);
    }

    @Test
    void updateCar_WhenModelNotFound_ShouldThrowException() {
        when(carRepository.findById(1L)).thenReturn(Optional.of(car));
        when(modelRepository.findById(999L)).thenReturn(Optional.empty());
        CarRequestDto dto = new CarRequestDto(999L, "Blue", 2023, 35000.0, 220, "VIN789");

        CarException exception = assertThrows(CarException.class,
                () -> carService.updateCar(1L, dto));

        assertEquals(CarErrorEnum.MODEL_NOT_FOUND, exception.baseErrorService);
        verify(carRepository, times(1)).findById(1L);
        verify(modelRepository, times(1)).findById(999L);
    }

    @Test
    void deleteCar_WhenCarExists_ShouldDeleteCar() {
        when(carRepository.existsById(1L)).thenReturn(true);

        carService.deleteCar(1L);

        verify(carRepository, times(1)).existsById(1L);
        verify(carRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteCar_WhenCarNotFound_ShouldThrowException() {
        when(carRepository.existsById(999L)).thenReturn(false);

        CarException exception = assertThrows(CarException.class,
                () -> carService.deleteCar(999L));

        assertEquals(CarErrorEnum.CAR_NOT_FOUND, exception.baseErrorService);
        verify(carRepository, times(1)).existsById(999L);
        verify(carRepository, never()).deleteById(anyLong());
    }
}