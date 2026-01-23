package org.abbtech.module3.service;

import org.abbtech.module3.model.Car;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class CarService {
    public List<Car> cars = new ArrayList<>();
    private AtomicLong idCounter = new AtomicLong(4);

    public CarService(List<Car> initialCars) {
        this.cars.addAll(initialCars);
    }

    public List<Car> getAll() {
        return cars;
    }

    public Optional<Car> getById(Long id) {
        return cars.stream()
                .filter(car -> car.getId().equals(id))
                .findFirst();
    }

    public void save(Car car) {
        cars.add(create(car));
    }

    public Optional<Car> update(Long id, Car car) {
        cars.set(Math.toIntExact(id-1), create(car));
        return Optional.ofNullable(create(car));
    }

    public void delete(Long id) {
        cars.removeIf(car -> car.getId().equals(id));
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