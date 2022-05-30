package ru.ssk.restvoting;

import org.assertj.core.api.Assertions;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import ru.ssk.restvoting.exception.CauseExceptionNotFoundException;
import ru.ssk.restvoting.model.User;
import ru.ssk.restvoting.web.json.JsonUtil;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class TestUtil {
    public static RequestPostProcessor userAuth(User user) {
        return SecurityMockMvcRequestPostProcessors.authentication(new UsernamePasswordAuthenticationToken(user.getName(), user.getPassword()));
    }

    public static RequestPostProcessor userHttpBasic(User user) {
        return SecurityMockMvcRequestPostProcessors.httpBasic(user.getName(), user.getPassword());
    }

    public static String getContent(MvcResult result) throws UnsupportedEncodingException {
        return result.getResponse().getContentAsString();
    }

    public static <T> T readValueFromMvcResult(MvcResult result, Class<T> clazz) throws UnsupportedEncodingException {
        return JsonUtil.readValue(getContent(result), clazz);
    }

    public static <T> List<T> readValuesFromMvcResult(MvcResult result, Class<T> clazz) throws UnsupportedEncodingException {
        return JsonUtil.readValues(getContent(result), clazz);
    }

    public static <T> T readFromJson(ResultActions action, Class<T> clazz) throws UnsupportedEncodingException {
        return JsonUtil.readValue(getContent(action.andReturn()), clazz);
    }

    public static <T> ResultMatcher contentJson(T expected, Class<T> clazz) {
        return result -> Assertions.assertThat(readValueFromMvcResult(result, clazz))
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    public static <T> ResultMatcher contentJson(Iterable<T> expected, Class<T> clazz) {
        return result -> Assertions.assertThat(readValuesFromMvcResult(result, clazz))
                .usingFieldByFieldElementComparator()
                .containsExactlyElementsOf(expected);
    }

    public static <T extends Throwable> Throwable getCauseException(Class<T> causeClass, String MessageSubstring, Throwable t) {
        Throwable cause = t;
        do {
            if (cause.getClass() == causeClass &&
                    (MessageSubstring.isEmpty() || cause.getMessage().toLowerCase().contains(MessageSubstring.toLowerCase())))
                return cause;
            cause = cause.getCause();
        } while (cause != null);
        return new CauseExceptionNotFoundException();
    }

}
