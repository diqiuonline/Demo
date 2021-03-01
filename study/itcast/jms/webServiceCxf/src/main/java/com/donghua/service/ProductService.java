package com.donghua.service;

import com.donghua.pojo.Product;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;
@Produces("*/*")
public interface ProductService {
    @GET
    @Path("/product")
    @Produces({ "application/xml", "application/json" })
    List<Product>getProductList();
}
