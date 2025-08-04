package ru.asteises.ozonhelper.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.asteises.ozonhelper.model.RegisterUserData;
import ru.asteises.ozonhelper.service.UserService;
import ru.asteises.ozonhelper.validator.TelegramAuthValidator;

//https://asteises.ru/dev/bot/ozon/helper
@Slf4j
@Controller
@RequiredArgsConstructor
public class MiniAppController {

    @Value("${telegram.bot.token}")
    private String botToken;

    private final UserService userService;

    @ResponseBody
    @PostMapping("/save")
    public ResponseEntity<String> saveOzonData(@RequestBody RegisterUserData registerUserData) {
        log.debug(registerUserData.toString());
        if (!TelegramAuthValidator.validate(registerUserData.getTelegramInitData(), botToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid hash");
        }
        String registrationMessage = userService.saveUser(registerUserData);
        return ResponseEntity.ok(registrationMessage);
    }

}
