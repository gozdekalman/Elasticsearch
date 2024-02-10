package com.code.springelasticsearch.controller;

import com.code.springelasticsearch.entity.Person;
import com.code.springelasticsearch.service.PersonService;
import dto.SearchRequestDto;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/api/v1/persons")
public class PersonController {
    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping()
    public Person addPerson(@RequestBody Person person){
        return  personService.addPerson(person);
    }


    @PostMapping("/init-index")
    public void addPersonsFromJson(){
        personService.addPersonsFromJson();
    }

  @GetMapping("/getAllFromIndex/{indexName}")
    public List<Person> getAllDataFromJson(@PathVariable String indexName){
        return personService.getAllDataFromJson(indexName);
    }




    @GetMapping("/search")
    public List<Person> searchPersonsByFieldAndValue(@RequestBody SearchRequestDto searchRequestDto){
        return personService.searchPersonsByFieldAndValue(searchRequestDto);
    }

    @GetMapping("/search/{name}/{surname}")
    public List<Person> searchPersonByNameAndSurnameWithQuery(@PathVariable String name, @PathVariable String surname){
        return  personService.searchPersonByNameAndSurnameWithQuery(name,surname);
    }

    @GetMapping("/boolQuery")
    public List<Person> boolQuery(@RequestBody SearchRequestDto dto){
        return personService.boolQuery(dto);
    }


    @GetMapping("/autoSuggest/{name}")
    public Set<String> autoSuggestPersonsByName(@PathVariable String name){
        return personService.findSuggestPersonsByNames(name);
    }


      @PostConstruct
    public void init(){
          Person person = new Person();
          person.setId("P0001");
          person.setName("Gozde");
          person.setSurname("Kalman");
          person.setAddress("test");

         personService.addPerson(person);
      }/*
    @GetMapping("/{search}")
    public ResponseEntity<List<Person>> getPerson(@PathVariable String search){
        List<Person> person = personRepository.getCustomQuery(search);
        return ResponseEntity.ok(person);

    }*/
}
