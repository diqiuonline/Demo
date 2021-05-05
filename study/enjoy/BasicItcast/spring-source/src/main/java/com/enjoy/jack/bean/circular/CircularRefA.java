package com.enjoy.jack.bean.circular;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * A
 * 	Autowired B ,C
 * B
 * 	Autowired A ,C
 * C
 * 	Autowired A ,B
 * 先getBean(A)
 * 	从缓存中获取A  null 实例化A 三级缓存中存储A  这个时候一级缓存中 空 二级缓存中 空 三级缓存中：A
 * 	扫描到B,C
 *
 * 	B.getBean() 从缓存中获取B  null 实例化B 三级缓存中存储B  这个时候一级缓存中 空 二级缓存中 空 三级缓存中：A，B
 * 	扫描到A，C
 *
 * 		A.getBean() 从缓存中获取A 获取到 升级到2级缓存 从三级缓存中删除A   这个时候一级缓存中 空 二级缓存A 空 三级缓存中：B
 * 		C.getBean() 从缓存中获取C null 实例化C 三级缓存中存储C 这个时候一级缓存中 空 二级缓存A  三级缓存中：B，C
 * 		扫描到A, B
 * 			A.getBean()  从缓存中获取A 获取到
 * 			B.getBean()  从缓存中获取B 获取到 升级到2级缓存 从三级缓存中删除B 这个时候一级缓存中 空 二级缓存A，B 空 三级缓存中：C
 * 			C创建成功 将C升级到1级缓存 删除2，3级缓存 	这个时候一级缓存中 C 二级缓存A，B 空 三级缓存中：空
 * 	B创建成功 将B升级到1级缓存 删除2，3级缓存 		这个时候一级缓存中 B,C 二级缓存A  三级缓存中：空
 * 	C.getBean() 从缓存中获取C
 *
 * A创建成功， 将A升级到1级缓存 删除2，3级缓存 这个时候一级缓存中A,B,C 二级缓存 空 三级缓存中：空
 *
 */
@Data
@Component
public class CircularRefA {

    public CircularRefA() {
        System.out.println("============CircularRefA()===========");
    }

    //这里会触发CircularRefB类型的getBean操作
    @Autowired
    private CircularRefB circularRefB;

    //这里会触发CircularRefB类型的getBean操作
    @Autowired
    private CircularRefC circularRefC;
}
