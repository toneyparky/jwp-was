package webserver.http.response;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import webserver.http.request.HttpRequest;
import webserver.http.request.StaticFiles;
import webserver.utils.FileIoUtils;

public class HttpResponse {
    private static final String HEADER_VALUE_SEPARATOR = "; ";
    private static final String ENCODING_CHARSET_UTF_8 = "charset=UTF-8";
    private static final String TEMPLATES = "./templates";
    private static final String LINE_SEPARATOR = System.lineSeparator();

    private static final Logger logger = LoggerFactory.getLogger(HttpResponse.class);

    private final DataOutputStream dataOutputStream;

    public HttpResponse(DataOutputStream dataOutputStream) {
        this.dataOutputStream = dataOutputStream;
    }

    public void responseOk(HttpRequest httpRequest) throws IOException, URISyntaxException {
        byte[] body = FileIoUtils.loadFileFromClasspath(
            StaticFiles.getDirectoryEndsWith(httpRequest.getHttpPath()) + httpRequest.getHttpPath());
        response200Header(httpRequest.getContentType(), body.length);
        responseBody(body);
    }

    private void response200Header(String contentType, int lengthOfBodyContent) {
        try {
            dataOutputStream.writeBytes("HTTP/1.1 200 OK" + LINE_SEPARATOR);
            dataOutputStream.writeBytes(
                "Content-Type: " + contentType + HEADER_VALUE_SEPARATOR + ENCODING_CHARSET_UTF_8 + LINE_SEPARATOR);
            dataOutputStream.writeBytes("Content-Length: " + lengthOfBodyContent + LINE_SEPARATOR);
            dataOutputStream.writeBytes(LINE_SEPARATOR);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void responseBody(byte[] body) {
        try {
            dataOutputStream.write(body, 0, body.length);
            dataOutputStream.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public void responseFound(String redirectPath) {
        response302Header(redirectPath);
    }

    private void response302Header(String url) {
        try {
            dataOutputStream.writeBytes("HTTP/1.1 302 FOUND" + LINE_SEPARATOR);
            dataOutputStream.writeBytes("Location: " + url + LINE_SEPARATOR);
            dataOutputStream.writeBytes(LINE_SEPARATOR);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public void responseNotFound() throws IOException, URISyntaxException {
        byte[] body = FileIoUtils.loadFileFromClasspath(TEMPLATES + "/common/not_found.html");
        response404Header();
        responseBody(body);
    }

    private void response404Header() {
        try {
            dataOutputStream.writeBytes("HTTP/1.1 404 NOT FOUND" + LINE_SEPARATOR);
            dataOutputStream.writeBytes(LINE_SEPARATOR);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public void responseInternalServerError() {
        try {
            byte[] body = FileIoUtils.loadFileFromClasspath(TEMPLATES + "/common/internal_server_error.html");
            response500Header();
            responseBody(body);
        } catch (IOException | URISyntaxException e) {
            logger.error(e.getMessage());
        }
    }

    private void response500Header() {
        try {
            dataOutputStream.writeBytes("HTTP/1.1 500 INTERNAL SERVER ERROR" + LINE_SEPARATOR);
            dataOutputStream.writeBytes(LINE_SEPARATOR);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
