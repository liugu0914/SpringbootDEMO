package com.jiopeel.sys.bean.result;

import com.jiopeel.sys.bean.Menu;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @description：角色表返回bean
 * @author     ：lyc
 * @date       ：2019/12/20 9:47
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class MenuResult extends Menu {

    private static final long serialVersionUID = -1567228179843411351L;

    /**
     * 子菜单层
     */
    private List<MenuResult> list;
}
