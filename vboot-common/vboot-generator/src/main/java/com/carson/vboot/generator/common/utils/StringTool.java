package com.carson.vboot.generator.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ibeetl 自定义函数
 */
public class StringTool {
    private static Pattern linePattern = Pattern.compile("_(\\w)");
    private static Pattern humpPattern = Pattern.compile("[A-Z]");

    /**
     * 下划线转驼峰
     */
    public String lineToHump(String str) {
        str = str.toLowerCase();
        Matcher matcher = linePattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }


    /**
     * 驼峰转下划线,效率比上面高
     */
    public String humpToLine(String str) {
        Matcher matcher = humpPattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 首字母大写
     *
     * @param str
     * @return
     */
    public String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }


    public String toUpperCase(String str) {
        return str.toUpperCase();
    }

    public static void main(String[] args) {
        StringTool stringTool = new StringTool();
        String lineToHump = stringTool.lineToHump("f_parent_no_leader");
        System.out.println(lineToHump);// fParentNoLeader
        System.out.println(stringTool.humpToLine(lineToHump));// f_parent_no_leader
    }
}
