package com.code.springelasticsearch.service;

import com.code.springelasticsearch.entity.Person;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JsonDataService {

    private final ObjectMapper objectMapper;

    public List<Person> readItemsFromJson(){
        try {
            ClassPathResource resource = new ClassPathResource("data/persons.json");
            InputStream inputStream = resource.getInputStream();
            return objectMapper.readValue(inputStream, new TypeReference<List<Person>>() {
            });
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
