package com.dhcc.shanjupay.transaction.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dhcc.shanjupay.transaction.api.dto.PayChannelDTO;
import com.dhcc.shanjupay.transaction.entity.PlatformChannel;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author author
 * @since 2019-11-15
 */
@Repository
public interface PlatformChannelMapper extends BaseMapper<PlatformChannel> {
    /**
     * 根据服务类型查询支付渠道
     * @param platformChannelCode
     * @return
     */
    @Select("SELECT" +
            "* " +
            "FROM " +
            "platform_pay_channel ppc," +
            "pay_channel pc," +
            "platform_channel plc " +
            "WHERE " +
            "ppc.PAY_CHANNEL = pc.CHANNEL_CODE " +
            "AND ppc.PLATFORM_CHANNEL = plc.CHANNEL_CODE " +
            "AND plc.CHANNEL_CODE = #{platformChannelCode}")
    List<PayChannelDTO> selectPayChannelByPlatformChannel(String platformChannelCode);
}
