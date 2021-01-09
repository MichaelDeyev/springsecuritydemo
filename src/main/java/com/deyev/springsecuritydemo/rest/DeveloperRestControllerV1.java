package com.deyev.springsecuritydemo.rest;

import com.deyev.springsecuritydemo.model.Developer;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/v1/developers")
public class DeveloperRestControllerV1 {

    private List<Developer> developers = Stream.of(
            new Developer(1L, "Ivan","Ivanov"),
            new Developer(2L, "Sergey","Sergeev"),
            new Developer(3L, "Petr","Petrov")
    ).collect(Collectors.toList());

    @GetMapping
    public List<Developer> getAll(){
        return developers;
    }

    @GetMapping("/{id}")
    public Developer getById(@PathVariable Long id){
        return developers.stream()
                .filter(developer -> developer.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @PostMapping
    public Developer createUser(@RequestBody Developer developer){
        this.developers.add((developer));
        return developer;
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id){
        this.developers.removeIf(developer -> developer.getId().equals(id));
    }
}
