package org.abbtech.module3.repository;

import org.abbtech.module3.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    List<Car> findByModelId(Long modelId);
    List<Car> findByColor(String color);
    List<Car> findByPriceBetween(Double minPrice, Double maxPrice);
}