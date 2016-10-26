package com.keydak.videoapp.webmvc.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;



/**
 * Created by admin on 2016/10/25.
 */
@Controller
@RequestMapping("/example")

public class ExampleController {
    @RequestMapping(value="/testJSP" ,method={RequestMethod.GET,RequestMethod.POST})
    public ModelAndView exampleJSP(){
        ModelAndView modelAndView = new ModelAndView("test");
        modelAndView.addObject("param1", "将参数传进来了");
        return modelAndView;
    }


    @ResponseBody
    @RequestMapping(value="/testJSON" ,method={RequestMethod.GET,RequestMethod.POST})
    //将字符串返回到页面上
    public String exampleJSON(){
        return "this is a test json";
    }

}
