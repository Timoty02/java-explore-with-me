package main.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class UpdateCompilationRequest {
    List<Integer> events;
    Boolean pinned;
    String title;

    public void validate() {
        if (title != null && title.length() > 50) {
            throw new IllegalArgumentException("Title is not valid");
        }
    }
}
