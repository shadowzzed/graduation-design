package com.zed.design.core.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zed
 * @date 2019/12/16 上午12:43
 * @contact shadowl91@163.com
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Paper {

    private String name;

    private String summary;

    private String author;

    private String source;

    private String db;

    private String date;

    private String ref;

    private String downloads;
}
