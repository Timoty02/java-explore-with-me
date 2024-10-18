package stat.server;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

import static stat.server.EndpointHit.TABLE_NAME;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Entity
@Table(name = TABLE_NAME)
public class EndpointHit {
    @Transient
    static final String TABLE_NAME = "endpoint_hits";
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    int id;
    String app;
    String uri;
    String ip;
    LocalDateTime timestamp;
}
