package org.abbtech.module3.controller;

import jakarta.validation.Valid;
import org.abbtech.module3.dto.CarRequestDto;
import org.abbtech.module3.dto.CarResponseDto;
import org.abbtech.module3.service.CarService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cars")
public class CarController {

    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CarResponseDto> getAllCars() {
        return carService.getAllCars();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CarResponseDto getCarById(@PathVariable Long id) {
        return carService.getCarById(id);
    }

    @GetMapping("/model/{modelId}")
    @ResponseStatus(HttpStatus.OK)
    public List<CarResponseDto> getCarsByModelId(@PathVariable Long modelId) {
        return carService.getCarsByModelId(modelId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CarResponseDto createCar(@Valid @RequestBody CarRequestDto requestDto) {
        return carService.createCar(requestDto);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CarResponseDto updateCar(@PathVariable Long id, @Valid @RequestBody CarRequestDto requestDto) {
        return carService.updateCar(id, requestDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCar(@PathVariable Long id) {
        carService.deleteCar(id);
    }
}