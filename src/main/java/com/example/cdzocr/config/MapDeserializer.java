package com.example.cdzocr.config;

import org.apache.kafka.common.serialization.Deserializer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Map;

public class MapDeserializer implements Deserializer<Map> {

    @Override
    public void close() {

    }

    @Override
    public void configure(Map config, boolean isKey) {

    }

    @Override
    public Map deserialize(String topic, byte[] message) {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(message); ObjectInput in = new ObjectInputStream(bis)) {
            Object o = in.readObject();
            if (o instanceof Map) {
                return (Map) o;
            } else
                return new HashMap<String, String>();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        // ignore close exception
        return new HashMap<String, String>();
    }
}