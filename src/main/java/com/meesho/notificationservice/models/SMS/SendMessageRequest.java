package com.meesho.notificationservice.models.SMS;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class SendMessageRequest {
    @NotBlank(message = "text field cannot be empty")
    private String text;

    @Pattern(regexp="[6-9][0-9]{9}",message = "phone number must be of 10 digits,1st digit must be in range-[6,9]")
    private String phoneNumber;
}
