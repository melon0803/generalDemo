package com.example.exceldemo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.example.jsonentity.Student;
import com.example.jsonentity.Teacher;
import org.junit.Test;

import java.util.ArrayList;

/**
 * @author Carmelo
 * @date 2018/11/1 - 14:24
 * @since 1.0.0
 */
public class FastJsonTest {
    //json字符串-简单对象型
    private static final String JSON_OBJ_STR = "{\"studentName\":\"liming\",\"studentAge\":12}";
    //json字符串-数组类型
    private static final String  JSON_ARRAY_STR = "[{\"studentName\":\"lily\",\"studentAge\":12},{\"studentName\":\"lucy\",\"studentAge\":15}]";
    //复杂格式json字符串
    private static final String  COMPLEX_JSON_STR =
            "{\"teacherName\":\"crystall\"," +
                    "\"teacherAge\":27," +
                    "\"course\":" + "{\"courseName\":\"english\",\"code\":1270}," +
                    "\"students\"" +
                    ":[{\"studentName\":\"lily\",\"studentAge\":12},{\"studentName\":\"lucy\",\"studentAge\":15}]}";

    /**
     * 1 json字符串-简单对象型与JSONObject之间的转换
     */
    @Test
    public void testJSONStrToJSONObject() {
        JSONObject jsonObject = JSON.parseObject(JSON_OBJ_STR);
        System.out.println(jsonObject.getString("studentName")+":"+jsonObject.getInteger("studentAge"));
    }

    /**
     * 2 json字符串-数组类型与JSONArray之间的转换
     */
    @Test
    public void testJSONStrToJSONArray() {
        JSONArray jsonArray = JSON.parseArray(JSON_ARRAY_STR);
        //遍历方式1
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            System.out.println(jsonObject.getString("studentName")+":"+jsonObject.getString("studentAge"));
        }
        //便利方式2
        for (Object Object:jsonArray) {
            JSONObject object = (JSONObject) Object;
            System.out.println(object.getString("studentName")+":"+object.getString("studentAge"));
        }
    }

    /**
     * 3 复杂json格式字符串与JSONObject之间的转换
     */
    @Test
    public void testComplexJSONStrToJSONObject() {
        JSONObject jsonObject = JSON.parseObject(COMPLEX_JSON_STR);
        String jsonString = jsonObject.toJSONString();
        System.out.println(jsonString);
        System.out.println(jsonObject.getString("teacherName"));
        System.out.println(jsonObject.getInteger("teacherAge"));
        System.out.println(jsonObject.getJSONObject("course"));
        System.out.println(jsonObject.getJSONArray("students"));
    }

    /**
     * 4 json字符串-简单对象与JavaBean_obj之间的转换
     */
    @Test
    public void testJSONStrToJavaBeanObj(){
        Student student = JSON.parseObject(JSON_OBJ_STR, new TypeReference<Student>() {
        });
        System.out.println(student.getStudentName()+":"+student.getStudentAge());
    }

    /**
     * 5 json字符串-数组类型与JavaBean_List之间的转换
     */
    @Test
    public void testJSONStrToJavaBeanList(){
        ArrayList<Student> studentArrayList = JSON.parseObject(JSON_ARRAY_STR, new TypeReference<ArrayList<Student>>() {
        });
        for (Student student:studentArrayList) {
            System.out.println(student.getStudentName()+":"+student.getStudentAge());
        }
    }

    /**
     * 复杂json格式字符串与JavaBean_obj之间的转换
     */
    @Test
    public void testComplexJSONStrToJavaBean(){
        Teacher teacher = JSON.parseObject(COMPLEX_JSON_STR, new TypeReference<Teacher>() {
        });
        System.out.println(teacher.getTeacherName());
        System.out.println(teacher.getTeacherAge());
        System.out.println(teacher.getCourse().toString());
        System.out.println(teacher.getStudents().toString());
    }

    //ToDo 火箭对湖人

}
