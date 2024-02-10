package com.code.springelasticsearch.util;

import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.termvectors.Term;
import co.elastic.clients.util.ObjectBuilder;
import dto.SearchRequestDto;
import lombok.experimental.UtilityClass;

import java.util.function.Function;
import java.util.function.Supplier;

@UtilityClass
public class ESUtil {

    public static Query createMatchAllQuery(){
        return Query.of(q -> q.matchAll((new MatchAllQuery.Builder().build())));
    }

    public static Supplier<Query> buildQueryForFieldAndValue(String fieldName, String searchValue) {
    return  () -> Query.of(q -> q.match(buildMatchForFieldAndValue(fieldName,searchValue)));
    }


    private static MatchQuery buildMatchForFieldAndValue(String fieldName,String searchName){
      return new MatchQuery.Builder()
              .field(fieldName)
              .query(searchName)
              .build();
    }

    public static Supplier<Query> createBoolQuery(SearchRequestDto dto) {
        return  () -> Query.of(q -> q.bool(boolQuery(dto.getFieldName().get(0),dto.getSearchValue().get(0),dto.getFieldName().get(1),dto.getSearchValue().get(1))));

    }

    private static BoolQuery boolQuery(String key1, String value1, String key2, String value2) {
    return new BoolQuery.Builder()
            .filter(termQuery(key1,value1))
            .must(matcQuery(key2,value2))
            .build();
    }

    private static Query matcQuery(String key2, String value2) {
        return Query.of(q-> q.match(new MatchQuery.Builder()
                .field(key2)
                .query(value2)
                .build()));
    }

    private static Query termQuery(String key1, String value1) {
    return Query.of(q-> q.term(new TermQuery.Builder()
            .field(key1)
            .value(value1)
            .build()));

    }

    public static Query buildAutoSuggestQuery(String name) {
        return Query.of(q -> q.match(createAutoSuggestMatchQuery(name)));
    }
    public static MatchQuery createAutoSuggestMatchQuery(String name) {
        return new MatchQuery.Builder()
                .field("name")
                .query(name)
                .analyzer("custom_index")
                .build();
    }
}
