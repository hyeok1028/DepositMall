package com.hana8.hanaro.common.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.hana8.hanaro.common.util.AccountNumberFormatter;

import java.io.IOException;

public class AccountNumberSerializer extends JsonSerializer<String> {

    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(AccountNumberFormatter.format(value));
    }
}
