package org.vul.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.vul.bean.User;
import org.vul.service.UserService;

@Controller
public class IndexController {

    @Autowired
    private UserService userService;

    @RequestMapping("/user")
    @ResponseBody
    public String getUser(@RequestParam("username")String username) {
        return userService.getUserByUsername(username).toString();
    }
}
