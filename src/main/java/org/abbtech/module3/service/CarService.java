package org.abbtech.module3.service;

import org.abbtech.module3.dto.CarRequestDto;
import org.abbtech.module3.dto.CarResponseDto;
import org.abbtech.module3.model.Car;
import org.abbtech.module3.model.Model;
import org.abbtech.module3.exception.CarErrorEnum;
import org.abbtech.module3.exception.CarException;
import org.abbtech.module3.logger.LoggerService;
import org.abbtech.module3.repository.CarRepository;
import org.abbtech.module3.repository.ModelRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarService {

    private final CarRepository carRepository;
    private final ModelRepository modelRepository;
    private final LoggerService loggerService;

    public CarService(CarRepository carRepository, ModelRepository modelRepository,
                      LoggerService loggerService) {
        this.carRepository = carRepository;
        this.modelRepository = modelRepository;
        this.loggerService = loggerService;
    }

    @Transactional(readOnly = true)
    public List<CarResponseDto> getAllCars() {
        loggerService.logRequest("/cars", "Fetching all cars");

        List<CarResponseDto> cars = carRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());

        loggerService.logResponse("/cars", cars);
        return cars;
    }

    @Transactional(readOnly = true)
    public CarResponseDto getCarById(Long id) {
        loggerService.logRequest("/cars/" + id, "Fetching car by id: " + id);

        Car car = carRepository.findById(id)
                .orElseThrow(() -> {
                    loggerService.logError("/cars/" + id, "Car not found");
                    return new CarException(CarErrorEnum.CAR_NOT_FOUND, id);
                });

        CarResponseDto response = toDto(car);
        loggerService.logResponse("/cars/" + id, response);
        return response;
    }

    @Transactional(readOnly = true)
    public List<CarResponseDto> getCarsByModelId(Long modelId) {
        loggerService.logRequest("/cars/model/" + modelId, "Fetching cars by model");

        List<CarResponseDto> cars = carRepository.findByModelId(modelId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());

        loggerService.logResponse("/cars/model/" + modelId, cars);
        return cars;
    }

    @Transactional
    public CarResponseDto createCar(CarRequestDto requestDto) {
        loggerService.logRequest("/cars", requestDto);

        Model model = modelRepository.findById(requestDto.modelId())
                .orElseThrow(() -> {
                    loggerService.logError("/cars", "Model not found");
                    return new CarException(CarErrorEnum.MODEL_NOT_FOUND, requestDto.modelId());
                });

        Car car = Car.builder()
                .color(requestDto.color())
                .year(requestDto.year())
                .price(requestDto.price())
                .speed(requestDto.speed())
                .vinNumber(requestDto.vinNumber())
                .model(model)
                .build();

        Car savedCar = carRepository.save(car);

        CarResponseDto response = toDto(savedCar);
        loggerService.logResponse("/cars", response);
        return response;
    }

    @Transactional
    public CarResponseDto updateCar(Long id, CarRequestDto requestDto) {
        loggerService.logRequest("/cars/" + id, requestDto);

        Car car = carRepository.findById(id)
                .orElseThrow(() -> {
                    loggerService.logError("/cars/" + id, "Car not found");
                    return new CarException(CarErrorEnum.CAR_NOT_FOUND, id);
                });

        Model model = modelRepository.findById(requestDto.modelId())
                .orElseThrow(() -> {
                    loggerService.logError("/cars/" + id, "Model not found");
                    return new CarException(CarErrorEnum.MODEL_NOT_FOUND, requestDto.modelId());
                });

        car.setColor(requestDto.color());
        car.setYear(requestDto.year());
        car.setPrice(requestDto.price());
        car.setSpeed(requestDto.speed());
        car.setVinNumber(requestDto.vinNumber());
        car.setModel(model);

        Car updatedCar = carRepository.save(car);

        CarResponseDto response = toDto(updatedCar);
        loggerService.logResponse("/cars/" + id, response);
        return response;
    }

    @Transactional
    public void deleteCar(Long id) {
        loggerService.logRequest("/cars/" + id, "Deleting car");

        if (!carRepository.existsById(id)) {
            loggerService.logError("/cars/" + id, "Car not found");
            throw new CarException(CarErrorEnum.CAR_NOT_FOUND, id);
        }

        carRepository.deleteById(id);
        loggerService.logResponse("/cars/" + id, "Car deleted successfully");
    }

    private CarResponseDto toDto(Car entity) {
        return new CarResponseDto(
                entity.getId(),
                entity.getColor(),
                entity.getYear(),
                entity.getPrice(),
                entity.getSpeed(),
                entity.getVinNumber(),
                entity.getModel().getId(),
                entity.getModel().getName(),
                entity.getModel().getBrand().getName(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}