package org.abbtech.module3.controller;

import org.abbtech.module3.exception.CarErrorEnum;
import org.abbtech.module3.exception.CarException;
import org.abbtech.module3.exception.base.BaseErrorResponseDTO;
import org.abbtech.module3.service.CarService;
import org.abbtech.module3.model.Car;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
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
    public List<Car> getCars() {
        return carService.getAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Car getCarById(@PathVariable Long id) {
        return carService.getById(id)
                .orElseThrow(() -> new CarException(CarErrorEnum.CAR_NOT_FOUND, id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Car createCar(@RequestBody Car car) {
        carService.save(car);
        return car;
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Car updateCar(@PathVariable Long id, @RequestBody Car car) {
        return carService.update(id, car)
                .orElseThrow(() -> new CarException(CarErrorEnum.CAR_NOT_FOUND, id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCar(@PathVariable Long id) {
        carService.delete(id);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<BaseErrorResponseDTO> handleIllegalArgument(
            IllegalArgumentException ex,
            WebRequest webRequest) {
        return new ResponseEntity<>(
                new BaseErrorResponseDTO(
                        "ILLEGAL-ARGUMENT-001",
                        ex.getMessage(),
                        webRequest.getContextPath(),
                        LocalDateTime.now().toString(),
                        HttpStatus.BAD_REQUEST.value()
                ),
                HttpStatus.BAD_REQUEST
        );
    }

    @GetMapping("/demo-response-status-exception")
    public Car demoResponseStatusException() {
        throw new ResponseStatusException(
                HttpStatus.NOT_IMPLEMENTED,
                "This endpoint is not yet implemented"
        );
    }
}