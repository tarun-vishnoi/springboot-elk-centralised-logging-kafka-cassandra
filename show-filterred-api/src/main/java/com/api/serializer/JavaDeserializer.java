package com.api.serializer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import org.apache.kafka.common.serialization.Deserializer;

public class JavaDeserializer implements Deserializer<Object> {

	@Override
	public Object deserialize(String topic, byte[] data) {
		try {
			ObjectInputStream inputStream = new ObjectInputStream(new ByteArrayInputStream(data));
			Object object = inputStream.readObject();
			return object;
		} catch (IOException | ClassNotFoundException e) {
			throw new IllegalStateException("Can't deserialize object: " + data, e);
		}
	}
}
