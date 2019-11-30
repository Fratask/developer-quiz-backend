package ru.fratask.model.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;
import java.util.stream.Stream;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

@Getter
@AllArgsConstructor
public enum QuizExceptionResponse {

    UNKNOWN_ERROR(0, "Unknown error"),
    USER_ALREADY_EXISTS(1, "User with this username already exists"),
    USER_NOT_FOUND(2, "User not found"),
    USER_DISABLED(4, "User disabled"),
    INVALID_CREDENTIALS(5, "Invalid credentials"),
    USER_UPDATE_FAIL(6, "Updating fail"),
    ROLE_NOT_FOUND(7, "Role not found"),

    ;

    private final int code;
    private final String errorMessage;

    private static QuizExceptionResponse byCode(int code) {
        return byCodeMap.get(code);
    }

    static {
        byCodeMap = Stream.of(QuizExceptionResponse.values()).collect(toMap(QuizExceptionResponse::getCode, identity()));
    }

    private static Map<Integer, QuizExceptionResponse> byCodeMap;
}
