package com.code.springelasticsearch.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.code.springelasticsearch.util.ESUtil;
import com.code.springelasticsearch.entity.Person;
import com.code.springelasticsearch.repository.PersonRepository;
import dto.SearchRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
@Slf4j

public class PersonService {

   private final PersonRepository personRepository;
  private  final  JsonDataService jsonDataService;

  private final ElasticsearchClient elasticsearchClient;

    public PersonService(PersonRepository personRepository, JsonDataService jsonDataService, ElasticsearchClient elasticsearchClient) {
        this.personRepository = personRepository;
        this.jsonDataService = jsonDataService;
        this.elasticsearchClient = elasticsearchClient;

    }

    public Person addPerson(Person person) {
        return personRepository.save(person);
    }

    public void addPersonsFromJson() {
        log.info("Adding person From Json");
        List<Person> personList = jsonDataService.readItemsFromJson();
        personRepository.saveAll(personList);
    }

    public List<Person> getAllDataFromJson(String indexName) {
        var query = ESUtil.createMatchAllQuery();
        log.info("Elasticsearch query {} ", query.toString());
        SearchResponse<Person> response = null;
        try {
            response = elasticsearchClient.search(
                    q -> q.index(indexName).query(query),Person.class);

        }catch (IOException e){
            throw  new RuntimeException(e);
        }
        log.info("Elasticsearch response {} ", response);

        return extractPersonsFromResponse(response);
    }

    private static List<Person> extractPersonsFromResponse(SearchResponse<Person> response) {
        return response
                .hits()
                .hits()
                .stream()
                .map(Hit::source)
                .collect(Collectors.toList());
    }

    public List<Person> searchPersonsByFieldAndValue(SearchRequestDto searchRequestDto) {
        Supplier<Query> query = ESUtil.buildQueryForFieldAndValue(searchRequestDto.getFieldName().get(0), searchRequestDto.getSearchValue().get(0));

        SearchResponse<Person> response = null;


        try {
            response = elasticsearchClient.search(
                    q -> q.index("person").query( query.get()), Person.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        return extractPersonsFromResponse(response);
    }

    public List<Person> searchPersonByNameAndSurnameWithQuery(String name, String surname) {
            return personRepository.searchByNameAndSurname(name,surname);
    }

    public List<Person> boolQuery(SearchRequestDto dto) {
        var query = ESUtil.createBoolQuery(dto);
        SearchResponse<Person> response = null;


        try {
            response = elasticsearchClient.search(
                    q -> q.index("person").query( query.get()), Person.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        return extractPersonsFromResponse(response);
    }

    public Set<String> findSuggestPersonsByNames(String name) {
        Query autoSuggestQuery = ESUtil.buildAutoSuggestQuery(name);
        log.info("Elasticsearch query: {}", autoSuggestQuery.toString());

        try {
            return elasticsearchClient.search(q -> q.index("person").query(autoSuggestQuery), Person.class)
                    .hits()
                    .hits()
                    .stream()
                    .map(Hit::source)
                    .map(Person::getName)
                    .collect(Collectors.toSet());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }}
}
