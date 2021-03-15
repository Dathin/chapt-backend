package me.pedrocaires.chapt.core.contact;

import com.fasterxml.jackson.core.JsonProcessingException;
import me.pedrocaires.chapt.core.json.JsonService;
import me.pedrocaires.chapt.core.testinterface.SerializationTest;
import org.junit.jupiter.api.Test;

public class ContactRequestSerializationTest implements SerializationTest {

    @Test
    @Override
    public void serializeDeserialize() throws JsonProcessingException {
        var serialized = JsonService.writeJsonObject(new ContactRequest());
        JsonService.readJsonObject(serialized, ContactRequest.class);
    }

}
