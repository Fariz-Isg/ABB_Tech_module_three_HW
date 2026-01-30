package org.abbtech.module3.controller;

import jakarta.validation.Valid;
import org.abbtech.module3.dto.ModelRequestDto;
import org.abbtech.module3.dto.ModelResponseDto;
import org.abbtech.module3.service.ModelService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/models")
public class ModelController {

    private final ModelService modelService;

    public ModelController(ModelService modelService) {
        this.modelService = modelService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ModelResponseDto> getAllModels() {
        return modelService.getAllModels();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ModelResponseDto getModelById(@PathVariable Long id) {
        return modelService.getModelById(id);
    }

    @GetMapping("/brand/{brandId}")
    @ResponseStatus(HttpStatus.OK)
    public List<ModelResponseDto> getModelsByBrandId(@PathVariable Long brandId) {
        return modelService.getModelsByBrandId(brandId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ModelResponseDto createModel(@Valid @RequestBody ModelRequestDto requestDto) {
        return modelService.createModel(requestDto);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ModelResponseDto updateModel(@PathVariable Long id, @Valid @RequestBody ModelRequestDto requestDto) {
        return modelService.updateModel(id, requestDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteModel(@PathVariable Long id) {
        modelService.deleteModel(id);
    }
}