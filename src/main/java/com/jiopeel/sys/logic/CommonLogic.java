package com.jiopeel.sys.logic;


import com.jiopeel.core.logic.BaseLogic;
import com.jiopeel.core.util.BaseUtil;
import com.jiopeel.sys.bean.Common;
import com.jiopeel.sys.bean.result.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author ：lyc
 * @description：公共的事件集合
 * @date ：2019/12/20 10:25
 */
@Slf4j
@Service
public class CommonLogic extends BaseLogic {

    private static String ICONFONT = "static/bootstrap/css/iconfont.css";

    private static String ICONFONT_REGEX = "cs-.+\\:";

    private static String ICONFONT_SHOW_VALUE = "<i class='%s'></i>";

    /**
     * @Description :模糊搜索
     * @Param: map
     * @Return: List<CommonResult>
     * @auhor:lyc
     * @Date:2020年05月23日18:55:10
     */
    public List<CommonResult> search(Map<String, String> map) {
        String sql = "";
        if (map.containsKey("sql"))
            sql = map.get("sql");
        if (BaseUtil.empty(sql))
            sql = "common.searchMenu";
        List<CommonResult> list = dao.query(sql, map);
        return list;
    }

    /**
     * @Description :图标模糊搜索
     * @Param : map
     * @Return : List<CommonResult>
     * @auhor :lyc
     * @Date :2020年05月23日18:55:14
     */
    public List<CommonResult> searchIcons(Map<String, String> map) {
        List<CommonResult> list = getIcons();
        List<CommonResult> items=new ArrayList<>();
        String search="";
        if (map.containsKey("search"))
            search = map.get("search").trim();
        for (CommonResult result : list) {
            String id=result.getId();
            if (id.indexOf(search)>=0)
                items.add(result);
        }
        return items;
    }

    /**
     * @Description :图标数据
     * @Return : List<CommonResult>
     * @auhor :lyc
     * @Date :2020年05月23日18:55:14
     */
    private List<CommonResult> getIcons() {
        List<CommonResult> list = new ArrayList<>();
        //获取icontfont.css文件路径，根据正则获取类名
        StringBuffer result = new StringBuffer();
        try {
            InputStream realPath = BaseUtil.getRealPath(ICONFONT);
            byte[] buf = new byte[1024]; //数据中转站 临时缓冲区
            int length = 0;
            while((length = realPath.read(buf)) != -1){
                result.append(System.lineSeparator() + new String(buf, 0, length));
            }
            realPath.close();
            String css = result.toString();
            if (!BaseUtil.empty(css)) {
                // 创建 Pattern 对象
                Pattern regex = Pattern.compile(ICONFONT_REGEX);
                // 现在创建 matcher 对象
                Matcher matcher = regex.matcher(css);
                while (matcher.find()) { //此处find（）每次被调用后，会偏移到下一个匹配
                    String group = "cs " + matcher.group().replaceAll("\\.|\\:", "");
                    String showValue = "<span>" + String.format(ICONFONT_SHOW_VALUE, group) + ' ' + group + "</span>";
                    CommonResult common = new CommonResult(group, showValue);
                    list.add(common);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    /**
     * @Description :菜单名称模糊搜索
     * @Param: map
     * @Return: List<CommonResult>
     * @auhor:lyc∏
     * @Date:2019/12/21 00:02
     */
    public List<CommonResult> searchMenu(Map<String, String> map) {
        List<CommonResult> list = dao.query("common.searchMenu", map);
        return list;
    }

    /**
     * @Description :应用模糊搜索
     * @Param: map
     * @Return: List<CommonResult>
     * @auhor:lyc∏
     * @Date:2019/12/21 00:02
     */
    public List<CommonResult> searchApp(Map<String, String> map) {
        List<CommonResult> list = dao.query("common.searchApp", map);
        return list;
    }
}
