package webserver.response;

import java.util.HashMap;
import java.util.Map;

public class ResponseHeader {
    private static final String HEADER_FIELD_SEPARATOR = ": ";

    private Map<String, String> attributes;

    public ResponseHeader() {
        this.attributes = new HashMap<>();
    }

    public boolean addAttribute(String key, String value) {
        if (attributes.containsKey(key)) {
            return false;
        }
        attributes.put(key, value);
        return true;
    }

    public String response() {
        StringBuilder str = new StringBuilder();
        for (String attributeName : attributes.keySet()) {
            str.append(attributeName).append(HEADER_FIELD_SEPARATOR).append(attributes.get(attributeName)).append("\r\n");
        }
        return str.toString();
    }
}
