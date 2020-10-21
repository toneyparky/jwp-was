package application.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import application.db.DataBase;
import application.model.User;
import webserver.annotation.Controller;
import webserver.annotation.RequestMapping;
import webserver.http.request.HttpMethod;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;
import webserver.http.response.HttpStatus;

@Controller
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @RequestMapping(path = "/user/create", method = HttpMethod.POST)
    public static void create(HttpRequest request, HttpResponse response) {
        logger.debug("request : {}, request : {}", request, response);

        User user = new User(
            request.getHttpBodyValueOf("userId"),
            request.getHttpBodyValueOf("password"),
            request.getHttpBodyValueOf("name"),
            request.getHttpBodyValueOf("email")
        );
        DataBase.addUser(user);
        logger.debug("Saved User: {}", DataBase.findUserById(request.getHttpBodyValueOf("userId")));

        response.addHttpHeader("Location", "/index.html");
        response.setHttpStatus(HttpStatus.FOUND);
    }
}
