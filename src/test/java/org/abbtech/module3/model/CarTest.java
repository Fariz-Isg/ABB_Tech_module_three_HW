package org.abbtech.module3.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Car Model Tests")
class CarTest {

    @Test
    @DisplayName("Should create car with builder")
    void testBuilder() {
        Car car = Car.builder()
                .id(1L)
                .name("BMW")
                .color("red")
                .year(2000)
                .price(15000)
                .build();

        assertEquals(1L, car.getId());
        assertEquals("BMW", car.getName());
        assertEquals("red", car.getColor());
        assertEquals(2000, car.getYear());
        assertEquals(15000, car.getPrice());
    }

    @Test
    @DisplayName("Should set and get properties")
    void testGettersSetters() {
        Car car = new Car();

        car.setId(2L);
        car.setName("Ford");
        car.setColor("blue");
        car.setYear(2005);
        car.setPrice(12000);

        assertEquals(2L, car.getId());
        assertEquals("Ford", car.getName());
        assertEquals("blue", car.getColor());
        assertEquals(2005, car.getYear());
        assertEquals(12000, car.getPrice());
    }
}