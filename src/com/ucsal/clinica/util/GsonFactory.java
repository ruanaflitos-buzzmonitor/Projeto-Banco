package com.ucsal.clinica.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Fábrica de Gson configurado para lidar com os tipos do pacote java.time.
 *
 * POR QUE ISSO EXISTE:
 * O Gson "puro" (new Gson()) NÃO sabe serializar LocalDate / LocalTime /
 * LocalDateTime em JDK 9+. Ele tenta acessar os campos internos via reflection
 * e lança InaccessibleObjectException, derrubando o Servlet com erro 500.
 *
 * Use SEMPRE GsonFactory.criar() no lugar de new Gson() nos Servlets.
 * Datas saem como texto ISO: "2024-01-15", "09:00:00", "2024-01-15T09:00:00".
 */
public final class GsonFactory {

    private GsonFactory() { }

    public static Gson criar() {
        return new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
    }

    static class LocalDateAdapter
            implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {
        public JsonElement serialize(LocalDate src, Type t, JsonSerializationContext c) {
            return new JsonPrimitive(src.format(DateTimeFormatter.ISO_LOCAL_DATE));
        }
        public LocalDate deserialize(JsonElement json, Type t, JsonDeserializationContext c) {
            return LocalDate.parse(json.getAsString());
        }
    }

    static class LocalTimeAdapter
            implements JsonSerializer<LocalTime>, JsonDeserializer<LocalTime> {
        public JsonElement serialize(LocalTime src, Type t, JsonSerializationContext c) {
            return new JsonPrimitive(src.format(DateTimeFormatter.ISO_LOCAL_TIME));
        }
        public LocalTime deserialize(JsonElement json, Type t, JsonDeserializationContext c) {
            return LocalTime.parse(json.getAsString());
        }
    }

    static class LocalDateTimeAdapter
            implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {
        public JsonElement serialize(LocalDateTime src, Type t, JsonSerializationContext c) {
            return new JsonPrimitive(src.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        }
        public LocalDateTime deserialize(JsonElement json, Type t, JsonDeserializationContext c) {
            return LocalDateTime.parse(json.getAsString());
        }
    }
}
