package com.ouweicong.common.utils;

import java.util.Random;

public class RandomNumberGenerator {
    //生成随机数
    public static String generateRandomNumber(int length) {
        StringBuilder sb = new StringBuilder(length);
        Random random = new Random();

        // 确保生成的数足够长以截取到指定长度（可能包含非数字字符）
        while (sb.length() < length) {
            int number = random.nextInt(10); // 生成0-9之间的随机数
            sb.append(number);
        }

        // 如果生成的数超过了12位，则截取前12位
        return sb.toString().substring(0, length);
    }
}
