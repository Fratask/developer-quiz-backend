package ru.fratask.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Message {

    REGISTRATION_TITLE_MESSAGE("Developer Quiz registration confirm"),
    REGISTRATION_BODY_MESSAGE("Your email was used to register in Developer Quiz. Please confirm your email by clicking on the link "),
    REGISTRATION_COMPLETE_TITLE_MESSAGE("Developer Quiz - email success confirmed"),
    REGISTRATION_COMPLETE_BODY_MESSAGE("Your email was successful confirmed"),


    ;
    private String message;

}
