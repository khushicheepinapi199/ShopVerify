package io.shopverify.app;
import java.math.BigDecimal;
import java.util.*;
import org.springframework.stereotype.Component;
@Component
public class Catalog {
 public record Product(long id,String name,String description,BigDecimal price,String image) {}
 private final List<Product> products=List.of(
  new Product(1,"Canvas Daypack","A lightweight everyday bag with roomy pockets.",new BigDecimal("39.00"),"bag"),
  new Product(2,"Studio Headphones","Over-ear headphones for your daily soundtrack.",new BigDecimal("59.00"),"headphones"),
  new Product(3,"Travel Bottle","A reusable bottle for your desk and adventures.",new BigDecimal("18.50"),"bottle"),
  new Product(4,"Everyday Notebook","A dotted notebook for ideas worth keeping.",new BigDecimal("12.00"),"notebook"),
  new Product(5,"Desk Lamp","A compact lamp for a brighter workspace.",new BigDecimal("32.00"),"lamp"),
  new Product(6,"Cotton Cap","An easy-fit cap for days on the move.",new BigDecimal("16.00"),"cap"));
 public Product get(long id) { return products.stream().filter(p->p.id()==id).findFirst().orElseThrow(()->new IllegalArgumentException("Product not found.")); }
 public List<Product> sorted(String sort) { var result=new ArrayList<>(products); Comparator<Product> c=switch(sort) {
  case "za" -> Comparator.comparing(Product::name).reversed(); case "lohi" -> Comparator.comparing(Product::price); case "hilo" -> Comparator.comparing(Product::price).reversed(); default -> Comparator.comparing(Product::name); }; result.sort(c); return result; }
}
