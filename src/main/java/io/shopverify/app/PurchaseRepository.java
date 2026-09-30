package io.shopverify.app;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
public interface PurchaseRepository extends JpaRepository<Purchase,Long> { List<Purchase> findByUsernameOrderByCreatedAtDesc(String username); }
