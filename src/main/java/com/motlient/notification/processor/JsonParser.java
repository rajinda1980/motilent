package com.motlient.notification.processor;

import java.io.IOException;

public interface JsonParser {
    <T> T parse(String filePath, Class<T> valueType) throws IOException;
    String serialize(Object value) throws IOException;
}
