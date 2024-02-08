package com.code.springelasticsearch.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.code.springelasticsearch.Util.ESUtil;
import com.code.springelasticsearch.entity.Person;
import com.code.springelasticsearch.repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
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

    public Person createIndex(Person person) {
        return personRepository.save(person);
    }

    public void addPersonsFromJson() {
        log.info("Adding person From Json");
        List<Person> personList = jsonDataService.readItemsFromJson();
        personRepository.saveAll(personList);
    }

    public List<Person> getAllDataFromJson(String indexName) {
        var query = ESUtil.createMatchAllqUery();
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
}
