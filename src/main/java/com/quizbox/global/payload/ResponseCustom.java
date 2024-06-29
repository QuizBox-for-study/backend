package com.quizbox.global.payload;

import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ResponseCustom<T> {

    private T data;
    private LocalDateTime transaction_time;
    private HttpStatus status;
    private String description;
    private int statusCode;


    @Builder
    public ResponseCustom(T data, LocalDateTime transaction_time, HttpStatus status, String description, int statusCode) {
        this.data = data;
        this.transaction_time = transaction_time;
        this.status = status;
        this.description = description;
        this.statusCode = statusCode;
    }

    public static <T> ResponseCustom<T> OK(@Nullable T data) {
        return (ResponseCustom<T>) ResponseCustom.builder()
                .transaction_time(LocalDateTime.now())
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .data(data)
                .build();
    }
}
