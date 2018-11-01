package com.example.jsonentity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author Carmelo
 * @date 2018/11/1 - 14:47
 * @since 1.0.0
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class Teacher {
    private String teacherName;

    private Integer teacherAge;

    private Course course;

    private List<Student> students;
}
