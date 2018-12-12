package com.client.util;

import com.client.lyrics.Sentence;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.*;

import java.io.IOException;
import java.util.List;

public class Json {

    public static final Gson GSON = new GsonBuilder().create();

    private static final ObjectMapper mapper;

    static {
        ObjectMapper m = new ObjectMapper();
       // m.registerModule(new JavaTimeModule());
        m.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        m.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        m.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        mapper = m;
    }

    public static String toJson(Object o) {
        try {
            return mapper.writeValueAsString(o);
        } catch(JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }

    public static <T> T fromJson(String value, Class<T> valueType) {
        try {
            return mapper.readValue(value, valueType);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public static <T> T fromJson(String value, TypeReference<T> reference) {
        try {
            return mapper.readValue(value, reference);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public static List<Sentence> getSentences(Object value){
        return mapper.convertValue(
                value,
                new TypeReference<List<Sentence>>() { });
    }

}

