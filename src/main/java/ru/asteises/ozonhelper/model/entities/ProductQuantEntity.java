package ru.asteises.ozonhelper.model.entities;

import jakarta.persistence.*;
import lombok.*;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    @Column(name = "quant_code", nullable = false)
    private String quantCode;

    @Column(name = "quant_size")
    private Integer quantSize;
}
