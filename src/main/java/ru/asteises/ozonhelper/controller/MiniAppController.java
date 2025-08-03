package ru.asteises.ozonhelper.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.asteises.ozonhelper.model.RegisterUserData;
import ru.asteises.ozonhelper.service.UserService;

//https://asteises.ru/dev/bot/ozon/helper
@Slf4j
@Controller
@RequiredArgsConstructor
public class MiniAppController {

    private final UserService userService;

    @ResponseBody
    @PostMapping("/save")
    public ResponseEntity<String> saveOzonData(@RequestBody RegisterUserData registerUserData) {
        String registrationMessage = userService.saveUser(registerUserData);
        log.debug(registerUserData.toString());
        return ResponseEntity.ok(registrationMessage);
    }

}
