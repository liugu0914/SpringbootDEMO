package com.jiopeel.core;

import com.jiopeel.core.util.MathUtil;

public class CircleArea {

    public static void main(String[] args) {
        double circleArea = getCircleArea(4d,2d);
        System.out.println("阴影面积为："+circleArea );
    }

    /**
     *
     * @param r 半径
     * @param distance 移动距离
     * @return 阴影部分面积
     */
    private static double getCircleArea(double r,double distance) {
        //移动距离大于直径，直接为0
        if (MathUtil.compare(distance,r*2)>=0)
            return 0;
        //直角三角形的底边长
        double rtd=MathUtil.div(distance,2);
        //根据圆心做出三角形，得余弦值
        double cosA=MathUtil.div(rtd,r);
        //三角形的高
        double height=Math.sqrt(MathUtil.sub(Math.pow(r,2),Math.pow(rtd,2)));
        //求得角度
        double acosA = Math.toDegrees(Math.acos(cosA));
        //半圆面积
        double allArea=MathUtil.div(MathUtil.mul(Math.pow(r,2),Math.PI),2,4);
        //圆弧面积
        double arcArea=MathUtil.div(Math.pow(r,2)*Math.PI*acosA,360,4);
        //直角三角形面积
        double rtArea=MathUtil.div(MathUtil.mul(rtd,height),2,4);
        //阴影部分面积
        double target=MathUtil.sub(allArea,MathUtil.mul(MathUtil.sub(arcArea,rtArea),2));
        return target;
    }


}
