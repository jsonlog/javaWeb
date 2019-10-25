package demo.msa.product.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import demo.msa.product.model.Product;

import java.util.List;

public class ProductResponse {

  @JsonProperty("items")
  private List<Product> productList;

  private int total;

  public List<Product> getProductList() {
    return productList;
  }

  public void setProductList(List<Product> productList) {
    this.productList = productList;
  }

  public int getTotal() {
    return total;
  }

  public void setTotal(int total) {
    this.total = total;
  }
}
