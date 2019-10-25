package demo.msa.product.service;

import demo.msa.product.model.Product;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ProductService {

  private final static List<Product> productList = new ArrayList<>();

  private final static AtomicInteger idGenerator = new AtomicInteger(1);

  static {
    productList.add(new Product(generateId(), "MacBook", 10000, getCurrentTime()));
    productList.add(new Product(generateId(), "MacBook Air", 7000, getCurrentTime()));
    productList.add(new Product(generateId(), "MacBook Pro", 12000, getCurrentTime()));
  }

  private static int generateId() {
    return idGenerator.getAndIncrement();
  }

  private static long getCurrentTime() {
    return System.currentTimeMillis();
  }

  public Product getProductById(long id) {
    for (Product product : productList) {
      if (product.getId() == id) {
        return product;
      }
    }
    return null;
  }

  public Product createProduct(String name, int price) {
    Product product = new Product(generateId(), name, price, getCurrentTime());
    boolean result = productList.add(product);
    if (result) {
      return product;
    }
    return null;
  }

  public Product updateProduct(long id, Map<String, Object> fieldMap) {
    Product product = getProductById(id);
    if (product != null) {
      for (Map.Entry<String, Object> fieldEntry : fieldMap.entrySet()) {
        Object fieldValue = fieldEntry.getValue();
        if (fieldValue != null) {
          String fieldName = fieldEntry.getKey();
          Field field = ReflectionUtils.findField(Product.class, fieldName);
          ReflectionUtils.makeAccessible(field);
          ReflectionUtils.setField(field, product, fieldValue);
        }
      }
      return product;
    }
    return null;
  }

  public boolean deleteProductById(long id) {
    Iterator<Product> products = productList.iterator();
    while (products.hasNext()) {
      Product product = products.next();
      if (product.getId() == id) {
        products.remove();
        return true;
      }
    }
    return false;
  }

  public List<Product> getProductList() {
    return productList;
  }
}
