package org.example.utils;

import java.util.Random;

import org.example.constant.ErrorInfo;

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

    public static ErrorInfo getErrorInfo(String status) {
        ErrorInfo errorInfo;
//        int randomNumber = getRandomNumber(1, 100);
//        if (randomNumber <= 80) {
//            errorInfo = ErrorInfo.OK;
//        } else if (randomNumber <= 90) {
//            errorInfo = ErrorInfo.NO_ADDRESS;
//        } else if (randomNumber <= 95) {
//            errorInfo = ErrorInfo.BUYER_TOO_MANY_UNPAID_ORDERS;
//        } else {
//            errorInfo = ErrorInfo.USING_PROMOTION_FAIL;
//        }
        if ("OK".equals(status)) {
            errorInfo = ErrorInfo.OK;
        } else {
            errorInfo = ErrorInfo.USING_PROMOTION_FAIL;
        }
        return errorInfo;
    }
}
