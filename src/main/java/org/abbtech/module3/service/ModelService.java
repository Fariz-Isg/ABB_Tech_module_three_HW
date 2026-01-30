package org.abbtech.module3.service;

import org.abbtech.module3.dto.ModelRequestDto;
import org.abbtech.module3.dto.ModelResponseDto;
import org.abbtech.module3.model.Brand;
import org.abbtech.module3.model.Model;
import org.abbtech.module3.exception.CarErrorEnum;
import org.abbtech.module3.exception.CarException;
import org.abbtech.module3.logger.LoggerService;
import org.abbtech.module3.repository.BrandRepository;
import org.abbtech.module3.repository.ModelRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ModelService {

    private final ModelRepository modelRepository;
    private final BrandRepository brandRepository;
    private final LoggerService loggerService;

    public ModelService(ModelRepository modelRepository, BrandRepository brandRepository,
                        LoggerService loggerService) {
        this.modelRepository = modelRepository;
        this.brandRepository = brandRepository;
        this.loggerService = loggerService;
    }

    @Transactional(readOnly = true)
    public List<ModelResponseDto> getAllModels() {
        loggerService.logRequest("/models", "Fetching all models");

        List<ModelResponseDto> models = modelRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());

        loggerService.logResponse("/models", models);
        return models;
    }

    @Transactional(readOnly = true)
    public ModelResponseDto getModelById(Long id) {
        loggerService.logRequest("/models/" + id, "Fetching model by id: " + id);

        Model model = modelRepository.findById(id)
                .orElseThrow(() -> {
                    loggerService.logError("/models/" + id, "Model not found");
                    return new CarException(CarErrorEnum.MODEL_NOT_FOUND, id);
                });

        ModelResponseDto response = toDto(model);
        loggerService.logResponse("/models/" + id, response);
        return response;
    }

    @Transactional(readOnly = true)
    public List<ModelResponseDto> getModelsByBrandId(Long brandId) {
        loggerService.logRequest("/models/brand/" + brandId, "Fetching models by brand");

        List<ModelResponseDto> models = modelRepository.findByBrandId(brandId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());

        loggerService.logResponse("/models/brand/" + brandId, models);
        return models;
    }

    @Transactional
    public ModelResponseDto createModel(ModelRequestDto requestDto) {
        loggerService.logRequest("/models", requestDto);

        Brand brand = brandRepository.findById(requestDto.brandId())
                .orElseThrow(() -> {
                    loggerService.logError("/models", "Brand not found");
                    return new CarException(CarErrorEnum.BRAND_NOT_FOUND, requestDto.brandId());
                });

        Model model = Model.builder()
                .name(requestDto.name())
                .modelYear(requestDto.modelYear())
                .bodyType(requestDto.bodyType())
                .brand(brand)
                .build();

        Model savedModel = modelRepository.save(model);

        ModelResponseDto response = toDto(savedModel);
        loggerService.logResponse("/models", response);
        return response;
    }

    @Transactional
    public ModelResponseDto updateModel(Long id, ModelRequestDto requestDto) {
        loggerService.logRequest("/models/" + id, requestDto);

        Model model = modelRepository.findById(id)
                .orElseThrow(() -> {
                    loggerService.logError("/models/" + id, "Model not found");
                    return new CarException(CarErrorEnum.MODEL_NOT_FOUND, id);
                });

        Brand brand = brandRepository.findById(requestDto.brandId())
                .orElseThrow(() -> {
                    loggerService.logError("/models/" + id, "Brand not found");
                    return new CarException(CarErrorEnum.BRAND_NOT_FOUND, requestDto.brandId());
                });

        model.setName(requestDto.name());
        model.setModelYear(requestDto.modelYear());
        model.setBodyType(requestDto.bodyType());
        model.setBrand(brand);

        Model updatedModel = modelRepository.save(model);

        ModelResponseDto response = toDto(updatedModel);
        loggerService.logResponse("/models/" + id, response);
        return response;
    }

    @Transactional
    public void deleteModel(Long id) {
        loggerService.logRequest("/models/" + id, "Deleting model");

        if (!modelRepository.existsById(id)) {
            loggerService.logError("/models/" + id, "Model not found");
            throw new CarException(CarErrorEnum.MODEL_NOT_FOUND, id);
        }

        modelRepository.deleteById(id);
        loggerService.logResponse("/models/" + id, "Model deleted successfully");
    }

    private ModelResponseDto toDto(Model entity) {
        return new ModelResponseDto(
                entity.getId(),
                entity.getName(),
                entity.getModelYear(),
                entity.getBodyType(),
                entity.getBrand().getId(),
                entity.getBrand().getName(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}