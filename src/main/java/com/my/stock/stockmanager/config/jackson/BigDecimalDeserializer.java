package com.my.stock.stockmanager.config.jackson;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.math.BigDecimal;

public class BigDecimalDeserializer extends StdDeserializer<BigDecimal> {

    protected BigDecimalDeserializer() {
        super(BigDecimal.class);
    }

    @Override
    public BigDecimal deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JacksonException {
        final String input = jsonParser.getText().replaceAll(",", "");
        try {
            return BigDecimal.valueOf(Double.parseDouble(input));
        } catch (final NumberFormatException e) {
            throw new IllegalArgumentException("Value must be a number.");
        }
    }
}