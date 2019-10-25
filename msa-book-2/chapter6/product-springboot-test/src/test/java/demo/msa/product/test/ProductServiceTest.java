package demo.msa.product.test;

import demo.msa.product.model.Product;
import demo.msa.product.service.ProductService;
import org.junit.Assert;
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
public class ProductServiceTest {

  @Autowired
  private ProductService productService;

  @Test
  public void test1_getProductById() throws Exception {
    Product product = productService.getProductById(1);
    Assert.assertNotNull("product is null", product);
  }

  @Test
  public void test2_createProduct() throws Exception {
    Product product = productService.createProduct("iMac", 8000);
    Assert.assertNotNull("product is null", product);
  }

  @Test
  public void test3_updateProduct() throws Exception {
    Map<String, Object> fieldMap = new HashMap<>();
    fieldMap.put("price", 9000);
    Product product = productService.updateProduct(4, fieldMap);
    Assert.assertNotNull("product is null", product);
    Assert.assertEquals("product.price is wrong", 9000, product.getPrice());
  }

  @Test
  public void test4_deleteProductById() throws Exception {
    boolean result = productService.deleteProductById(4);
    Assert.assertTrue("delete failure", result);
  }

  @Test
  public void test5_getProductList() throws Exception {
    List<Product> productList = productService.getProductList();
    Assert.assertNotNull("productList is null", productList);
    Assert.assertEquals("productList.size is wrong", 3, productList.size());
  }
}
