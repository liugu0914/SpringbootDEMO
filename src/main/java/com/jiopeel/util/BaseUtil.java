package com.jiopeel.util;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.ClassUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Slf4j
public class BaseUtil {

    protected static final Logger sys = LogManager.getLogger("sys");

    private static EncrypAES de1 = null;

    /**
     * 获取原始路径
     *
     * @param name
     * @return
     */
    public static InputStream getRealPath(String name) {
        if (name != null && !name.startsWith("/")) {
            name = "/" + name;
        }

        if (!isJarRuning()) {
            return BaseUtil.class.getResourceAsStream(name);
        } else {
            String path = getSysRootPath();
            Object input = null;

            try {
                input = new FileInputStream(path + name);
            } catch (FileNotFoundException var4) {
                sys.warn("未找到:" + path + name, var4.getMessage());
            }

            if (input == null) {
                input = BaseUtil.class.getResourceAsStream(name);
            }

            return (InputStream) input;
        }
    }


    public static boolean isJarRuning() {
        String path = null;

        try {
            path = ClassUtils.getDefaultClassLoader().getResource("").getPath();
        } catch (NullPointerException var2) {
            throw new RuntimeException("构建失败的版本,未加入系统jar包,需要检查构建pom文件!");
        }

        return path == null || path.indexOf(".jar") != -1;
    }


    public static String getSysRootPath() {
        String path = ClassUtils.getDefaultClassLoader().getResource("").getPath().replace("file:", "").replace("jar:", "");
        int i;
        if ((i = path.indexOf(".jar")) != -1) {
            path = path.substring(0, i);
            path = path.substring(0, path.lastIndexOf("/"));
        }

        return path.substring(1);
    }

    /**
     * 判断空
     *
     * @param param
     * @return
     */
    public static boolean empty(Object param) {
        return param == null || "".equals(param) || "null".equals(param);
    }

    public static int parseInt(Object param) {
        if (empty(param)) {
            return 0;
        } else {
            return (Integer) param;
        }
    }

    public static long parseLong(Object param) {

        if (empty(param)) {
            return 0;
        } else {
            return (Long) param;
        }
    }

    /**
     * MD5加密
     *
     * @param key
     * @return
     */
    public static String MD5(String key) {
        byte[] secretBytes = null;
        try {
            secretBytes = MessageDigest.getInstance("md5").digest(
                    key.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("没有这个md5算法！");
        }
        String md5code = new BigInteger(1, secretBytes).toString(16);
        for (int i = 0; i < 32 - md5code.length(); i++) {
            md5code = "0" + md5code;
        }
        return md5code;
    }

    /**
     * AES加密
     *
     * @param key return result
     */
    public static String AES(String key) {
        if (empty(key))
            return "";
        String result = "";
        try {
            if (de1 == null)
                de1 = new EncrypAES();
            byte[] encontent = de1.Encrytor(key);
            result = parseByte2HexStr(encontent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * AES解密
     *
     * @param key return result
     */
    public static String AESdec(String key) {
        if (empty(key))
            return "";
        String result = "";
        try {
            byte[] bytes = parseHexStr2Byte(key);
            if (de1 == null)
                de1 = new EncrypAES();
            byte[] decontent = de1.Decryptor(bytes);
            result = new String(decontent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 将二进制转换成16进制
     *
     * @param buf
     * @return
     */
    public static String parseByte2HexStr(byte[] buf) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 获取UUID
     *
     * @return string
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 将16进制转换为二进制
     *
     * @param hexStr
     * @return
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    /**
     * 自动将字符串补位,后面加上空格
     *
     * @param text        字符串
     * @param coverLength 总长度
     * @return
     */
    public static String cover(String text, int coverLength) {
        int len = text == null ? 0 : text.length();
        if (coverLength - len <= 0)
            return text;
        byte[] c = new byte[coverLength - len];
        for (int i = 0; i < coverLength - len; i++)
            c[i] = ' ';
        return text == null ? new String(c) : text + new String(c);
    }


    /**
     * @Description :时间格式化
     * @Param: prefix
     * @Param: date
     * @Return: String
     * @auhor:lyc
     * @Date:2019/11/1 22:53
     */
    public static String Dateformat(String prefix, Date date) {
        if (empty(prefix))
            prefix = "yyyy-MM-dd HH:mm:ss";
        if (empty(date))
            date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(prefix);
        return sdf.format(date);
    }

    /**
     * @Description :时间格式化
     * @Param: date
     * @Return: String
     * @auhor:lyc
     * @Date:2019/11/1 22:53
     */
    public static String Dateformat(Date date) {
        return Dateformat("", date);
    }

    /**
     * @Description :时间格式化
     * @Param: date
     * @Return: String
     * @auhor:lyc
     * @Date:2019/11/1 22:53
     */
    public static String Dateformat(Long date) {
        return Dateformat("", new Date(date));
    }

    /**
     * @Description :url转json
     * @Param: date
     * @Return: String
     * @auhor:lyc
     * @Date:2019/11/1 22:53
     */
    public static String Url2JSON(String paramStr) {
        String[] params = paramStr.split("&");
        JSONObject obj = new JSONObject();
        for (int i = 0; i < params.length; i++) {
            String[] param = params[i].split("=");
            if (param.length >= 2) {
                String key = param[0];
                String value = param[1];
                for (int j = 2; j < param.length; j++) {
                    value += "=" + param[j];
                }
                try {
                    obj.put(key, value);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return obj.toString();
    }


    /**
     * 对象转url
     *
     * @param clazz
     * @return
     */
    public static String Object2Url(Object clazz) {
        // 遍历属性类、属性值
        Field[] fields = clazz.getClass().getDeclaredFields();

        StringBuilder requestURL = new StringBuilder();
        try {
            boolean flag = true;
            String property, value;
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                // 允许访问私有变量
                field.setAccessible(true);
                // 属性名
                property = field.getName();
                // 属性值
                value = field.get(clazz).toString();
                if (empty(value))
                    continue;
                String params = property + "=" + value;
                if (flag) {
                    requestURL.append(params);
                    flag = false;
                } else {
                    requestURL.append("&" + params);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("URL参数为：" + clazz.toString());
        }
        return requestURL.toString();
    }
}
