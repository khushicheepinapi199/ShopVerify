package io.shopverify.app;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
@Configuration
public class SecurityConfig {
 @Bean PasswordEncoder encoder() { return new BCryptPasswordEncoder(); }
 @Bean UserDetailsService users(AccountRepository accounts) { return username -> {
  var a=accounts.findByUsername(username).orElseThrow(()->new UsernameNotFoundException("Invalid credentials"));
  return User.withUsername(a.getUsername()).password(a.getPasswordHash()).roles("SHOPPER").build();
 }; }
 @Bean SecurityFilterChain security(HttpSecurity http) throws Exception {
  return http.authorizeHttpRequests(a->a.requestMatchers("/","/login","/register","/css/**","/images/**","/error").permitAll().anyRequest().authenticated())
   .formLogin(f->f.loginPage("/login").defaultSuccessUrl("/products",true).permitAll())
   .logout(l->l.logoutSuccessUrl("/login?logout").invalidateHttpSession(true).deleteCookies("JSESSIONID"))
   .build();
 }
}
