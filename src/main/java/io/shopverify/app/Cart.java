package io.shopverify.app;
import java.io.Serializable;
import java.util.*;
public class Cart implements Serializable {
 private final Map<Long,Integer> quantities=new LinkedHashMap<>();
 public void add(long id) { update(id,quantities.getOrDefault(id,0)+1); }
 public void update(long id,int quantity) { if(quantity<1 || quantity>10) throw new IllegalArgumentException("Quantity must be between 1 and 10."); quantities.put(id,quantity); }
 public void remove(long id) { quantities.remove(id); }
 public Map<Long,Integer> quantities() { return Collections.unmodifiableMap(quantities); }
 public int count() { return quantities.values().stream().mapToInt(Integer::intValue).sum(); }
 public void clear() { quantities.clear(); }
}
