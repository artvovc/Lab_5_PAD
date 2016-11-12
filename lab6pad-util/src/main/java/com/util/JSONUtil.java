package com.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;


public class JSONUtil {
    private static ObjectMapper OBJECTMAPPER = new ObjectMapper();

    public static String getJSONStringfromJAVAObject(Object obj) throws JsonProcessingException {
        return OBJECTMAPPER.writeValueAsString(obj);
    }

    public static Object getJAVAObjectfromJSONString(String json, Class klass) throws IOException {
        return OBJECTMAPPER.readValue(json, klass);
    }

    public static Object getJAVAObjectfromJSONStringList(String json, TypeReference<?> typeReference) throws IOException {
        return OBJECTMAPPER.readValue(json, typeReference);
    }
}