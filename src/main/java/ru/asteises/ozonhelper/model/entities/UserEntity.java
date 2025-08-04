package ru.asteises.ozonhelper.model.entities;

import jakarta.persistence.*;
import lombok.*;
import ru.asteises.ozonhelper.enums.UserRole;
import ru.asteises.ozonhelper.enums.UserStatus;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Column(name = "telegram_user_id", nullable = false, unique = true)
    private Long telegramUserId;

    @Column(name = "user_name")
    private String username;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "registered_at")
    private LocalDateTime registeredAt;

    @Column(name = "client_id")
    private String clientId;

    @Column(name = "encrypted_api_key")
    private String encryptedApiKey;

    @Transient
    public boolean hasSecrets() {
        return hasClientId() && hasEncryptedApiKey();
    }

    @Transient
    public boolean hasClientId() {
        return clientId != null && !clientId.isBlank();
    }

    @Transient
    public boolean hasEncryptedApiKey() {
        return encryptedApiKey != null && !encryptedApiKey.isBlank();
    }

    @Transient
    public boolean hasAdditionalData() {
        return hasFirstName() && hasLastName();
    }

    @Transient
    public boolean hasFirstName() {
        return firstName != null && !firstName.isBlank();
    }

    @Transient
    public boolean hasLastName() {
        return lastName != null && !lastName.isBlank();
    }
}

