package main.server.dto;

import jakarta.validation.constraints.Size;
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
public class NewCategoryDto {
    String name;

    public void validate(){
        if (name == null || name.isBlank() || name.length() > 50) {
            throw new IllegalArgumentException("Name is not valid");
        }
    }
}
