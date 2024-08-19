package org.example.utils;

import org.example.constant.ErrorInfo;

import java.util.Random;

/**
 * @author 恬裕
 * @date 2024/1/15
 */
public class RequestUtils {

    public static int getRandomNumber(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }

    static boolean getRandomBoolean() {
        Random random = new Random();
        return random.nextBoolean();
    }

    public static int getRandomCallTime() {
        Random random = new Random();
        int randomNumber = random.nextInt(100);
        if (randomNumber < 95) {
            return getRandomNumber(50, 200);
        } else {
            return getRandomNumber(200, 3500);
        }
    }

    public static ErrorInfo getErrorInfo() {
        ErrorInfo errorInfo;
        int randomNumber = getRandomNumber(1, 100);
        if (randomNumber <= 80) {
            errorInfo = ErrorInfo.OK;
        } else if (randomNumber <= 90) {
            errorInfo = ErrorInfo.INVALID_PARAMETER;
        } else if (randomNumber <= 95) {
            errorInfo = ErrorInfo.SYSTEM_ERROR;
        } else {
            errorInfo = ErrorInfo.NO_PERMISSION;
        }
        return errorInfo;
    }

    public static String getRandomOrderType() {
        Random random = new Random();
        int randomNumber = random.nextInt(100);
        if (randomNumber < 30) {
            return "Daily";
        } else if (randomNumber < 55) {
            return "Clothing";
        } else if (randomNumber < 75) {
            return "Electronics";
        } else if (randomNumber < 90) {
            return "Beauty";
        } else {
            return "Furniture";
        }
    }
}
