package com.zed.design.core;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author Zed
 * @date 2019/12/12 下午10:10
 * @contact shadowl91@163.com
 */
@Configuration
public class Config {
    @Value("${reptile.cnki.url}")
    public String cnkiURL;
}
