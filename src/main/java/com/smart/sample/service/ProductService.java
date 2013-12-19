package com.smart.sample.service;

import com.smart.framework.bean.Multipart;
import com.smart.framework.bean.Pager;
import com.smart.sample.bean.ProductBean;
import com.smart.sample.entity.Product;
import com.smart.sample.entity.ProductType;
import java.util.List;
import java.util.Map;

public interface ProductService {

    boolean createProduct(Map<String, Object> fieldMap, Multipart multipart);

    boolean deleteProduct(long id);

    boolean updateProduct(long id, Map<String, Object> fieldMap, Multipart multipart);

    Product getProduct(long id);

    ProductBean getProductBean(long id);

    Pager<ProductBean> getProductBeanPager(int pageNumber, int pageSize, String name);

    List<ProductType> getProductTypeList();
}