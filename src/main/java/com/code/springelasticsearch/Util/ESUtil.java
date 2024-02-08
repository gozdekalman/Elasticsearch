package com.code.springelasticsearch.Util;

import co.elastic.clients.elasticsearch._types.query_dsl.MatchAllQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ESUtil {

    public static Query createMatchAllqUery(){
        return Query.of(q -> q.matchAll((new MatchAllQuery.Builder().build())));
    }
}
