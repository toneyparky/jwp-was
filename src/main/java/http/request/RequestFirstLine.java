package http.request;

import http.RequestMethod;

public class RequestFirstLine {
    private final RequestMethod requestMethod;
    private String path;
    private String query;
    private final String version;

    public RequestFirstLine(String firstLine) {
        String[] tokens = firstLine.split(" ");
        this.requestMethod = RequestMethod.valueOf(tokens[0]);
        urlParse(tokens[1]);
        this.version = tokens[2];
    }

    private void urlParse(String token) {
        String[] address = token.split("\\?");
        if (address.length > 1) {
            this.query = address[1];
        }
        this.path = address[0];
    }

    public RequestMethod getMethod() {
        return requestMethod;
    }

    public String getPath() {
        return path;
    }


    public String getVersion() {
        return version;
    }

    public String getQuery() {
        return query;
    }
}
