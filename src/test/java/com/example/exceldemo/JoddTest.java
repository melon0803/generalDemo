package com.example.exceldemo;

import com.google.common.collect.Lists;
import jodd.io.FileUtil;
import jodd.mail.*;
import jodd.mail.att.ByteArrayAttachment;
import jodd.mail.att.FileAttachment;
import jodd.net.MimeTypes;
import jodd.props.Props;
import jodd.typeconverter.Converter;
import jodd.typeconverter.impl.LocalDateConverter;
import jodd.util.StringUtil;
import jodd.util.Wildcard;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Carmelo
 * @date 2018/10/31 - 11:54
 * @since 1.0.0
 */
public class JoddTest {
    /**
     * 读取prop文件中的属性值
     *
     * @throws Exception
     */
    @Test
    public void testProp() throws Exception {

        int i = Wildcard.matchOne("*", "*.xml");

        Props p = new Props();
        try {
            p.load(Paths.get("F:\\generaldemo\\src\\main\\resources\\application.properties").toFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
        String story = p.getValue("a");
        System.out.println(story);
        p.setValue("a", "1234");
        System.out.println(p.getValue("a"));
    }

    /**
     * 发送email
     *
     * @throws Exception
     */
    @Test
    public void testEmail() throws Exception {
        SendMailSession session = null;
        try {
            Email email = new Email();
            email.from("841809961@qq.com");
            email.to("767007496@qq.com");
            email.setSubject("Hellooooooo");
            EmailMessage textMessage = new EmailMessage("Hello!", MimeTypes.MIME_TEXT_PLAIN);
            email.addMessage(textMessage);
            EmailMessage htmlMessage = new EmailMessage(
                    "<html><META http-equiv=Content-Type content=\"text/html; charset=utf-8\">" +
                            "<body><h1>Hey!</h1><img src='cid:c.png'><h2>Hay!</h2></body></html>",
                    MimeTypes.MIME_TEXT_HTML);
            email.addMessage(htmlMessage);
            // 添加验证
            SmtpServer<?> smtpServer = SmtpSslServer.create("smtp.qq.com")
                    .authenticateWith("841809961@qq.com", "。。。").timeout(1000);
            // 创建session
            session = smtpServer.createSession();
            session.open();
            String result = session.sendMail(email);
            System.out.println(result);
            session.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            } finally {
                if (null != session) {
                    session.close();
                }
            }
        }

    /**
     * 字符串操作工具类
     *
     * @throws Exception
     */
    @Test
    public void testString() throws Exception {

        Long aLong = Converter.get().toLong(null,0L);

        String exam = "abcdefg10101010abcdefg";
        String result = "";
        /*
         * replace 字符替换
         */
            // 将字符串exam中的a替换成b
            result = StringUtil.replace(exam, "a", "b");
            // 将字符串exam中的a替换成8,b替换成9
            result = StringUtil.replace(exam, new String[] { "a", "b" }, new String[] { "8", "9" });
            // 将字符串exam中的a替换成b 这里是替换字符
            result = StringUtil.replaceChar(exam, 'a', 'b');
            // 将字符串exam中的a替换成8,b替换成9 这里是替换字符
            result = StringUtil.replaceChars(exam, new char[] { 'a', 'b' }, new char[] { '8', '9' });
            // 将字符串exam中的第一个a替换成b
            result = StringUtil.replaceFirst(exam, "a", "b");
            // 将字符串exam中的第一个a替换成b 这里是替换字符
            result = StringUtil.replaceFirst(exam, 'a', 'b');
            // 将字符串exam中的最后一个a替换成b
            result = StringUtil.replaceLast(exam, "a", "b");
            // 将字符串exam中的最后一个a替换成b 这里是替换字符
            result = StringUtil.replaceLast(exam, 'a', 'b');
            // 将字符串exam中的a和A替换成FF b和B替换成gg 即忽略大小写
            result = StringUtil.replaceIgnoreCase(exam, new String[] { "a", "b" }, new String[] { "FF", "gg" });
        /*
         * remove 字符移除
         */
            // 将字符串exam中的a移除
            result = StringUtil.remove(exam, "a");
            // 将字符串exam中的a移除 移除的是字符
            result = StringUtil.remove(exam, 'a');
            // 将字符串exam中的a b移除 移除的是字符 最后一个参数为无限参数
            result = StringUtil.removeChars(exam, 'a', 'b');
            // 将字符串exam中的a移除
            result = StringUtil.removeChars(exam, "a");
        /*
         * 判断字符串是否为空
         */
            // 判断字符串exam是否为空
            System.out.println(StringUtil.isEmpty(exam));
            // 判断字符串exam是否不为空
            System.out.println(StringUtil.isNotEmpty(exam));
            // 判断字符串exam是否为空 这里的空为去掉空格之后
            System.out.println(StringUtil.isBlank("   "));
            // 判断字符串exam是否不为空 这里的空为去掉空格之后
            System.out.println(StringUtil.isNotBlank("   "));
            // 判断字符串(无限参数)是否都为空 他们之间的关系为并且
            System.out.println(StringUtil.isAllEmpty(exam, "  ", "null"));
            // 判断字符串(无限参数)是否都为空 这里的空为去掉空格之后 他们之间的关系为并且
            System.out.println(StringUtil.isAllBlank(exam, "  ", "null"));
            // 对比字符串exam中的第4索引的字符是不是d
            System.out.println(StringUtil.isCharAtEqual(exam, 4, 'd'));
            // 对比字符串exam中的第4索引的字符是不是 不是d
            System.out.println(StringUtil.isCharAtEscaped(exam, 4, 'd'));
        /*
         * equals安全的字符串对比是否相等 不需要考虑null.equals等问题
         */
            // 判断字符串exam与aaa是否相等
            System.out.println(StringUtil.equals(exam, "aaa"));
            // 判断两个数组是否相等
            System.out.println(StringUtil.equals(new String[] { "aaa" }, new String[] { "aaa", "bbb" }));
            // 判断两个数组是否相等 且忽略大小写
            System.out.println(StringUtil.equalsIgnoreCase(new String[] { "aaa" }, new String[] { "aaa", "bbb" }));
            // 获取字符串bbb在数组中的索引
            System.out.println(StringUtil.equalsOne("bbb", new String[] { "aaa", "bbb" }));
            // 获取字符串bbb在数组中的索引 且忽略大小写
            System.out.println(StringUtil.equalsOneIgnoreCase("bbb", new String[] { "aaa", "bbb" }));
        /*
         * 首字母的更改
         */
            // 首字母大写
            result = StringUtil.capitalize(exam);
            // 首字母小写
            result = StringUtil.uncapitalize(exam);
        /*
         * split字符串分割
         */
            // 将字符串按 , 分割
            String[] array = StringUtil.split("1,2,3,4,5,6,7,8", ",");
        /*
         * indexOf 获取字符串中的字符索引
         */
        /*
         * Strips, crops, trims and cuts
         */
            // 若这个字符串以a为开头,则去掉a
            result = StringUtil.stripLeadingChar(exam, 'a');
            // 若这个字符串以g为结尾,则去掉g
            result = StringUtil.stripTrailingChar(exam, 'g');
            // 若该字符串为"" 则返回null 若不是则返回字符串
            result = StringUtil.crop("");
            // 裁剪数组 将""变成null
            StringUtil.cropAll(new String[] { "", " " });
            // 去掉字符串两边的空格
            result = StringUtil.trimDown("  aa  ");
            // 去掉字符串左边的空格
            result = StringUtil.trimLeft("  aa  ");
            // 去掉字符串右边的空格
            result = StringUtil.trimRight("  aa  ");
            // 去掉字符串右边的空格
            String[] array2 = new String[] { "  aa  ", "  b  b" };
        /*
         * 去掉数组内空格
         */
            StringUtil.trimAll(array2);
            StringUtil.trimDownAll(array2);
            for (String string : array2)
            {
                System.out.println(string);
            }
        /*
         * 切割字符串
         */
            // 从字符串的f字符开始切割字符串 保留f
            result = StringUtil.cutFromIndexOf(exam, 'f');
            // 从字符串的fg字符串开始切割字符串 保留fg
            result = StringUtil.cutFromIndexOf(exam, "fg");
            // 检查字符串是否为abc开头,若为此开头,则切割掉abc
            result = StringUtil.cutPrefix(exam, "abc");
            // 检查字符串是否为efg结尾,若为此结尾,则切割掉efg
            result = StringUtil.cutSuffix(exam, "efg");
            // 检查字符串是否为efg开头或结尾,若为此开头或结尾,则切割掉efg
            result = StringUtil.cutSurrounding(exam, "efg");
            // 检查字符串是否为abc开头efg结尾,若为为abc开头efg结尾,则切割掉
            result = StringUtil.cutSurrounding(exam, "abc", "efg");
            // 截取到字符串的f字符开始切割字符串 不保留f
            result = StringUtil.cutToIndexOf(exam, 'f');
            // 截取到字符串的fg字符串开始切割字符串 不保留fg
            result = StringUtil.cutToIndexOf(exam, "fg");
        /*
         * 其他很多小巧的方法,可以自行研究
         */
            System.out.println(result);
    }


}
