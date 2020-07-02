package com.jiopeel.core.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
public class BaseUtil {

    private static final String serialVersionUID = "serialVersionUID";

    private static EncrypAES de1;

    private static ObjectMapper objectMapper;

    private static SnowFlakeUtil snowFlakeUtil;


    /**
     * 解析json
     *
     * @param content
     * @param valueType
     * @return
     */
    public static <T> T fromJson(String content, Class<T> valueType) {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }
        try {
            return objectMapper.readValue(content, valueType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 生成json
     *
     * @param object
     * @return
     */
    public static String toJson(Object object) {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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

    /**
     * 转int
     *
     * @param param
     * @return
     */
    public static int parseInt(Object param) {
        if (empty(param)) {
            return 0;
        } else {
            return Integer.parseInt(String.valueOf(param));
        }
    }

    /**
     * 转long
     *
     * @param param
     * @return
     */
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
     * @return String
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-","");
    }

    /**
     * 雪花算法获取ID 代替UUID
     * @return String
     */
    public static String getSnowFlakeID() {
        if (snowFlakeUtil == null)
            snowFlakeUtil = new SnowFlakeUtil(1L,1L);
        long id =0L;
        try {
            id = snowFlakeUtil.nextId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return String.valueOf(id);
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
     * @Description :url encode加密
     * @Param: url
     * @Return: String
     * @auhor:lyc
     * @Date:2019/11/1 22:53
     */
    public static String encodeURL(String url) {
        String str = "";
        try {
            str = URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
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
        Map<String, String> obj = new HashMap<>();
        for (int i = 0; i < params.length; i++) {
            String[] param = params[i].split("=");
            if (param.length >= 2) {
                String key = param[0];
                String value = param[1];
                for (int j = 2; j < param.length; j++) {
                    value += "=" + param[j];
                }
                obj.put(key, value);
            }
        }
        return toJson(obj);
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

    /**
     * 将list<E>转换为数组
     *
     * @param data list<Bean>集合
     * @return Bean[] 对象
     */
    @SuppressWarnings("unchecked")
    public static <E> E[] arrs(List<E> data) {
        int i;
        E[] result = null;
        if (!BaseUtil.empty(data) && (i = data.size()) > 0) {
            result = (E[]) Array.newInstance(data.get(0).getClass(), i);
            data.toArray(result);
        }
        return result;
    }

    /**
     * 将list<E>转换为数组
     *
     * @param data list<Bean>集合
     * @return Bean[] 对象
     */
    @SuppressWarnings("unchecked")
    public static <E> E[] arrs(Set<E> data) {
        int i;
        E[] result = null;
        if (!BaseUtil.empty(data) && (i = data.size()) > 0) {
            result = (E[]) Array.newInstance(data.iterator().next().getClass(), i);
            data.toArray(result);
        }
        return result;
    }

    /**
     * 将T[]数组转换为list
     *
     * @param data Bean[] 数组集合
     * @return List<Bean> 对象
     */
    public static <E> List<E> list(E[] data) {
        if (BaseUtil.empty(data))
            return null;
        List<E> result = new ArrayList<E>(data.length);
        for (E bean : data) {
            result.add(bean);
        }
        return result;
    }

    /**
     * 将Collection<E>数组转换为list
     *
     * @param data Collection<E>集合
     * @return List<Bean> 对象
     */
    public static <E> List<E> list(Collection<E> data) {
        if (BaseUtil.empty(data))
            return null;
        List<E> result = new ArrayList<E>(data.size());
        for (E bean : data) {
            result.add(bean);
        }
        return result;
    }

    /**
     * 获取对象中的所有字段成员 包括父类
     *
     * @param object
     * @return Field[]
     */
    public static Field[] getAllFields(Object object) {
        Class clazz = object.getClass();
        return getAllFields(clazz);
    }

    /**
     * 获取对象中的所有字段成员 包括父类
     *
     * @param clazz
     * @return Field[]
     */
    public static Field[] getAllFields(Class<?> clazz) {
        List<Field> fieldList = new ArrayList<Field>();
        while (clazz != null) {
            fieldList.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }
        List<Field> newFieldList = new ArrayList<Field>();
        for (Field field : fieldList) {
            String name = field.getName();
            if (serialVersionUID.equals(name))
                continue;
            newFieldList.add(field);
        }
        Field[] fields = new Field[newFieldList.size()];
        return newFieldList.toArray(fields);
    }


    /**
     * @Description :获取字段对应的值
     * @param: field  字段
     * @param: bean
     * @Return: Object 返回值
     * @auhor:lyc
     * @Date:2019/12/21 11:48
     */
    public static <T> Object getFieldVal(Field field, T bean) {
        field.setAccessible(true);
        Object obj = null;
        if (empty(bean))
            return obj;
        try {
            obj = field.get(bean);
            if (obj instanceof String)
                obj = String.valueOf(obj);
            if (obj instanceof Integer)
                obj = BaseUtil.parseInt(obj);
            if (obj instanceof Date)
                obj = BaseUtil.Dateformat((Date) obj);
            if (obj instanceof BigDecimal)
                obj = ((BigDecimal) obj).doubleValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    /**
     * 驼峰转下划线
     *
     * @param c
     * @return String
     */
    public static String camel2under(String c) {
        String separator = "_";
        c = c.replaceAll("([a-z])([A-Z])", "$1" + separator + "$2").toLowerCase();
        return c;
    }


    /**
     * 下划线转驼峰
     *
     * @param s
     * @return String
     */
    public static String under2camel(String s) {
        String separator = "_";
        String under = "";
        s = s.toLowerCase().replace(separator, " ");
        String sarr[] = s.split(" ");
        for (int i = 0; i < sarr.length; i++) {
            String w = sarr[i].substring(0, 1).toUpperCase() + sarr[i].substring(1);
            under += w;
        }
        return under;
    }

    /**
     * 将list拆分
     *
     * @param items 查询条件
     * @param steps 划分大小
     * @return 拆分之后的结果
     * @auhor:lyc
     * @Date:2019/12/21 11:48
     */
    public static <E> List<List<E>> splitList(List<E> items, int steps) {
        List<List<E>> beans = new ArrayList<>(steps);
        if (items == null || items.isEmpty())
            return beans;
        if (steps <= 0 || steps >= Integer.MAX_VALUE)
            return beans;
        int len = items.size();
        int[] nums = new int[len / steps + ((len % steps == 0) ? 0 : 1)];
        for (int i = 0; i < len; i++) {
            nums[i] = (i + 1) * steps;
            if (nums[i] >= len)
                break;
        }
        for (int i = 0; i < nums.length; i++) {
            int j = i == 0 ? 0 : nums[i - 1], t = nums[i];
            List<E> list = items.subList(j, t > len ? len : t);
            if (list != null && list.size() != 0)
                beans.add(list);
            if (t >= len)
                break;
        }
        return beans;
    }
}
