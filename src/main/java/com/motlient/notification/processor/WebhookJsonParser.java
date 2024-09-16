package com.motlient.notification.processor;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class WebhookJsonParser implements JsonParser {

    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public <T> T parse(String filePath, Class<T> valueType) throws IOException {
        return objectMapper.readValue(new File(filePath), valueType);
    }

    @Override
    public String serialize(Object value) throws IOException {
        return objectMapper.writeValueAsString(value);
    }
}
