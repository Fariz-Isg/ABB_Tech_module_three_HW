package org.abbtech.module3.controller;

import org.abbtech.module3.exception.CarErrorEnum;
import org.abbtech.module3.exception.CarException;
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
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CarController.class)
@DisplayName("CarController Tests")
class CarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CarService carService;

    @Autowired
    private ObjectMapper objectMapper;

    private Car bmw;

    @BeforeEach
    void setUp() {
        bmw = Car.builder().id(1L).name("BMW").color("red").year(2000).price(15000).build();
    }

    @Test
    @DisplayName("GET /cars - should return all cars")
    void testGetCars() throws Exception {
        when(carService.getAll()).thenReturn(Arrays.asList(bmw));

        mockMvc.perform(get("/cars"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("BMW"));
    }

    @Test
    @DisplayName("GET /cars/{id} - should return car")
    void testGetCarById() throws Exception {
        when(carService.getById(1L)).thenReturn(Optional.of(bmw));

        mockMvc.perform(get("/cars/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("BMW"));
    }

    @Test
    @DisplayName("GET /cars/{id} - should return 404")
    void testGetCarByIdNotFound() throws Exception {
        when(carService.getById(999L)).thenThrow(new CarException(CarErrorEnum.CAR_NOT_FOUND, 999L));

        mockMvc.perform(get("/cars/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /cars - should create car")
    void testCreateCar() throws Exception {
        Car newCar = Car.builder().name("Tesla").color("black").year(2024).price(50000).build();

        mockMvc.perform(post("/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCar)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("PUT /cars/{id} - should update car")
    void testUpdateCar() throws Exception {
        Car updated = Car.builder().id(1L).name("BMW Updated").color("blue").year(2022).price(20000).build();
        when(carService.update(eq(1L), any(Car.class))).thenReturn(Optional.of(updated));

        mockMvc.perform(put("/cars/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("BMW Updated"));
    }

    @Test
    @DisplayName("DELETE /cars/{id} - should delete car")
    void testDeleteCar() throws Exception {
        mockMvc.perform(delete("/cars/1"))
                .andExpect(status().isNoContent());
    }
}