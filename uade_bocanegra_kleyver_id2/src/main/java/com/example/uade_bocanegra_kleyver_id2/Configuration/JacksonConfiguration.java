package com.example.uade_bocanegra_kleyver_id2.Configuration;

import java.io.IOException;

import org.bson.types.ObjectId;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;

@Configuration
public class JacksonConfiguration {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        
        // Configuración para manejar fechas
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        
        // Configurar el deserializador personalizado para ObjectId
        SimpleModule module = new SimpleModule();
        module.addDeserializer(ObjectId.class, new ObjectIdDeserializer());
        objectMapper.registerModule(module);
        
        return objectMapper;
    }
    
    // Deserializador personalizado para ObjectId
    public static class ObjectIdDeserializer extends StdDeserializer<ObjectId> {
        public ObjectIdDeserializer() {
            this(null);
        }

        public ObjectIdDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public ObjectId deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            String objectId = jp.getValueAsString();
            return new ObjectId(objectId);
        }
    }
}