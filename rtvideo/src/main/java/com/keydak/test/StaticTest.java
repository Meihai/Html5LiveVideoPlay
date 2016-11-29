package com.keydak.test;
//http://blog.csdn.net/u013256816/article/details/50837863
/**
 * Created by admin on 2016/11/24.
 */
public class StaticTest {
    public static void main(String[] args)
    {
        staticFunction();
    }

    static StaticTest st = new StaticTest();

    static
    {
        System.out.println("1");
    }

    {
        System.out.println("2");
    }

    StaticTest()
    {
        System.out.println("3");
        System.out.println("a="+a+",b="+b);
    }
    public static void staticFunction(){
        System.out.println("4");
    }

    int a=110;
    static int b =112;

}
//java中赋值顺序
/*
* 类的生命周期 加载>验证>准备>解析>初始化>使用>卸载
* 父类的静态变量赋值
* 自身的静态变量赋值
* 父类成员变量赋值
* 父类块赋值
* 父类构造函数赋值
* 自身成员变量赋值
* 自身块赋值
* 自身构造函数赋值
*/