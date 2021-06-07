package com.enjoy.jack.scope;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("refreshScope")
@Data
public class CustomScopeBean {
    private String username;
}
