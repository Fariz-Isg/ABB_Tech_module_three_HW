package org.abbtech.module3.service;

import org.abbtech.module3.config.AppConfig;
import org.abbtech.module3.exception.CarException;
import org.abbtech.module3.logger.LoggerService;
import org.abbtech.module3.model.Car;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CarService Tests")
class CarServiceTest {

    @Mock
    private LoggerService loggerService;

    @Mock
    private AppConfig appConfig;

    @Mock
    private AppConfig.FeaturesConfig featuresConfig;

    private CarService carService;
    private List<Car> initialCars;

    @BeforeEach
    void setUp() {
        initialCars = new ArrayList<>();
        initialCars.add(Car.builder().id(1L).name("BMW").color("red").year(2000).price(15000).build());
        initialCars.add(Car.builder().id(2L).name("Ford").color("white").year(2005).price(12000).build());

        lenient().when(appConfig.getFeatures()).thenReturn(featuresConfig);
        lenient().when(featuresConfig.isDetailedLogging()).thenReturn(false);

        carService = new CarService(initialCars, loggerService, appConfig);
    }

    @Test
    @DisplayName("Should return all cars")
    void testGetAll() {
        List<Car> cars = carService.getAll();

        assertEquals(2, cars.size());
        assertEquals("BMW", cars.get(0).getName());
    }

    @Test
    @DisplayName("Should return car by ID")
    void testGetById() {
        Optional<Car> car = carService.getById(1L);

        assertTrue(car.isPresent());
        assertEquals("BMW", car.get().getName());
    }

    @Test
    @DisplayName("Should throw exception when car not found")
    void testGetByIdNotFound() {
        assertThrows(CarException.class, () -> carService.getById(999L));
    }

    @Test
    @DisplayName("Should save new car")
    void testSave() {
        Car newCar = Car.builder().name("Tesla").color("black").year(2024).price(50000).build();

        carService.save(newCar);

        assertEquals(3, carService.getAll().size());
    }

    @Test
    @DisplayName("Should throw exception when name is null")
    void testSaveWithNullName() {
        Car invalidCar = Car.builder().name(null).color("black").year(2024).price(50000).build();

        assertThrows(CarException.class, () -> carService.save(invalidCar));
    }

    @Test
    @DisplayName("Should update existing car")
    void testUpdate() {
        Car updatedCar = Car.builder().name("BMW Updated").color("blue").year(2022).price(20000).build();

        Optional<Car> result = carService.update(1L, updatedCar);

        assertTrue(result.isPresent());
        assertEquals("BMW Updated", result.get().getName());
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent car")
    void testUpdateNotFound() {
        Car updatedCar = Car.builder().name("Test").color("blue").year(2022).price(20000).build();

        assertThrows(CarException.class, () -> carService.update(999L, updatedCar));
    }

    @Test
    @DisplayName("Should delete car")
    void testDelete() {
        carService.delete(1L);

        assertEquals(1, carService.getAll().size());
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent car")
    void testDeleteNotFound() {
        assertThrows(CarException.class, () -> carService.delete(999L));
    }
}