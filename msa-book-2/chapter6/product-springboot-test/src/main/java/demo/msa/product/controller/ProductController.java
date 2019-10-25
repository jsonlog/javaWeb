package demo.msa.product.controller;

import demo.msa.product.model.Product;
import demo.msa.product.request.ProductRequest;
import demo.msa.product.response.ProductResponse;
import demo.msa.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class ProductController {

  @Autowired
  private ProductService productService;

  @GetMapping("/product/{id}")
  public Product getProductById(@PathVariable("id") long id) {
    return productService.getProductById(id);
  }

  @PostMapping("/product")
  public Product createProduct(@RequestBody ProductRequest productRequest) {
    String name = productRequest.getName();
    int price = productRequest.getPrice();
    return productService.createProduct(name, price);
  }

  @PutMapping("/product/{id}")
  public Product updateProduct(@PathVariable("id") long id, @RequestBody Map<String, Object> fieldMap) {
    return productService.updateProduct(id, fieldMap);
  }

  @DeleteMapping("/product/{id}")
  public Product deleteProductById(@PathVariable("id") long id) {
    Product product = productService.getProductById(id);
    return productService.deleteProductById(id) ? product : null;
  }

  @GetMapping("/product")
  public ProductResponse getAllProducts() {
    List<Product> productList = productService.getProductList();
    ProductResponse response = new ProductResponse();
    response.setProductList(productList);
    response.setTotal(productList.size());
    return response;
  }
}
