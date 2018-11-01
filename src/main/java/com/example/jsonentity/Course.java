package com.example.jsonentity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author Carmelo
 * @date 2018/11/1 - 14:46
 * @since 1.0.0
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class Course {
    private String courseName;

    private Integer code;
}
