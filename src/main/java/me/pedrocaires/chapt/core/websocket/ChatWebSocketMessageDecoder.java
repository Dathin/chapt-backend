package me.pedrocaires.chapt.core.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import me.pedrocaires.chapt.core.exception.UnexpectedException;
import me.pedrocaires.chapt.core.json.JsonService;
import me.pedrocaires.chapt.core.message.Message;

import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

public class ChatWebSocketMessageDecoder implements Decoder.Text<Message> {

    @Override
    public Message decode(String s) {
        try {
            return JsonService.readJsonObject(s, Message.class);
        } catch (JsonProcessingException e) {
            throw new UnexpectedException();
        }
    }

    @Override
    public boolean willDecode(String s) {
        return true;
    }

    @Override
    public void init(EndpointConfig endpointConfig) {
    }

    @Override
    public void destroy() {
    }
}
