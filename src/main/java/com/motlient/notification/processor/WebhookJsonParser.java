package com.motlient.notification.processor;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

/**
 * The {@code WebhookJsonParser} class provides methods to parse and serialize JSON data.
 *
 * <p>This class implements the {@link JsonParser} interface and utilizes the
 * Jackson {@link ObjectMapper} to handle JSON operations. It supports two key functionalities:
 *
 * <ul>
 *   <li>Deserializing (parsing) JSON content from a file into an object of a specified type.</li>
 *   <li>Serializing a given Java object into its JSON string representation.</li>
 * </ul>
 *
 * <p>Usage Example:
 * <pre>
 *     WebhookJsonParser parser = new WebhookJsonParser();
 *     MyObject obj = parser.parse("data.json", MyObject.class);
 *     String jsonString = parser.serialize(obj);
 * </pre>
 *
 * <p><b>Note:</b> This class throws {@link IOException} in case of any I/O or
 * JSON parsing/serialization errors.
 */
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
