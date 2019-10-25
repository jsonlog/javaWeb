package demo.msa.product.controller;

import demo.msa.product.model.Product;
import demo.msa.product.request.ProductRequest;
import demo.msa.product.response.ProductResponse;
import demo.msa.product.service.ProductService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(produces = "application/json")
public class ProductController {

  @Autowired
  private ProductService productService;

  @ApiOperation("根据产品 ID 查询产品")
  @ApiImplicitParams({
      @ApiImplicitParam(value = "产品 ID", paramType = "path", dataType = "long", name = "id", required = true)
  })
  @ApiResponses({
      @ApiResponse(code = 200, message = "成功"),
      @ApiResponse(code = 400, message = "错误请求"),
      @ApiResponse(code = 600, message = "无效的产品 ID")
  })
  @GetMapping("/product/{id}")
  public ResponseEntity<Product> getProductById(@PathVariable("id") long id) {
    if (id <= 0) {
      return ResponseEntity.status(600).build();
    }
    Product product = productService.getProductById(id);
    if (product != null) {
      return ResponseEntity.ok(product);
    }
    return ResponseEntity.badRequest().build();
  }

  @ApiOperation("创建产品")
  @ApiImplicitParams({
      @ApiImplicitParam(value = "产品请求对象", paramType = "body", dataType = "ProductRequest", name = "productRequest", required = true)
  })
  @ApiResponses({
      @ApiResponse(code = 200, message = "成功"),
      @ApiResponse(code = 400, message = "错误请求"),
      @ApiResponse(code = 600, message = "无效的产品请求对象")
  })
  @PostMapping("/product")
  public ResponseEntity<Product> createProduct(@RequestBody ProductRequest productRequest) {
    if (productRequest == null) {
      return ResponseEntity.status(600).build();
    }
    String name = productRequest.getName();
    int price = productRequest.getPrice();
    Product product = productService.createProduct(name, price);
    if (product != null) {
      return ResponseEntity.ok(product);
    }
    return ResponseEntity.badRequest().build();
  }

  @ApiOperation("根据产品 ID 更新产品")
  @ApiImplicitParams({
      @ApiImplicitParam(value = "产品 ID", paramType = "path", dataType = "long", name = "id", required = true),
      @ApiImplicitParam(value = "产品相关属性", paramType = "body", dataType = "Map<String, Object>", name = "fieldMap", required = true)
  })
  @ApiResponses({
      @ApiResponse(code = 200, message = "成功"),
      @ApiResponse(code = 400, message = "错误请求"),
      @ApiResponse(code = 600, message = "无效的产品 ID")
  })
  @PutMapping("/product/{id}")
  public ResponseEntity<Product> updateProduct(@PathVariable("id") long id, @RequestBody Map<String, Object> fieldMap) {
    if (id <= 0) {
      return ResponseEntity.status(600).build();
    }
    Product product = productService.updateProduct(id, fieldMap);
    if (product != null) {
      return ResponseEntity.ok(product);
    }
    return ResponseEntity.badRequest().build();
  }

  @ApiOperation("根据产品 ID 删除产品")
  @ApiImplicitParams({
      @ApiImplicitParam(value = "产品 ID", paramType = "path", dataType = "long", name = "id", required = true)
  })
  @ApiResponses({
      @ApiResponse(code = 200, message = "成功"),
      @ApiResponse(code = 400, message = "错误请求"),
      @ApiResponse(code = 600, message = "无效的产品 ID")
  })
  @DeleteMapping("/product/{id}")
  public ResponseEntity<Product> deleteProductById(@PathVariable("id") long id) {
    if (id <= 0) {
      return ResponseEntity.status(600).build();
    }
    Product product = productService.getProductById(id);
    if (product != null) {
      boolean result = productService.deleteProductById(id);
      if (result) {
        return ResponseEntity.ok(product);
      }
    }
    return ResponseEntity.badRequest().build();
  }

  @ApiOperation("获取所有产品")
  @ApiResponses({
      @ApiResponse(code = 200, message = "成功"),
      @ApiResponse(code = 400, message = "错误请求")
  })
  @GetMapping("/product")
  public ResponseEntity<ProductResponse> getAllProducts() {
    List<Product> productList = productService.getProductList();
    if (productList != null) {
      ProductResponse productResponse = new ProductResponse();
      productResponse.setProductList(productList);
      productResponse.setTotal(productList.size());
      return ResponseEntity.ok(productResponse);
    }
    return ResponseEntity.badRequest().build();
  }
}
