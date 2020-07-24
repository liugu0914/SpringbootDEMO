package com.jiopeel.core.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jiopeel.sys.bean.result.PermissionResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.beans.BeanUtils;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
public class BaseUtil {

    private static final String serialVersionUID = "serialVersionUID";

    private static EncrypAES de1;

    private static ObjectMapper objectMapper;

    private static SnowFlakeUtil snowFlakeUtil;


    /**
     * 获取泛型的Map Type
     *
     * @param main
     * @param elementClasses 元素类
     * @return JavaType Java类型
     */
    public static JavaType getJavaType(Class<?> main, Class<?>... elementClasses) {
        return objectMapper.getTypeFactory().constructParametricType(main, elementClasses);
    }


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
     * 解析json
     *
     * @param content
     * @param TypeReference
     * @return
     */
    public static <T> T fromJson(String content, TypeReference<T> TypeReference) {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }
        try {
            return objectMapper.readValue(content, TypeReference);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析json
     *
     * @param content
     * @param valueType
     * @return
     */
    public static Object fromJson(String content, Class<?> main, Class<?>... valueType) {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }
        try {
            return objectMapper.readValue(content, getJavaType(main, valueType));
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
     * 源信息是否在集合中存在
     *
     * @param param  源信息
     * @param params 集合的信息
     * @return true则存在
     */
    public static boolean equals(Object param, Object... params) {
        if (params != null)
            for (Object s : params) {
                if (Objects.equals(param, s))
                    return true;
            }
        return false;
    }

    /**
     * 将对象的值转换为string
     *
     * @param object 对象值
     * @return 字符串
     */
    @SuppressWarnings("unchecked")
    public static String str(Object object) {
        if (empty(object))
            return "";

        Class<?> clazz = object.getClass();
        if (clazz.equals(String.class))
            return ((String) object).trim();

        if (clazz.equals(int.class) || clazz.equals(long.class) || clazz.equals(char.class) || clazz.equals(float.class)
                || clazz.equals(char[].class))
            return String.valueOf(object);

        if (clazz.equals(Integer.class) || clazz.equals(Long.class) || clazz.equals(Character.class)
                || clazz.equals(Float.class))
            return object.toString();

        if (clazz.equals(double.class) || clazz.equals(Double.class)) {
            clazz = BigDecimal.class;
            object = new BigDecimal((Double) object);
        }

        if (clazz.equals(BigDecimal.class))
            return new DecimalFormat("#.#####").format((BigDecimal) object);

        if (clazz.equals(boolean.class) || clazz.equals(Boolean.class))
            return ((Boolean) object) ? "1" : "0";

        /*
         * try { if (clazz.equals(oracle.sql.TIMESTAMP.class)) { object =
         * ((oracle.sql.TIMESTAMP) object).toJdbc(); clazz = java.sql.Timestamp.class; }
         * } catch (Exception e) { sys.warn(e); }
         */
        if (clazz.equals(Date.class) || clazz.equals(java.sql.Date.class) || clazz.equals(java.sql.Timestamp.class))
            return Dateformat((Date) object);

        if (clazz.equals(ArrayList.class) || clazz.equals(List.class)) {
            StringBuilder sbd = new StringBuilder();
            for (Object obj : (List<Object>) object) {
                sbd.append(",").append(str(obj));
            }
            return sbd != null && sbd.length() > 1 ? sbd.substring(1) : null;
        }

        if (clazz.equals(Set.class) || clazz.equals(HashSet.class)) {
            StringBuilder sbd = new StringBuilder();
            for (Object obj : (Set<Object>) object) {
                sbd.append(",").append(str(obj));
            }
            return sbd != null && sbd.length() > 1 ? sbd.substring(1) : null;
        }

        return object.getClass().getName();
    }

    /**
     * 将字符串转换为日期 日期格式的字符串支持： yyyy.MM.dd;yyyy.MM.dd HH:mm;yyyy.MM.dd HH:mm:ss;
     * yyyy-MM-dd;yyyy-MM-dd HH:mm;yyyy-MM-dd HH:mm:ss; yyyy/MM/dd;yyyy/MM/dd
     * HH:mm;yyyy/MM/dd HH:mm:ss; 11,13位纯数字
     *
     * @param object 需要转换的对象
     * @return java.util.Date日期
     */
    public static Date parseDate(Object object) {
        if (empty(object))
            return null;
        if (object instanceof Date)
            return (Date) object;
        if (object instanceof String)
            return parseDate((String) object);
        return parseDate(str(object));
    }

    /**
     * 是否是数字
     *
     * @param obj 任何参数
     * @return true则为数字
     */

    public static boolean isNumber(Object obj) {
        // 标记符号、小数点、e是否出现过
        char[] str = obj.toString().toCharArray();
        boolean sign = false;
        boolean decimal = false;
        boolean hasE = false;
        for (int i = 0; i < str.length; i++) {
            if (str[i] == 'e' || str[i] == 'E') {
                if (i == str.length - 1)
                    return false;
                if (hasE)
                    return false;
                hasE = true;
            } else if (str[i] == '+' || str[i] == '-') {
                if (sign && str[i - 1] != 'e' && str[i - 1] != 'E')
                    return false;
                if (!sign && i > 0 && str[i - 1] != 'e' && str[i - 1] != 'E')
                    return false;
                sign = true;
            } else if (str[i] == '.') {
                if (hasE || decimal)
                    return false;
                decimal = true;
            } else if (str[i] < '0' || str[i] > '9') {
                return false;
            }
        }
        return true;
    }


    /**
     * 转化为number(支持科学计算法)
     *
     * @param param 任何参数
     * @return Number 不为数字则返回空
     */
    public static Number parseNumber(Object param) {
        String str;
        if (!empty(param) && isNumber(str = param.toString())) {
            while (str.startsWith("0") && str.length() > 1 && str.substring(1, 2).startsWith("[0-9]*")) {
                str = str.substring(1, str.length());
            }
            return NumberUtils.createNumber(str);
        }
        return null;
    }

    /**
     * 转化为int(支持科学计算法)
     *
     * @param param 任何参数
     * @return int型数字, 为空或非法则为0
     */
    public static int parseInt(Object param) {
        if (empty(param))
            return 0;
        if (param instanceof Integer || (param != null && param.getClass().equals(int.class)))
            return (Integer) param;
        Number number = parseNumber(param);
        return empty(number) ? 0 : number.intValue();
    }

    /**
     * 转化为long
     *
     * @param param 任何參數
     * @return long型数字, 为空或非法则为0l
     */
    public static long parseLong(Object param) {
        if (param instanceof Long || (param != null && param.getClass().equals(long.class)))
            return (Long) param;
        Number number = parseNumber(param);
        return empty(number) ? 0l : number.longValue();
    }

    /**
     * 转化为double
     *
     * @param param 任何參數
     * @return double型数字, 为空或非法则为0d
     */
    public static double parseDouble(Object param) {
        if (param instanceof Double || (param != null && param.getClass().equals(double.class)))
            return (Double) param;
        Number number = parseNumber(param);
        return empty(number) ? 0d : Double.parseDouble(number.toString());// number.doubleValue()有精度丢失的风险
    }

    /**
     * 转化为float
     *
     * @param param 任何參數
     * @return float型数字, 为空或非法则为0f
     */
    public static float parseFloat(Object param) {
        if (param instanceof Float || (param != null && param.getClass().equals(float.class)))
            return (Float) param;
        Number number = parseNumber(param);
        return empty(number) ? 0f : Float.parseFloat(number.toString());// number.floatValue()有精度丢失的风险
    }

    /**
     * 将单个字符串转换为char
     *
     * @param param 源目标
     * @return char型, 非单个字符则为' '
     */
    public static char parseChar(Object param) {
        if (param instanceof Character || (param != null && param.getClass().equals(char.class)))
            return (Character) param;
        String str;
        if (!empty(param) && (str = param.toString()).length() == 1)
            return CharUtils.toChar(str);
        return ' ';
    }


    /**
     * @Description : string转 boolean
     * @Param: s
     * @Return: boolean
     * @auhor:lyc
     * @Date:2020/7/12 17:52
     */
    public static boolean parseBoolean(Object param) {
        if (empty(param))
            return false;
        if (param instanceof Boolean || param.getClass().equals(boolean.class)) {
            return (Boolean) param;
        }
        try {
            String s = String.valueOf(param).trim();
            if ("1".equals(s) || "'1'".equals(s))
                s = "true";
            return Boolean.parseBoolean(s);
        } catch (Exception e) {
            log.warn("解析[{}]错误;消息:{}", param, e);
        }
        return false;
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
     * @return String
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 雪花算法获取ID
     *
     * @Param: dataCenterId 数据中心名
     * @Param: machineId 机器名
     * @Return: String
     * @auhor: lyc
     * @Date: 2020/7/19 17:07
     */
    public static String getSnowFlakeID(long dataCenterId, long machineId) {
        if (snowFlakeUtil == null)
            snowFlakeUtil = new SnowFlakeUtil(dataCenterId, machineId);
        long id = 0L;
        try {
            id = snowFlakeUtil.nextId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return String.valueOf(id);
    }

    /**
     * 雪花算法获取ID 数据中心名: 1 机器名:1
     *
     * @Return: String
     * @auhor: lyc
     * @Date: 2020/7/19 17:07
     */
    public static String getSnowFlakeID() {
        return getSnowFlakeID(1L, 1L);
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
    public static String Dateformat(Date date, String prefix) {
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
        return Dateformat(date, "");
    }

    /**
     * @Description :时间格式化
     * @Param: date
     * @Return: String
     * @auhor:lyc
     * @Date:2019/11/1 22:53
     */
    public static String Dateformat(Long date) {
        return Dateformat(new Date(date), "");
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
        if (!empty(data) && (i = data.size()) > 0) {
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
        if (!empty(data) && (i = data.size()) > 0) {
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
        if (empty(data))
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
        if (empty(data))
            return null;
        List<E> result = new ArrayList<E>(data.size());
        for (E bean : data) {
            result.add(bean);
        }
        return result;
    }

    /**
     * 获取对象中的所有字段成员 包括父类 无视private/protected修饰符
     *
     * @param object
     * @return Field[]
     */
    public static Field[] getAllFields(Object object) {
        Class clazz = object.getClass();
        return getAllFields(clazz);
    }

    /**
     * 获取对象中的所有字段成员 包括父类 无视private/protected修饰符
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
            obj = obj(field.get(bean));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    /**
     * 根据属性名称找到地应的属性对象(不区分大小写)
     *
     * @param fields    所有属性对象
     * @param fieldName 属性名称
     * @return Field
     */
    public static Field getFieldByName(Field[] fields, String fieldName) {
        String name = null;
        for (Field field : fields) {
            name = field.getName();
            if (name.equalsIgnoreCase(fieldName))
                return field;
        }
        return null;
    }

    /**
     * 直接设置对象属性值, 无视private/protected修饰符, 不经过setter函数.
     *
     * @param target 当前对象
     * @param field  字段属性对象
     * @param value  对象的属性值
     */
    public static void setFieldVal(Object target, Field field, Object value) {
        try {
            value = obj(field.getType(), value);
            FieldUtils.writeField(field, target, value, true);
        } catch (IllegalAccessException e) {
            log.warn("设置属性值失败:{}", e);
        }
    }

    /**
     * 将对象的值转换为对应类型的值
     *
     * @param value 需转换的值
     * @return 转换后的对象
     */
    public static Object obj(Object value) {
        if (empty(value))
            return value;
        Class<?> clazz = value.getClass();
        return obj(clazz, value);
    }

    /**
     * 将对象的值转换为对应类型的值
     *
     * @param clazz 要转换的类型
     * @param value 需转换的值
     * @return 转换后的对象
     */
    public static Object obj(Class<?> clazz, Object value) {
        if (clazz.equals(Date.class))
            value = parseDate(value);
        else if (clazz.equals(int.class) || clazz.equals(Integer.class))
            value = parseInt(value);
        else if (clazz.equals(long.class) || clazz.equals(Long.class))
            value = parseLong(value);
        else if (clazz.equals(float.class) || clazz.equals(Float.class))
            value = parseFloat(value);
        else if (clazz.equals(double.class) || clazz.equals(Double.class))
            value = parseDouble(value);
        else if (clazz.equals(boolean.class) || clazz.equals(Boolean.class))
            value = parseBoolean(value);
        else if (clazz.equals(char.class) || clazz.equals(Character.class))
            value = parseChar(value);
        else if (clazz.equals(BigDecimal.class))
            value = ((BigDecimal) value).doubleValue();
        return value;
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

    /**
     * copyProperties
     * URI 转通配符
     *
     * @param uri
     * @return String
     */
    public static String uri2Charm(String uri) {
        if (empty(uri))
            return "";
        if (uri.startsWith("/"))
            uri = uri.substring(1);
        return uri.replace("/", ":");
    }


    /**
     * 同copyProperties 集中管理
     *
     * @param source
     * @param target
     * @return String
     */
    public static void copyProperties(Object source, Object target) {
        BeanUtils.copyProperties(source, target);
    }
}
