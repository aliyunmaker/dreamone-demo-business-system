package org.example.constant;

import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Properties;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 恬裕
 * @date 2024/1/2
 */
@Slf4j
public class CommonConstants {

    public static final String CONFIG_PATH = System.getProperty("user.home") + "/config/";

    public static final Map<String,String> LOGIN_USER_MAP;

    static {
        Properties properties = loadProperties();
        String loginUserString = properties.getProperty("dreamone.login_user");
        LOGIN_USER_MAP = JSON.parseObject(loginUserString, new TypeReference<Map<String, String>>() {});
    }

    public static Properties loadProperties() {
        Properties properties = new Properties();
        log.info("user.dir: " + System.getProperty("user.dir"));
        try {
            InputStream ins = CommonConstants.class.getResourceAsStream("/application.properties");
            if (ins == null) {
                ins = new FileInputStream(CommonConstants.CONFIG_PATH + "/application.properties");
                log.info("Used user.dir: " + System.getProperty("user.dir"));
            }
            properties.load(ins);
            ins.close();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return properties;
    }

}
