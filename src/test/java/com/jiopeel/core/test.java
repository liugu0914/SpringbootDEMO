package com.jiopeel.core;

import com.alibaba.fastjson.JSON;

import java.util.Arrays;
import java.util.List;

public class test {
    public static void main(String[] args) {
        String[] strings=null;
        if (strings==null || strings.length==0)
            strings=new String[10];
        List<String> strlist = Arrays.asList(strings);
        List<String> ns =Arrays.asList(new String[]{"null","asd"});
        for (String n : ns) {
            if (strlist.contains(n))
                System.out.println(n);
        }
        String s = JSON.toJSONString(strlist);
        System.out.println(s);

        List<String> splitfield = Arrays.asList("".split(","));
        s = JSON.toJSONString(splitfield);
        System.out.println(s);
    }
}
