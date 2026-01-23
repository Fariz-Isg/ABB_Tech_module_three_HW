package org.abbtech.module3.controller;

import org.abbtech.module3.model.Car;
import org.abbtech.module3.service.CarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@WebMvcTest(CarController.class)
@DisplayName("CarController Integration Tests")
class CarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CarService carService;

    @Autowired
    private ObjectMapper objectMapper;

    private List<Car> testCars;
    private Car bmw;
    private Car ford;
    private Car toyota;

    @BeforeEach
    void setUp() {
        bmw = Car.builder()
                .id(1L)
                .name("BMW")
                .color("red")
                .year(2000)
                .price(15000)
                .build();

        ford = Car.builder()
                .id(2L)
                .name("Ford")
                .color("white")
                .year(2005)
                .price(12000)
                .build();

        toyota = Car.builder()
                .id(3L)
                .name("Toyota")
                .color("blue")
                .year(2010)
                .price(18000)
                .build();

        testCars = Arrays.asList(bmw, ford, toyota);
    }

    @Test
    @DisplayName("GET /cars - Should return all cars with 202 status")
    void testGetCars() throws Exception {
        when(carService.getAll()).thenReturn(testCars);

        mockMvc.perform(get("/cars"))
                .andExpect(status().isAccepted())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].name", is("BMW")))
                .andExpect(jsonPath("$[0].color", is("red")))
                .andExpect(jsonPath("$[0].year", is(2000)))
                .andExpect(jsonPath("$[0].price", is(15000.0)))
                .andExpect(jsonPath("$[1].name", is("Ford")))
                .andExpect(jsonPath("$[2].name", is("Toyota")));

        verify(carService, times(1)).getAll();
    }

    @Test
    @DisplayName("GET /cars - Should return empty list when no cars exist")
    void testGetCarsEmpty() throws Exception {
        when(carService.getAll()).thenReturn(Arrays.asList());

        mockMvc.perform(get("/cars"))
                .andExpect(status().isAccepted())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));

        verify(carService, times(1)).getAll();
    }

    @Test
    @DisplayName("GET /cars/{id} - Should return car when ID exists")
    void testGetCarByIdWhenExists() throws Exception {
        when(carService.getById(1L)).thenReturn(Optional.of(bmw));

        mockMvc.perform(get("/cars/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("BMW")))
                .andExpect(jsonPath("$.color", is("red")))
                .andExpect(jsonPath("$.year", is(2000)))
                .andExpect(jsonPath("$.price", is(15000.0)));

        verify(carService, times(1)).getById(1L);
    }

    @Test
    @DisplayName("GET /cars/{id} - Should return 404 when car not found")
    void testGetCarByIdWhenNotExists() throws Exception {
        when(carService.getById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/cars/999"))
                .andExpect(status().isNotFound());

        verify(carService, times(1)).getById(999L);
    }

    @Test
    @DisplayName("POST /cars - Should create new car with 201 status")
    void testCreateCar() throws Exception {
        Car newCar = Car.builder()
                .name("Mercedes")
                .color("black")
                .year(2020)
                .price(30000)
                .build();

        doNothing().when(carService).save(any(Car.class));

        mockMvc.perform(post("/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCar)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("Mercedes")))
                .andExpect(jsonPath("$.color", is("black")))
                .andExpect(jsonPath("$.year", is(2020)))
                .andExpect(jsonPath("$.price", is(30000.0)));

        verify(carService, times(1)).save(any(Car.class));
    }

    @Test
    @DisplayName("PUT /cars/{id} - Should update existing car")
    void testUpdateCarWhenExists() throws Exception {
        Car updatedCar = Car.builder()
                .id(1L)
                .name("BMW Updated")
                .color("black")
                .year(2021)
                .price(20000)
                .build();

        when(carService.update(eq(1L), any(Car.class))).thenReturn(Optional.of(updatedCar));

        mockMvc.perform(put("/cars/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedCar)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("BMW Updated")))
                .andExpect(jsonPath("$.color", is("black")))
                .andExpect(jsonPath("$.year", is(2021)))
                .andExpect(jsonPath("$.price", is(20000.0)));

        verify(carService, times(1)).update(eq(1L), any(Car.class));
    }

    @Test
    @DisplayName("PUT /cars/{id} - Should return 404 when car not found")
    void testUpdateCarWhenNotExists() throws Exception {
        Car updatedCar = Car.builder()
                .name("NonExistent")
                .color("gray")
                .year(2022)
                .price(25000)
                .build();

        when(carService.update(eq(999L), any(Car.class))).thenReturn(Optional.empty());

        mockMvc.perform(put("/cars/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedCar)))
                .andExpect(status().isNotFound());

        verify(carService, times(1)).update(eq(999L), any(Car.class));
    }

    @Test
    @DisplayName("DELETE /cars/{id} - Should delete car with 202 status")
    void testDeleteCar() throws Exception {
        doNothing().when(carService).delete(1L);

        mockMvc.perform(delete("/cars/1"))
                .andExpect(status().isAccepted());

        verify(carService, times(1)).delete(1L);
    }

    @Test
    @DisplayName("DELETE /cars/{id} - Should handle deletion of non-existent car")
    void testDeleteNonExistentCar() throws Exception {
        doNothing().when(carService).delete(999L);

        mockMvc.perform(delete("/cars/999"))
                .andExpect(status().isAccepted());

        verify(carService, times(1)).delete(999L);
    }
}