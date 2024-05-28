package com.example.demo.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class ResponseEntity {

    private final boolean result;
    private final String message;

    public ResponseEntity(boolean result, String message) {
        this.result = result;
        this.message = message;
    }

}
