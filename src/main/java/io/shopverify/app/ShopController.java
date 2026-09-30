package io.shopverify.app;
import java.math.*;
import java.security.Principal;
import java.util.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
@Controller
public class ShopController {
 private final AccountService accounts; private final Catalog catalog; private final PurchaseRepository orders;
 public ShopController(AccountService a,Catalog c,PurchaseRepository o) { accounts=a; catalog=c; orders=o; }
 public record Line(Catalog.Product product,int quantity,BigDecimal amount) {}
 private Cart cart(HttpSession s) { var c=(Cart)s.getAttribute("cart"); if(c==null) { c=new Cart(); s.setAttribute("cart",c); } return c; }
 @ModelAttribute void shared(Model m,HttpSession s,Principal user) { m.addAttribute("cartCount",cart(s).count()); m.addAttribute("username",user==null?null:user.getName()); }
 @GetMapping("/") String home() { return "redirect:/products"; }
 @GetMapping("/login") String login() { return "login"; }
 @GetMapping("/register") String register() { return "register"; }
 @PostMapping("/register") String register(@RequestParam String username,@RequestParam String password,@RequestParam String confirmation,Model m) {
  try { accounts.register(username,password,confirmation); return "redirect:/login?registered"; }
  catch(IllegalArgumentException ex) { m.addAttribute("error",ex.getMessage()); m.addAttribute("enteredUsername",username); return "register"; }
 }
 @GetMapping("/products") String products(@RequestParam(defaultValue="az") String sort,Model m) { m.addAttribute("products",catalog.sorted(sort)); m.addAttribute("sort",sort); return "products"; }
 @GetMapping("/products/{id}") String product(@PathVariable long id,Model m) { m.addAttribute("product",catalog.get(id)); return "product"; }
 @PostMapping("/cart/add") String add(@RequestParam long id,HttpSession s,RedirectAttributes flash) { catalog.get(id); try { cart(s).add(id); } catch(IllegalArgumentException ex) { flash.addFlashAttribute("error",ex.getMessage()); } return "redirect:/products"; }
 @PostMapping("/cart/update") String update(@RequestParam long id,@RequestParam int quantity,HttpSession s,RedirectAttributes f) { catalog.get(id); try { cart(s).update(id,quantity); } catch(IllegalArgumentException ex) { f.addFlashAttribute("error",ex.getMessage()); } return "redirect:/cart"; }
 @PostMapping("/cart/remove") String remove(@RequestParam long id,HttpSession s) { cart(s).remove(id); return "redirect:/cart"; }
 private void summary(HttpSession s,Model m) { var lines=cart(s).quantities().entrySet().stream().map(e->{ var p=catalog.get(e.getKey()); return new Line(p,e.getValue(),p.price().multiply(BigDecimal.valueOf(e.getValue()))); }).toList(); var subtotal=lines.stream().map(Line::amount).reduce(BigDecimal.ZERO,BigDecimal::add).setScale(2); var tax=subtotal.multiply(new BigDecimal("0.08")).setScale(2,RoundingMode.HALF_UP); m.addAttribute("lines",lines); m.addAttribute("subtotal",subtotal); m.addAttribute("tax",tax); m.addAttribute("total",subtotal.add(tax)); }
 @GetMapping("/cart") String viewCart(HttpSession s,Model m) { summary(s,m); return "cart"; }
 @GetMapping("/checkout") String checkout(HttpSession s,Model m) { if(cart(s).count()==0) return "redirect:/cart"; summary(s,m); return "checkout"; }
 @PostMapping("/checkout") String checkout(@RequestParam String fullName,@RequestParam String address,HttpSession s,Principal user,Model m) {
  if(cart(s).count()==0) return "redirect:/cart";
  if(fullName.isBlank() || address.isBlank() || fullName.length()>100 || address.length()>200) { m.addAttribute("error","Enter your name and delivery address within the stated limits."); summary(s,m); return "checkout"; }
  summary(s,m); var order=orders.saveAndFlush(new Purchase(user.getName(),(BigDecimal)m.getAttribute("total"))); cart(s).clear(); return "redirect:/orders/"+order.getId();
 }
 @GetMapping("/orders") String orders(Principal user,Model m) { m.addAttribute("orders",orders.findByUsernameOrderByCreatedAtDesc(user.getName())); return "orders"; }
 @GetMapping("/orders/{id}") String order(@PathVariable long id,Principal user,Model m) { var order=orders.findById(id).filter(o->o.getUsername().equals(user.getName())).orElseThrow(()->new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND)); m.addAttribute("order",order); return "confirmation"; }
 @ExceptionHandler(IllegalArgumentException.class) @ResponseStatus(org.springframework.http.HttpStatus.BAD_REQUEST) @ResponseBody String invalid(IllegalArgumentException ex) { return "Invalid request."; }
}
