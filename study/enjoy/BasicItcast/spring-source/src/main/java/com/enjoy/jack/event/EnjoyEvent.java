package com.enjoy.jack.event;

import lombok.Data;
import org.springframework.context.ApplicationEvent;


@Data
public class EnjoyEvent extends ApplicationEvent {

    private String name;

    public EnjoyEvent(Object source,String name) {
        super(source);
        this.name = name;
    }
}
