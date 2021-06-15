package com.enjoy.jack.bean.DeferredImportSelector;

import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;


@Component
//Import虽然是实例化一个类，Import进来的类可以实现一些接口
@Import({DeferredImportSelectorDemo.class,LisonSelectImport.class})
//@EnableAspectJAutoProxy
public class ImportBean {
}
