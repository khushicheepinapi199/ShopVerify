package io.shopverify.app;
import jakarta.persistence.*;
@Entity @Table(name="accounts")
public class Account {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false,unique=true,length=32) private String username;
 @Column(nullable=false,length=100) private String passwordHash;
 protected Account() {}
 public Account(String username,String hash) { this.username=username; this.passwordHash=hash; }
 public String getUsername() { return username; }
 public String getPasswordHash() { return passwordHash; }
}
