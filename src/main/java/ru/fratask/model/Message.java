package ru.fratask.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Message {

    REGISTRATION_TITLE_MESSAGE("Developer Quiz registration confirm"),
    REGISTRATION_BODY_MESSAGE("Your mail was used to register in Developer Quiz. Please confirm your email by clicking on the link "),

    ;
    private String message;

}
