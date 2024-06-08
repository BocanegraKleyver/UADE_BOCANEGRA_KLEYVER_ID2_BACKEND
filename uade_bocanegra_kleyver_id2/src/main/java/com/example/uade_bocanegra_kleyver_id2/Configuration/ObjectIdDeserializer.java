package com.example.uade_bocanegra_kleyver_id2.Configuration;

import java.io.IOException;

import org.bson.types.ObjectId;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class ObjectIdDeserializer extends StdDeserializer<ObjectId> {

    public ObjectIdDeserializer() {
        this(null);
    }

    public ObjectIdDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public ObjectId deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);
        String objectIdAsString = node.get("$oid").asText();
        return new ObjectId(objectIdAsString);
    }
}