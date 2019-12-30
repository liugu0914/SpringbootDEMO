package com.jiopeel.core.util;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.*;
import java.util.Map;

public class FreemarkerUtil {

    private static FreemarkerUtil util;
    private static Configuration cfg;
    private static final String TEMPLATE = "/templates";
    private static final String UTF_8 = "utf-8";

    public FreemarkerUtil() {
    }

    /**
     * 初始化
     */
    public static FreemarkerUtil getInstance() {
        if (util == null) {
            cfg = new Configuration(Configuration.VERSION_2_3_28);
            cfg.setClassForTemplateLoading(FreemarkerUtil.class, TEMPLATE);
            util = new FreemarkerUtil();
        }
        return util;
    }

    /**
     * 设置输出模板
     * @param fname   模板路劲
     */
    private Template getTemplate(String fname) {
        try {
            return cfg.getTemplate(fname,UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过标准输出流输出模板的结果
     *
     * @param map   数据对象
     * @param fname 模板文件
     */
    public void sPrint(Map map, String fname) {
        try {
            Template template = getTemplate(fname);
            template.process(map, new PrintWriter(System.out));
        } catch (TemplateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 基于文件的输出
     *
     * @param map     参数设置
     * @param fname   模板文件
     * @param outpath 输出路径
     */
    public void fPrint(Map<String, Object> map, String fname, String outpath) {
        try {
            Template template = getTemplate(fname);
            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(outpath)), UTF_8));
            template.process(map, out);
            out.flush();
            out.close();
        } catch (TemplateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
