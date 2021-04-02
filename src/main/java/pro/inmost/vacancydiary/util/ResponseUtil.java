package pro.inmost.vacancydiary.util;

import java.util.Collections;
import java.util.Map;
import pro.inmost.vacancydiary.response.status.ResponseStatus;

public class ResponseUtil {
    private static final String RESPONSE = "response";

    public static Map<String, ResponseStatus> createResponse(ResponseStatus response) {
        return Collections.singletonMap(RESPONSE, response);
    }
}
