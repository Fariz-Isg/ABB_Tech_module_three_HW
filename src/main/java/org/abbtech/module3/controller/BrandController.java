package org.abbtech.module3.controller;

import jakarta.validation.Valid;
import org.abbtech.module3.dto.BrandRequestDto;
import org.abbtech.module3.dto.BrandResponseDto;
import org.abbtech.module3.service.BrandService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/brands")
public class BrandController {

    private final BrandService brandService;

    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<BrandResponseDto> getAllBrands() {
        return brandService.getAllBrands();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BrandResponseDto getBrandById(@PathVariable Long id) {
        return brandService.getBrandById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BrandResponseDto createBrand(@Valid @RequestBody BrandRequestDto requestDto) {
        return brandService.createBrand(requestDto);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BrandResponseDto updateBrand(@PathVariable Long id, @Valid @RequestBody BrandRequestDto requestDto) {
        return brandService.updateBrand(id, requestDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBrand(@PathVariable Long id) {
        brandService.deleteBrand(id);
    }
}