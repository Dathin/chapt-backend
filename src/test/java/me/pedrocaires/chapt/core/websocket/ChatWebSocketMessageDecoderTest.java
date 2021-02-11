package me.pedrocaires.chapt.core.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import me.pedrocaires.chapt.core.exception.UnexpectedException;
import me.pedrocaires.chapt.core.json.JsonService;
import me.pedrocaires.chapt.core.message.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static me.pedrocaires.chapt.core.constants.GeneralConstant.EMPTY_STRING;
import static org.junit.jupiter.api.Assertions.*;

class ChatWebSocketMessageDecoderTest {

    ChatWebSocketMessageDecoder chatWebSocketMessageDecoder;

    @BeforeEach
    void init() {
        chatWebSocketMessageDecoder = new ChatWebSocketMessageDecoder();
    }

    @Test
    void shouldDecodeValidFormat() throws JsonProcessingException {
        var message = new Message(1, "content");
        var messageJsonFormat = JsonService.writeJsonObject(message);

        var decodedMessage = chatWebSocketMessageDecoder.decode(messageJsonFormat);

        assertEquals(message.getTo(), decodedMessage.getTo());
        assertEquals(message.getContent(), decodedMessage.getContent());
    }

    @Test
    void shouldThrowUnexpectedExceptionWhenInvalidFormat() {
        var invalidJsonFormat = "";

        assertThrows(UnexpectedException.class, () -> chatWebSocketMessageDecoder.decode(invalidJsonFormat));
    }

    @Test
    void shouldDecode() {
        var willDecode = chatWebSocketMessageDecoder.willDecode(EMPTY_STRING);

        assertTrue(willDecode);
    }

    @Test
    void shouldInit() {
        chatWebSocketMessageDecoder.init(null);
    }

    @Test
    void shouldDestroy() {
        chatWebSocketMessageDecoder.destroy();
    }
}
