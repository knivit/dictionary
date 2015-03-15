package com.tsoft.dictionary.server.app.web.login;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.tsoft.dictionary.server.app.UserInfo;
import com.tsoft.dictionary.server.app.web.ServerServlet;
import java.util.UUID;
import java.util.logging.Logger;

public class LoginServlet extends ServerServlet implements LoginServletInterface {
    private static final Logger logger = Logger.getLogger(LoginServlet.class.getName());

    @Override
    public UserInfoResponseTO getUserInfo(UserInfo userInfo) {
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();

        UserInfoResponseTO responseTO = populateUserInfo(user, userService);
        return responseTO;
    }

    private UserInfoResponseTO populateUserInfo(User user, UserService userService) {
        UserInfoResponseTO responseTO = new UserInfoResponseTO();
        if (user == null) {
            responseTO.setUserId(UUID.randomUUID().toString());
            responseTO.setUserName(UserInfo.ANONIMOUS_NAME);
            responseTO.setLoginUrl(userService.createLoginURL("/"));
            responseTO.setIsAnonimous(true);
        } else {
            responseTO.setUserId(user.getUserId());
            responseTO.setUserName(user.getNickname());
            responseTO.setLogoutUrl(userService.createLogoutURL("/"));
            responseTO.setIsAnonimous(false);
        }

        return responseTO;
    }
}
