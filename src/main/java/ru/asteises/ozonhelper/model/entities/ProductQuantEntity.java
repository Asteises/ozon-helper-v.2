package ru.asteises.ozonhelper.model.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product_quants",
        uniqueConstraints = @UniqueConstraint(columnNames = {"product_id", "quant_code"}))
public class ProductQuantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uuid")
    private UUID uuid;

    @ManyToOne()
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    @Column(name = "quant_code", nullable = false)
    private String quantCode;

    @Column(name = "quant_size")
    private Integer quantSize;

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        ProductQuantEntity that = (ProductQuantEntity) object;
        return Objects.equals(uuid, that.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(uuid);
    }
}
