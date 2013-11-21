package com.smart.sample.service;

import com.smart.framework.annotation.Impl;
import com.smart.sample.entity.Customer;
import com.smart.sample.service.impl.CustomerServiceCacheAnnotationImpl;
import java.util.List;
import java.util.Map;

@Impl(CustomerServiceCacheAnnotationImpl.class)
public interface CustomerService {

    List<Customer> getCustomerList();

    boolean deleteCustomer(long id);

    Customer getCustomer(long id);

    boolean updateCustomer(long id, Map<String, Object> fieldMap);

    boolean createCustomer(Map<String, Object> fieldMap);
}
