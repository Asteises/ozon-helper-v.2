package ru.asteises.ozonhelper.model.entities;

import jakarta.persistence.*;
import lombok.*;
import ru.asteises.ozonhelper.enums.CatalogStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_product_catalog",
        uniqueConstraints = @UniqueConstraint(columnNames = "user_id"))
public class UserProductCatalogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uuid")
    private UUID uuid;

    @OneToOne()
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "total_products", nullable = false)
    private Integer totalProducts;

    @Column(name = "last_synced_at")
    private LocalDateTime lastSyncedAt;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private CatalogStatus status;

    @Column(name = "progress")
    private Integer progress;

    @Column(name = "last_processed_id")
    private String lastProcessedId;

    @OneToMany(mappedBy = "catalog", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductEntity> products;

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        UserProductCatalogEntity that = (UserProductCatalogEntity) object;
        return Objects.equals(uuid, that.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(uuid);
    }
}
