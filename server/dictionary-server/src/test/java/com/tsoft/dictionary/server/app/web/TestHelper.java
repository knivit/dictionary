package com.tsoft.dictionary.server.app.web;

import com.tsoft.dictionary.server.app.Inject;
import com.tsoft.dictionary.server.app.UserInfo;
import com.tsoft.dictionary.server.app.web.dictionary.DictionaryServletMock;
import com.tsoft.dictionary.server.app.web.login.UserInfoResponseTO;
import java.lang.reflect.Field;
import java.util.Random;
import java.util.UUID;
import javax.servlet.ServletContext;
import org.junit.Ignore;

@Ignore
public final class TestHelper {
    public static final String DICT01_NAME = "dict01";
    public static final String DICT02_NAME = "dict02";

    private static UserInfo userInfo;

    private static Random random = new Random();

    private static ServletContext dictionaryServletContext = new MockServletContext() {
        @Override
        public String getRealPath(String path) {
            return DictionaryServletMock.class.getResource(path.substring(1)).getFile();
        }
    };

    private TestHelper() { }

    public static synchronized UserInfo getDefaultUserInfo() {
        if (userInfo == null) {
            UserInfoResponseTO to = new UserInfoResponseTO();
            to.setUserId("junit");
            to.setUserName("JUnit Test");
            to.setIsAnonimous(false);
            userInfo = new UserInfo(to);
        }
        return userInfo;
    }

    public static ServletContext getDictionaryServletContext() {
        return dictionaryServletContext;
    }

    public static void injectServlet(Object test) throws InstantiationException, IllegalAccessException {
        Field[] fields = test.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (isResourceInjection(field)) {
                ServerServlet servlet = (ServerServlet)field.getType().newInstance();
                field.set(test, servlet);
            }
        }
    }

    public static String getUniqueString() {
        return UUID.randomUUID().toString();
    }


    public static <E extends Object> E getRandomElement(E[] elements) {
        if (elements == null || elements.length == 0) {
            return null;
        }

        int index = random.nextInt(elements.length);
        return elements[index];
    }

    public static int getRandomInteger(int minValueInclusive, int maxValueExclusive) {
        int n = random.nextInt(maxValueExclusive - minValueInclusive);
        return minValueInclusive + n;
    }

    private static boolean isResourceInjection(Field field) {
        return field.isAnnotationPresent(Inject.class) && ServerServlet.class.isAssignableFrom(field.getType());
    }
}
