package ru.asteises.ozonhelper.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.asteises.ozonhelper.enums.CallbackType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ButtonDto {

    private String text;
    private CallbackType callback;
    private String webAppUrl;
}
