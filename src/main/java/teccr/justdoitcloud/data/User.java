package teccr.justdoitcloud.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Data
@Table("users")
public class User implements UserDetails {
    @Id
    private Long id;
    @Column("user_name")
    private String userName;
    private String name;
    private String email;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private Type type;
    @Transient
    @JsonIgnore // No incluir esta propiedad en las respuestas JSON de la API
    private List<Task> tasks;
    @Column("created_at")
    private LocalDateTime createdAt;

    public enum Type {
        ADMIN,
        REGULAR
    }


    @Override
    public String getUsername() {
        return userName;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + type.name()));
    }
}
