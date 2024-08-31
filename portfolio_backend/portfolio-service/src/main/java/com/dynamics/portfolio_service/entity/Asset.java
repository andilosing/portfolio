package com.dynamics.portfolio_service.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table(name = "asset")
@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "asset_id", updatable = false, nullable = false)
    private Long assetId;

    @Column(name = "name", nullable = false , unique = true)
    private String name;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "last_value", nullable = true)
    private BigDecimal lastValue;

    @Column(name = "value_date", nullable = true)
    private LocalDateTime valueDate;

}
