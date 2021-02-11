package me.pedrocaires.chapt.core.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import me.pedrocaires.chapt.core.exception.UnexpectedException;
import me.pedrocaires.chapt.core.json.JsonService;
import me.pedrocaires.chapt.core.message.Message;

import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class ChatWebSocketMessageEncoder implements Encoder.Text<Message> {

    @Override
    public String encode(Message message) {
        try {
            return JsonService.writeJsonObject(message);
        } catch (JsonProcessingException e) {
            throw new UnexpectedException();
        }
    }

    @Override
    public void init(EndpointConfig endpointConfig) {
        //Interface forces us to implement this method, but we will just ignore it
    }

    @Override
    public void destroy() {
        //Interface forces us to implement this method, but we will just ignore it
    }
}
