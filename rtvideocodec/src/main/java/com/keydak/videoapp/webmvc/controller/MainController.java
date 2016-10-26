package com.keydak.videoapp.webmvc.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by admin on 2016/10/25.
 */
@Controller
@RequestMapping("/streams")
public class MainController {
    @RequestMapping(value="/videoplay" ,method={RequestMethod.GET,RequestMethod.POST})
    public ModelAndView videoPlay(@RequestParam("width") Integer width,
                                  @RequestParam("height") Integer height,
                                  @RequestParam("weburl") String weburl){
        ModelAndView modelAndView = new ModelAndView("streamvideo");
        //分辨率
        modelAndView.addObject("width",width);
        modelAndView.addObject("height",height);
        //websocket地址
        modelAndView.addObject("weburl", weburl); //"ws://127.0.0.1:9094/"
        return modelAndView;
    }

}
