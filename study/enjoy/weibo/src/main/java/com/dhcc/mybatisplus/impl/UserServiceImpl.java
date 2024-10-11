package com.dhcc.mybatisplus.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dhcc.mybatisplus.IUserService;
import com.dhcc.mybatisplus.User;
import com.dhcc.mybatisplus.UserMapper;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends  ServiceImpl<UserMapper,User> implements IUserService {

}
