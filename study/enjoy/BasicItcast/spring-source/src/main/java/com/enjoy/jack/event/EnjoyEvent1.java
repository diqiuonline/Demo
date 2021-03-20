package com.enjoy.jack.event;

import lombok.Data;
import org.springframework.context.ApplicationEvent;


@Data
public class EnjoyEvent1 extends ApplicationEvent {

    private String name;

    public EnjoyEvent1(Object source, String name) {
        super(source);
        this.name = name;
    }
}
