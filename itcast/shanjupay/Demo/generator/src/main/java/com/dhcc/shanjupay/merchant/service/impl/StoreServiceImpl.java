package com.dhcc.shanjupay.merchant.service.impl;

import com.dhcc.shanjupay.merchant.dto.StoreDTO;
import com.dhcc.shanjupay.merchant.mapper.StoreMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dhcc.shanjupay.merchant.service.IStoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author author
 * @since 2020-06-14
 */
@Slf4j
@Service
@Transactional
public class StoreServiceImpl extends ServiceImpl<StoreMapper, StoreDTO> implements IStoreService {

}
