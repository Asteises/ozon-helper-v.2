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
import ru.asteises.ozonhelper.model.CheckUserData;
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
    @PostMapping("/check")
    public ResponseEntity<String> isUserExist(@RequestBody CheckUserData checkUserData) {
        log.info("Checking if user exists for user tg id: [ {} ]", checkUserData.getTelegramUserId());
        if (!TelegramAuthValidator.validateInitData(checkUserData.getTelegramInitData(), botToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(HttpStatus.UNAUTHORIZED.getReasonPhrase());
        }
        boolean userExist = userService.existByTelegramUserId(checkUserData.getTelegramUserId());
        if (userExist) {
            return ResponseEntity.ok(HttpStatus.OK.getReasonPhrase());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(HttpStatus.NOT_FOUND.getReasonPhrase());
    }

    @ResponseBody
    @PostMapping("/save")
    public ResponseEntity<Boolean> saveOrUpdateUser(@RequestBody RegisterUserData registerUserData) {
        log.debug("Registration data for user tg id: [ {} ]", registerUserData.getTelegramUserId());
        Boolean registrationSuccess = userService.saveOrUpdateUser(registerUserData);
        return ResponseEntity.ok(registrationSuccess);
    }
}
