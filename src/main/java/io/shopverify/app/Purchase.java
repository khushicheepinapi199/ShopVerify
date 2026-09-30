package io.shopverify.app;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
@Entity @Table(name="purchases")
public class Purchase {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false) private String username;
 @Column(nullable=false) private BigDecimal total;
 @Column(nullable=false) private Instant createdAt;
 protected Purchase() {}
 public Purchase(String username,BigDecimal total) { this.username=username; this.total=total; createdAt=Instant.now(); }
 public Long getId() { return id; } public String getUsername() { return username; } public BigDecimal getTotal() { return total; } public Instant getCreatedAt() { return createdAt; }
}
