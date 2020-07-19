package com.jiopeel.core.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description : 树结构定义
 * @auhor: lyc
 * @Date:2020/7/12 17:01
 */
@Data
public class TreeNode implements Serializable {

    private static final long serialVersionUID = 8236808256735522812L;

    /**
     * 树节点id
     */
    private String id;

    /**
     * 树节点的父节点id
     */
    private String pid;

    /**
     * 节点名称
     */
    private  String name;

    /**
     * 节点连接url
     */
    private  String  url;

    /**
     * 节点字体图标 参考阿里 iconfont 类似 cs cs-jia
     */
    private String iconfont;

    /**
     * 是否为父节点
     */
    private boolean isParent;

    /**
     * 是否隐藏
     */
    private boolean isHidden;

    /**
     * 是否展开 true 展开 false 折叠
     */
    private boolean open;

    /**
     * 设置点击节点后在何处打开 url
     */
    private String target;

    /**
     * 节点的 checkBox / radio 的 勾选状态。[setting.check.enable = true & treeNode.nocheck = false 时有效]
     * 1、如果不使用 checked 属性设置勾选状态，请修改 setting.data.key.checked
     * 2、建立 treeNode 数据时设置 treeNode.checked = true 可以让节点的输入框默认为勾选状态
     * 3、修改节点勾选状态，可以使用 treeObj.checkNode / checkAllNodes / updateNode 方法，具体使用哪种请根据自己的需求而定
     * 4、为了解决部分朋友生成 json 数据出现的兼容问题, 支持 "false","true" 字符串格式的数据
     *
     * 默认值：false
     */
    private boolean checked;

    /**
     *
     * 1、设置节点的 checkbox / radio 是否禁用 [setting.check.enable = true 时有效]
     * 2、为了解决部分朋友生成 json 数据出现的兼容问题, 支持 "false","true" 字符串格式的数据
     * 3、请勿对已加载的节点修改此属性，禁止 或 取消禁止 请使用 setChkDisabled() 方法
     * 4、初始化时，如果需要子孙节点继承父节点的 chkDisabled 属性，请设置 setting.check.chkDisabledInherit 属性
     *
     * 默认值：false
     */
    private boolean chkDisabled;


    /**
     * 节点自定义图标的 URL 路径。 img，gif等  基本不使用
     */
    @Deprecated
    private String icon;

    /**
     * 父节点自定义折叠时图标的 URL 路径。
     * 1、此属性只针对父节点有效
     * 2、此属性必须与 iconOpen 同时使用
     * 3、如果想利用 className 设置个性化图标，需要设置 treeNode.iconSkin 属性
     */
    @Deprecated
    private String iconClose;

    /**
     * 父节点自定义展开时图标的 URL 路径。
     * 1、此属性只针对父节点有效
     * 2、此属性必须与 iconOpen 同时使用
     * 3、如果想利用 className 设置个性化图标，需要设置 treeNode.iconSkin 属性
     */
    @Deprecated
    private String iconOpen;

    /**
     * 节点自定义图标的 className
     * 1、需要修改 css，增加相应 className 的设置
     * 2、css 方式简单、方便，并且同时支持父节点展开、折叠状态切换图片
     * 3、css 建议采用图片分割渲染的方式以减少反复加载图片，并且避免图片闪动
     * 4、zTree v3.x 的 iconSkin 同样支持 IE6
     * 5、如果想直接使用 图片的Url路径 设置节点的个性化图标，需要设置 treeNode.icon / treeNode.iconOpen / treeNode.iconClose 属性
     */
    @Deprecated
    private String iconSkin;
}
