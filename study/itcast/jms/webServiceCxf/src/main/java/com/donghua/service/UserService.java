package com.donghua.service;

import com.donghua.pojo.User;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

public interface UserService {
    @POST
    @Path("/user")
    @Produces({ "application/xml", "application/json" })
    void setUser(User user);
}
