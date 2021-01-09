package com.deyev.springsecuritydemo.rest;

import com.deyev.springsecuritydemo.model.Developer;
import com.deyev.springsecuritydemo.model.Permission;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @GetMapping//к урле "/" имеют доступ все пользователи.
    public List<Developer> getAll(){
        return developers;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('developers.read')")// на чтение (get-request) имеют доступ только пользователи с правом чтения
    public Developer getById(@PathVariable Long id){
        return developers.stream()
                .filter(developer -> developer.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('developers.write')")// на запись (post-request) имеют доступ только пользователи с правом записи
    public Developer createUser(@RequestBody Developer developer){
        this.developers.add((developer));
        return developer;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('developers.write')")// на удаление (delete-request) имеют доступ только пользователи с правом записи
    public void deleteById(@PathVariable Long id){
        this.developers.removeIf(developer -> developer.getId().equals(id));
    }
}
