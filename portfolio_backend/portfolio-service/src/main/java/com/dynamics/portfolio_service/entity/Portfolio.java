package com.dynamics.portfolio_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Table(name = "portfolio")
@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Portfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "portfolio_id", updatable = false, nullable = false, unique = true)
    private Long portfolioId;

    @Column(name = "name", nullable = false)
    private String name;

   @Column(name = "user_id")
    private UUID userId;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "portfolio", cascade = CascadeType.ALL)
    private List<PortfolioAsset> portfolioAssets;

    @Column(length = 6)
    private String accessCode;

}
