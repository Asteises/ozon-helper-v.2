package ru.asteises.ozonhelper.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.asteises.ozonhelper.model.OzonDataForm;

@Slf4j
@Controller
public class MiniAppController {

    @ResponseBody
    @PostMapping("/save")
    public ResponseEntity<String> saveOzonData(@RequestBody OzonDataForm ozonDataForm) {
        // TODO Логика сохранения
        log.debug(ozonDataForm.toString());
        return ResponseEntity.ok("Данные сохранены");
    }

}
