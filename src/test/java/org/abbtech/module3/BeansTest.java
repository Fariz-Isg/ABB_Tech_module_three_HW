package org.abbtech.module3;

import org.abbtech.module3.model.Car;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Beans Configuration Tests")
class BeansTest {

    private final Beans beans = new Beans();

    @Test
    @DisplayName("Should create BMW bean")
    void testCreateCarBmw() {
        Car bmw = beans.createCarBmw();

        assertEquals(1L, bmw.getId());
        assertEquals("BMW", bmw.getName());
        assertEquals("red", bmw.getColor());
    }

    @Test
    @DisplayName("Should create Ford bean")
    void testCreateCarFord() {
        Car ford = beans.createCarFord();

        assertEquals(2L, ford.getId());
        assertEquals("Ford", ford.getName());
        assertEquals("white", ford.getColor());
    }

    @Test
    @DisplayName("Should create Toyota bean")
    void testCreateCarToyota() {
        Car toyota = beans.createCarToyota();

        assertEquals(3L, toyota.getId());
        assertEquals("Toyota", toyota.getName());
        assertEquals("blue", toyota.getColor());
    }

    @Test
    @DisplayName("Should create initial cars list")
    void testInitialCars() {
        List<Car> cars = beans.initialCars();

        assertEquals(3, cars.size());
        assertEquals("BMW", cars.get(0).getName());
        assertEquals("Ford", cars.get(1).getName());
        assertEquals("Toyota", cars.get(2).getName());
    }
}