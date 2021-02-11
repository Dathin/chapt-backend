package me.pedrocaires.chapt.core.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import me.pedrocaires.chapt.core.json.JsonService;
import me.pedrocaires.chapt.core.message.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ChatWebSocketMessageEncoderTest {

    ChatWebSocketMessageEncoder chatWebSocketMessageEncoder;

    @BeforeEach
    void init() {
        chatWebSocketMessageEncoder = new ChatWebSocketMessageEncoder();
    }

    @Test
    void shouldEncodeValidFormat() throws JsonProcessingException {
        var message = new Message(1, "content");

        var encodedMessage = chatWebSocketMessageEncoder.encode(message);

        assertEquals(JsonService.writeJsonObject(message), encodedMessage);
    }

    @Test
    void shouldInit() {
        chatWebSocketMessageEncoder.init(null);
    }

    @Test
    void shouldDestroy() {
        chatWebSocketMessageEncoder.destroy();
    }


}
