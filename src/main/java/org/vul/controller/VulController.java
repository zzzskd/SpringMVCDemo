package org.vul.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.vul.poc.ControllerMemShellGenerator;

@Controller
public class VulController {
    /**
     * 假设这里存在代码执行漏洞，可以在此接口实现
     * @return
     */
    @RequestMapping("/vul")
    public String vulInterface(@RequestParam("type")String type) {
        if ("controller".equals(type)) {
            new ControllerMemShellGenerator();
        }
        return "";
    }
}
