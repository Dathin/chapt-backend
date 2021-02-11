package me.pedrocaires.chapt.core.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import me.pedrocaires.chapt.core.json.JsonService;
import me.pedrocaires.chapt.core.testinterface.SerializationTest;
import org.junit.jupiter.api.Test;

class MessageSerializationTest implements SerializationTest {

    @Test
    @Override
    public void serializeDeserialize() throws JsonProcessingException {
        var serialized = JsonService.writeJsonObject(new Message());
        JsonService.readJsonObject(serialized, Message.class);
    }

}
