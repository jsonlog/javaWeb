package demo.msa.product.controller;

import demo.msa.product.model.Product;
import demo.msa.product.request.ProductRequest;
import demo.msa.product.response.ProductResponse;
import demo.msa.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@SuppressWarnings("JavaDoc")
@RestController
@RequestMapping(produces = "application/json")
public class ProductController {

  @Autowired
  private ProductService productService;

  /**
   * @api {GET} /product/{id} 根据产品 ID 查询产品
   * @apiVersion 1.0.0
   * @apiGroup Product
   * @apiName getProductById
   *
   * @apiParam {Number} id 产品 ID
   *
   * @apiSuccess {Product} product 产品对象
   * @apiSuccess {String} product.id 产品 ID
   * @apiSuccess {String} product.name 产品名称
   * @apiSuccess {Number} product.price 产品价格
   * @apiSuccess {Number} product.created 创建时间
   *
   * @apiError 400 错误请求
   * @apiError 600 无效的产品 ID
   *
   * @apiSuccessExample 输入
   * GET:/product/1
   * @apiSuccessExample 输出
   * {
   *   "id": 1,
   *   "name": "MacBook",
   *   "price": 10000,
   *   "created": xxx
   * }
   */
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

  /**
   * @api {POST} /product 创建产品
   * @apiVersion 1.0.0
   * @apiGroup Product
   * @apiName createProduct
   *
   * @apiParam {ProductRequest} productRequest 产品请求对象
   * @apiParam {String} productRequest.name 产品名称
   * @apiParam {Number} productRequest.price 产品价格
   *
   * @apiSuccess {Product} product 产品对象
   * @apiSuccess {String} product.id 产品 ID
   * @apiSuccess {String} product.name 产品名称
   * @apiSuccess {Number} product.price 产品价格
   * @apiSuccess {Number} product.created 创建时间
   *
   * @apiError 400 错误请求
   * @apiError 600 无效的产品请求对象
   *
   * @apiSuccessExample 输入
   * {
   *   "name": "iMac",
   *   "price": 8000
   * }
   * @apiSuccessExample 输出
   * {
   *   "id": 4
   *   "name": "iMac",
   *   "price": 8000,
   *   "created": xxx
   * }
   */
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

  /**
   * @api {PUT} /product/{id} 根据产品 ID 更新产品
   * @apiVersion 1.0.0
   * @apiGroup Product
   * @apiName updateProduct
   *
   * @apiParam {Number} id 产品 ID
   * @apiParam {Map} fieldMap 产品名称
   *
   * @apiSuccess {Product} product 产品对象
   * @apiSuccess {String} product.id 产品 ID
   * @apiSuccess {String} product.name 产品名称
   * @apiSuccess {Number} product.price 产品价格
   * @apiSuccess {Number} product.created 创建时间
   *
   * @apiError 400 错误请求
   * @apiError 600 无效的产品 ID
   *
   * @apiSuccessExample 输入
   * {
   *   "id": 4
   *   "price": 9000
   * }
   * @apiSuccessExample 输出
   * {
   *   "id": 4
   *   "name": "iMac",
   *   "price": 9000,
   *   "created": xxx
   * }
   */
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

  /**
   * @api {DELETE} /product/{id} 根据产品 ID 删除产品
   * @apiVersion 1.0.0
   * @apiGroup Product
   * @apiName deleteProductById
   *
   * @apiParam {Number} id 产品 ID
   *
   * @apiSuccess {Product} product 产品对象
   * @apiSuccess {String} product.id 产品 ID
   * @apiSuccess {String} product.name 产品名称
   * @apiSuccess {Number} product.price 产品价格
   * @apiSuccess {Number} product.created 创建时间
   *
   * @apiError 400 错误请求
   * @apiError 600 无效的产品 ID
   *
   * @apiSuccessExample 输入
   * GET:/product/4
   * @apiSuccessExample 输出
   * {
   *   "id": 4
   *   "name": "iMac",
   *   "price": 9000,
   *   "created": xxx
   * }
   */
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

  /**
   * @api {GET} /product 获取所有产品
   * @apiVersion 1.0.0
   * @apiGroup Product
   * @apiName getAllProducts
   *
   * @apiSuccess {ProductResponse} productResponse 产品响应对象
   * @apiSuccess {Object} items 产品条目
   * @apiSuccess {String} items.id 产品 ID
   * @apiSuccess {String} items.name 产品名称
   * @apiSuccess {Number} items.price 产品价格
   * @apiSuccess {Number} items.created 创建时间
   * @apiSuccess {Number} total 产品总数
   *
   * @apiError 400 错误请求
   *
   * @apiSuccessExample 输入
   * GET:/product
   * @apiSuccessExample 输出
   * {
   *   "items": [{
   *     "id": 1,
   *     "name": "MacBook",
   *     "price": 10000,
   *     "created": xxx,
   *   },
   *   {
   *     "id": 2,
   *     "name": "MacBook Air",
   *     "price": 7000,
   *     "created": xxx,
   *   },{
   *     "id": 3,
   *     "name": "MacBook Pro",
   *     "price": 12000,
   *     "created": xxx,
   *   }],
   *   "total": 3
   * }
   */
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
