package ru.asteises.ozonhelper.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class MiniAppController {

    @GetMapping({"/miniapp", "/miniapp/", "/miniapp/**"})
    public String forwardToMiniApp() {
        return "forward:/miniapp/index.html";
    }
}
