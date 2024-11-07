package main.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class NewCommentDto {
    String text;

    public void validate() {
        if (text == null || text.isBlank() || text.length() > 500) {
            throw new IllegalArgumentException("Text is not valid");
        }
    }
}
