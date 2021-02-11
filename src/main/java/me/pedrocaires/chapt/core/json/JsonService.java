package me.pedrocaires.chapt.core.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JsonService {

    private static final ObjectMapper OBJECT_MAPPER = getConfiguredObjectMapper();

    private JsonService() {

    }

    public static void writeJsonObjectToHttpServlet(Object object, HttpServletResponse resp) throws IOException {
        resp.getWriter().write(OBJECT_MAPPER.writeValueAsString(object));
        resp.getWriter().flush();
        resp.getWriter().close();
    }

    public static void writeJsonObjectToHttpServlet(Object object, int statusCode, HttpServletResponse resp) throws IOException {
        if (object != null) {
            resp.setStatus(statusCode);
            writeJsonObjectToHttpServlet(object, resp);
        }
    }

    public static String writeJsonObject(Object object) throws JsonProcessingException {
        return OBJECT_MAPPER.writeValueAsString(object);
    }

    public static <T> T readJsonObject(HttpServletRequest req, Class<T> tClass) throws IOException {
        return OBJECT_MAPPER.readValue(req.getReader(), tClass);
    }

    public static <T> T readJsonObject(String object, Class<T> tClass) throws JsonProcessingException {
        return OBJECT_MAPPER.readValue(object, tClass);
    }

    private static ObjectMapper getConfiguredObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }


}
