package me.pedrocaires.chapt.core.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import me.pedrocaires.chapt.core.json.JsonService;
import me.pedrocaires.chapt.core.testinterface.SerializationTest;
import org.junit.jupiter.api.Test;

class UserLoginResponseSerializationTest implements SerializationTest {

    @Test
    @Override
    public void serializeDeserialize() throws JsonProcessingException {
        var serialized = JsonService.writeJsonObject(new UserLoginResponse());
        JsonService.readJsonObject(serialized, UserLoginResponse.class);
    }
}
