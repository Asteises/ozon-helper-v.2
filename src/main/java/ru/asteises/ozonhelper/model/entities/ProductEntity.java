package ru.asteises.ozonhelper.model.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products",
        uniqueConstraints = @UniqueConstraint(columnNames = {"catalog_id", "product_id"}))
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "catalog_id", nullable = false)
    private UserProductCatalogEntity catalog;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "offer_id")
    private String offerId;

    @Column(name = "name")
    private String name;

    @Column(name = "has_fbo_stocks")
    private Boolean hasFboStocks;

    @Column(name = "has_fbs_stocks")
    private Boolean hasFbsStocks;

    @Column(name = "archived")
    private Boolean archived;

    @Column(name = "is_discounted")
    private Boolean isDiscounted;

    @Column(name = "last_synced_at")
    private LocalDateTime lastSyncedAt;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductQuantEntity> quants;
}
