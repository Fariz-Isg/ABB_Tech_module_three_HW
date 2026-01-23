package org.abbtech.module3;

import org.abbtech.module3.model.Car;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class Beans {
    @Bean(name = "bmw")
    public Car createCarBmw() {
        return Car.builder()
                .name("BMW")
                .color("red")
                .year(2000)
                .price(15000)
                .id(1L)
                .build();
    }

    @Bean(name = "ford")
    public Car createCarFord() {
        return Car.builder()
                .name("Ford")
                .color("white")
                .year(2005)
                .price(12000)
                .id(2L)
                .build();
    }

    @Bean(name = "toyota")
    public Car createCarToyota() {
        return Car.builder()
                .name("Toyota")
                .color("blue")
                .year(2010)
                .price(18000)
                .id(3L)
                .build();
    }

    @Bean
    public List<Car> initialCars() {
        return List.of(
                createCarBmw(),
                createCarFord(),
                createCarToyota()
        );
    }
}
