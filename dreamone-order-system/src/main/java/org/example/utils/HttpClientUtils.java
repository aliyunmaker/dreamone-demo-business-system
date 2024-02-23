package org.example.utils;

import java.net.http.HttpRequest;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.fluent.Request;
import org.apache.http.message.BasicNameValuePair;

/**
 * @author 恬裕
 * @date 2024/1/12
 */
@Slf4j
public class HttpClientUtils {

    public static final String DEFAULT_CHARSETNAME = "UTF-8";

    public static final int DEFAULT_TIMEOUT = 6 * 1000;

    public static final int CONNECT_TIMEOUT = 3 * 1000;

    public static String postUrlAndFormBody(String url, Map<String, String> form) {
        return postUrlAndFormBody(url, form, DEFAULT_TIMEOUT);
    }

    public static String postUrlAndFormBody(String url, Map<String, String> form, int timeOut) {
        try {
            List<NameValuePair> nvps = new ArrayList<>();
            for (Map.Entry<String, String> stringStringEntry : form.entrySet()) {
                nvps.add(new BasicNameValuePair(stringStringEntry.getKey(), stringStringEntry.getValue()));
            }
            return
                Request.Post(url).bodyForm(nvps, Consts.UTF_8).socketTimeout(timeOut).connectTimeout(CONNECT_TIMEOUT)
                .execute().returnContent().asString(Charset.forName(DEFAULT_CHARSETNAME));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
