package dev.tise.transaction_service.utils;

import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Objects;

public class ResponseUtils {

    public static ResponseEntity<List<String>> mapResponse (List<String> output) {

        if (Objects.isNull(output)){
            return ResponseEntity.badRequest().build();
        }
        if (output.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(output);
    }
}
