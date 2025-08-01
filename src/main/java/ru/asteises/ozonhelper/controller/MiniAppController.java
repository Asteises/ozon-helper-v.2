package ru.asteises.ozonhelper.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MiniAppController {

    @GetMapping("/miniapp")
    public String getMiniAppPage() {
        return "miniapp";
    }
}
