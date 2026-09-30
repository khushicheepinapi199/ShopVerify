package io.shopverify.app;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.dao.DataIntegrityViolationException;
@Service
public class AccountService {
 private final AccountRepository accounts; private final PasswordEncoder encoder;
 public AccountService(AccountRepository a,PasswordEncoder e) { accounts=a; encoder=e; }
 public void register(String username,String password,String confirmation) {
  if(username==null || !username.matches("[a-z0-9_]{3,32}")) throw new IllegalArgumentException("Use 3–32 lowercase letters, numbers or underscores for your username.");
  if(password==null || password.length()<8 || password.length()>64 || password.getBytes(java.nio.charset.StandardCharsets.UTF_8).length>72) throw new IllegalArgumentException("Password must contain 8–64 characters and at most 72 UTF-8 bytes.");
  if(!password.equals(confirmation)) throw new IllegalArgumentException("Passwords do not match.");
  if(accounts.findByUsername(username).isPresent()) throw new IllegalArgumentException("Username is already taken.");
  try { accounts.saveAndFlush(new Account(username,encoder.encode(password))); }
  catch(DataIntegrityViolationException ex) { throw new IllegalArgumentException("Username is already taken."); }
 }
}
