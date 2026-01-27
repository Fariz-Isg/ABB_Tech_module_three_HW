package org.abbtech.module3.service;

import org.abbtech.module3.config.AppConfig;
import org.abbtech.module3.exception.CarErrorEnum;
import org.abbtech.module3.exception.CarException;
import org.abbtech.module3.logger.LoggerService;
import org.abbtech.module3.model.Car;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class CarService {

    private final List<Car> cars = new ArrayList<>();
    private final AtomicLong idCounter = new AtomicLong(4);
    private final LoggerService loggerService;
    private final AppConfig appConfig;

    @Value("${app.env}")
    private String env;

    public CarService(List<Car> initialCars, LoggerService loggerService, AppConfig appConfig) {
        this.cars.addAll(initialCars);
        this.loggerService = loggerService;
        this.appConfig = appConfig;
    }

    public List<Car> getAll() {
        loggerService.logRequest("/cars", "Fetching all cars");

        if (appConfig.getFeatures().isDetailedLogging()) {
            System.out.println("Environment (from @ConfigurationProperties): " + appConfig.getEnv());
            System.out.println("Environment (from @Value): " + env);
            System.out.println("Total cars in database: " + cars.size());
        }

        loggerService.logResponse("/cars", cars);
        return cars;
    }

    public Optional<Car> getById(Long id) {
        loggerService.logRequest("/cars/" + id, "Fetching car by id: " + id);

        Optional<Car> car = cars.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();

        if (car.isEmpty()) {
            loggerService.logError("/cars/" + id, "Car not found");
            throw new CarException(CarErrorEnum.CAR_NOT_FOUND, id);
        }

        loggerService.logResponse("/cars/" + id, car.get());
        return car;
    }

    public void save(Car car) {
        loggerService.logRequest("/cars", car);

        if (car.getName() == null || car.getName().isBlank()) {
            throw new CarException(CarErrorEnum.INVALID_CAR_DATA, "Car name is required");
        }

        Car savedCar = create(car);
        cars.add(savedCar);

        loggerService.logResponse("/cars", savedCar);
    }

    public Optional<Car> update(Long id, Car car) {
        loggerService.logRequest("/cars/" + id, car);

        Optional<Car> existingCar = cars.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();

        if (existingCar.isEmpty()) {
            loggerService.logError("/cars/" + id, "Car not found for update");
            throw new CarException(CarErrorEnum.CAR_NOT_FOUND, id);
        }

        int index = cars.indexOf(existingCar.get());
        Car updatedCar = Car.builder()
                .id(id)
                .name(car.getName())
                .color(car.getColor())
                .year(car.getYear())
                .price(car.getPrice())
                .build();

        cars.set(index, updatedCar);

        loggerService.logResponse("/cars/" + id, updatedCar);
        return Optional.of(updatedCar);
    }

    public void delete(Long id) {
        loggerService.logRequest("/cars/" + id, "Deleting car");

        boolean removed = cars.removeIf(car -> car.getId().equals(id));

        if (!removed) {
            loggerService.logError("/cars/" + id, "Car not found for deletion");
            throw new CarException(CarErrorEnum.CAR_NOT_FOUND, id);
        }

        loggerService.logResponse("/cars/" + id, "Car deleted successfully");
    }

    private Car create(Car car) {
        return Car.builder()
                .id(idCounter.getAndIncrement())
                .name(car.getName())
                .color(car.getColor())
                .year(car.getYear())
                .price(car.getPrice())
                .build();
    }
}