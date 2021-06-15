package com.enjoy.jack.bean.DeferredImportSelector;

import com.enjoy.jack.bean.Jack;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.core.type.AnnotationMetadata;


public class LisonSelectImport implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
      /*  MergedAnnotations annotations = importingClassMetadata.getAnnotations();
        MergedAnnotation<EnableAspectJAutoProxy> eas = annotations.get(EnableAspectJAutoProxy.class);

        Object proxyTargetClass = eas.getValue("proxyTargetClass").get();*/
        //类的完整限定名，
        return new String[]{Jack.class.getName()};
    }
}
