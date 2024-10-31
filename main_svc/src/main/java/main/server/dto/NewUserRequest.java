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
public class NewUserRequest {
    String email;
    String name;

    public void validate() {
        if (email == null || email.isEmpty() || email.isBlank() || !email.contains("@") || email.length() < 6
                || email.length() > 254 || !email.matches("^(?=.{1,64}@)([a-zA-Z0-9._%+-]{1,64})@([a-zA-Z0-9.-]{1,63})\\.(.+)$")) {
            throw new IllegalArgumentException("Email is not valid");
        }
        if (name == null || name.isEmpty() || name.isBlank() || name.length() < 2 || name.length() > 250) {
            throw new IllegalArgumentException("Name is not valid");
        }
    }
}
