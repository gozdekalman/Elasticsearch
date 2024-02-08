package com.code.springelasticsearch.controller;

import com.code.springelasticsearch.entity.Person;
import com.code.springelasticsearch.service.PersonService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
@RestController
@RequestMapping("/api/v1/persons")
public class PersonController {
    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping()
    public Person createIndex(@RequestBody Person person){
        return  personService.createIndex(person);
    }


    @PostMapping("/init-index")
    public void addPersonsFromJson(){
        personService.addPersonsFromJson();
    }

    @GetMapping("/getAllFromIndex/{indexName}")
    public List<Person> getAllDataFromJson(@PathVariable String indexName){
        return personService.getAllDataFromJson(indexName);
    }

      @PostConstruct
    public void init(){
          Person person = new Person();
          person.setId("P0001");
          person.setName("Gozde");
          person.setSurname("Kalman");
          person.setAddress("test");

         personService.createIndex(person);
      }/*
    @GetMapping("/{search}")
    public ResponseEntity<List<Person>> getPerson(@PathVariable String search){
        List<Person> person = personRepository.getCustomQuery(search);
        return ResponseEntity.ok(person);

    }*/
}
