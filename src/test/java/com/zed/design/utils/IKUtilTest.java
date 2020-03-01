package com.zed.design.utils;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Zed
 * @date 2020/3/1 下午11:40
 * @contact shadowl91@163.com
 */
@SpringBootTest
class IKUtilTest {

    @Test
    public void test() throws IOException {
        String text = "中国成立九十周年了！";
        List<String> list = IKUtil.cut(text);
        list.forEach(System.out::println);
    }
}