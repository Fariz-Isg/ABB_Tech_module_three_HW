package org.abbtech.module3.service;

import org.abbtech.module3.dto.BrandRequestDto;
import org.abbtech.module3.dto.BrandResponseDto;
import org.abbtech.module3.model.Brand;
import org.abbtech.module3.exception.CarErrorEnum;
import org.abbtech.module3.exception.CarException;
import org.abbtech.module3.logger.LoggerService;
import org.abbtech.module3.repository.BrandRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BrandService {

    private final BrandRepository brandRepository;
    private final LoggerService loggerService;

    public BrandService(BrandRepository brandRepository, LoggerService loggerService) {
        this.brandRepository = brandRepository;
        this.loggerService = loggerService;
    }

    @Transactional(readOnly = true)
    public List<BrandResponseDto> getAllBrands() {
        loggerService.logRequest("/brands", "Fetching all brands");

        List<BrandResponseDto> brands = brandRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());

        loggerService.logResponse("/brands", brands);
        return brands;
    }

    @Transactional(readOnly = true)
    public BrandResponseDto getBrandById(Long id) {
        loggerService.logRequest("/brands/" + id, "Fetching brand by id: " + id);

        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> {
                    loggerService.logError("/brands/" + id, "Brand not found");
                    return new CarException(CarErrorEnum.BRAND_NOT_FOUND, id);
                });

        BrandResponseDto response = toDto(brand);
        loggerService.logResponse("/brands/" + id, response);
        return response;
    }

    @Transactional
    public BrandResponseDto createBrand(BrandRequestDto requestDto) {
        loggerService.logRequest("/brands", requestDto);

        if (brandRepository.existsByName(requestDto.name())) {
            loggerService.logError("/brands", "Brand already exists");
            throw new CarException(CarErrorEnum.BRAND_ALREADY_EXISTS, requestDto.name());
        }

        Brand brand = Brand.builder()
                .name(requestDto.name())
                .country(requestDto.country())
                .foundedYear(requestDto.foundedYear())
                .build();

        Brand savedBrand = brandRepository.save(brand);

        BrandResponseDto response = toDto(savedBrand);
        loggerService.logResponse("/brands", response);
        return response;
    }

    @Transactional
    public BrandResponseDto updateBrand(Long id, BrandRequestDto requestDto) {
        loggerService.logRequest("/brands/" + id, requestDto);

        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> {
                    loggerService.logError("/brands/" + id, "Brand not found");
                    return new CarException(CarErrorEnum.BRAND_NOT_FOUND, id);
                });

        brand.setName(requestDto.name());
        brand.setCountry(requestDto.country());
        brand.setFoundedYear(requestDto.foundedYear());

        Brand updatedBrand = brandRepository.save(brand);

        BrandResponseDto response = toDto(updatedBrand);
        loggerService.logResponse("/brands/" + id, response);
        return response;
    }

    @Transactional
    public void deleteBrand(Long id) {
        loggerService.logRequest("/brands/" + id, "Deleting brand");

        if (!brandRepository.existsById(id)) {
            loggerService.logError("/brands/" + id, "Brand not found");
            throw new CarException(CarErrorEnum.BRAND_NOT_FOUND, id);
        }

        brandRepository.deleteById(id);
        loggerService.logResponse("/brands/" + id, "Brand deleted successfully");
    }

    private BrandResponseDto toDto(Brand entity) {
        return new BrandResponseDto(
                entity.getId(),
                entity.getName(),
                entity.getCountry(),
                entity.getFoundedYear(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}