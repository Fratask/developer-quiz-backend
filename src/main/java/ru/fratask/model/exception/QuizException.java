package ru.fratask.model.exception;

import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class QuizException extends RuntimeException {

    private QuizExceptionResponse code;
    private String errorMessage;

    public QuizException(QuizExceptionResponse code) {
        super(code.getErrorMessage());
        this.code = code;
        this.errorMessage = code.getErrorMessage();
    }

    public QuizException(Throwable cause) {
        super(cause);
    }
}
