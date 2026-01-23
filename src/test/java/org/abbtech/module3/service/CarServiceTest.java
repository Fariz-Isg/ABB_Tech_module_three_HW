package org.abbtech.module3.service;

import org.abbtech.module3.model.Car;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CarService Unit Tests")
class CarServiceTest {

    private CarService carService;
    private List<Car> initialCars;

    @BeforeEach
    void setUp() {
        initialCars = new ArrayList<>();
        initialCars.add(Car.builder()
                .id(1L)
                .name("BMW")
                .color("red")
                .year(2000)
                .price(15000)
                .build());
        initialCars.add(Car.builder()
                .id(2L)
                .name("Ford")
                .color("white")
                .year(2005)
                .price(12000)
                .build());
        initialCars.add(Car.builder()
                .id(3L)
                .name("Toyota")
                .color("blue")
                .year(2010)
                .price(18000)
                .build());

        carService = new CarService(initialCars);
    }

    @Test
    @DisplayName("Should return all cars")
    void testGetAll() {
        List<Car> cars = carService.getAll();

        assertNotNull(cars);
        assertEquals(3, cars.size());
        assertEquals("BMW", cars.get(0).getName());
        assertEquals("Ford", cars.get(1).getName());
        assertEquals("Toyota", cars.get(2).getName());
    }

    @Test
    @DisplayName("Should return car by existing ID")
    void testGetByIdWhenCarExists() {
        Optional<Car> car = carService.getById(1L);

        assertTrue(car.isPresent());
        assertEquals("BMW", car.get().getName());
        assertEquals("red", car.get().getColor());
        assertEquals(2000, car.get().getYear());
        assertEquals(15000, car.get().getPrice());
    }

    @Test
    @DisplayName("Should return empty when car ID does not exist")
    void testGetByIdWhenCarDoesNotExist() {
        Optional<Car> car = carService.getById(999L);

        assertFalse(car.isPresent());
    }

    @Test
    @DisplayName("Should save a new car")
    void testSave() {
        Car newCar = Car.builder()
                .name("Mercedes")
                .color("black")
                .year(2020)
                .price(30000)
                .build();

        int initialSize = carService.getAll().size();
        carService.save(newCar);

        assertEquals(initialSize + 1, carService.getAll().size());

        Car savedCar = carService.getAll().get(initialSize);
        assertEquals("Mercedes", savedCar.getName());
        assertEquals("black", savedCar.getColor());
        assertEquals(2020, savedCar.getYear());
        assertEquals(30000, savedCar.getPrice());
        assertNotNull(savedCar.getId());
    }

    @Test
    @DisplayName("Should update an existing car")
    void testUpdate() {
        Car updatedCar = Car.builder()
                .name("BMW Updated")
                .color("black")
                .year(2021)
                .price(20000)
                .build();

        Optional<Car> result = carService.update(1L, updatedCar);

        assertTrue(result.isPresent());
        assertEquals("BMW Updated", result.get().getName());
        assertEquals("black", result.get().getColor());
        assertEquals(2021, result.get().getYear());
        assertEquals(20000, result.get().getPrice());
    }

    @Test
    @DisplayName("Should delete a car by ID")
    void testDelete() {
        int initialSize = carService.getAll().size();

        carService.delete(2L);

        assertEquals(initialSize - 1, carService.getAll().size());
        assertFalse(carService.getById(2L).isPresent());
    }

    @Test
    @DisplayName("Should not throw exception when deleting non-existent car")
    void testDeleteNonExistentCar() {
        int initialSize = carService.getAll().size();

        assertDoesNotThrow(() -> carService.delete(999L));
        assertEquals(initialSize, carService.getAll().size());
    }

    @Test
    @DisplayName("Should generate sequential IDs for new cars")
    void testIdGeneration() {
        Car car1 = Car.builder()
                .name("Car1")
                .color("red")
                .year(2020)
                .price(10000)
                .build();

        Car car2 = Car.builder()
                .name("Car2")
                .color("blue")
                .year(2021)
                .price(15000)
                .build();

        carService.save(car1);
        carService.save(car2);

        List<Car> allCars = carService.getAll();
        Car savedCar1 = allCars.get(allCars.size() - 2);
        Car savedCar2 = allCars.get(allCars.size() - 1);

        assertNotNull(savedCar1.getId());
        assertNotNull(savedCar2.getId());
        assertTrue(savedCar2.getId() > savedCar1.getId());
    }
}