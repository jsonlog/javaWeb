package demo.msa.product.test;

import demo.msa.product.model.Product;
import demo.msa.product.service.ProductService;
import org.assertj.core.api.Assertions;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductServiceTest2 {

  @Autowired
  private ProductService productService;

  @Test
  public void test1_getProductById() throws Exception {
    Product product = productService.getProductById(1);
    Assertions.assertThat(product).isNotNull();
  }

  @Test
  public void test2_createProduct() throws Exception {
    Product product = productService.createProduct("iMac", 8000);
    Assertions.assertThat(product).isNotNull();
  }

  @Test
  public void test3_updateProduct() throws Exception {
    Map<String, Object> fieldMap = new HashMap<>();
    fieldMap.put("price", 9000);
    Product product = productService.updateProduct(4, fieldMap);
    Assertions.assertThat(product).isNotNull();
    Assertions.assertThat(product.getPrice()).isEqualTo(9000);
  }

  @Test
  public void test4_deleteProductById() throws Exception {
    boolean result = productService.deleteProductById(4);
    Assertions.assertThat(result).isTrue();
  }

  @Test
  public void test5_getProductList() throws Exception {
    List<Product> productList = productService.getProductList();
    Assertions.assertThat(productList).hasSize(3);
  }
}
