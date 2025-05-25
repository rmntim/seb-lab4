package ru.rmntim.web.beans;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.Destroyed;
import jakarta.enterprise.context.SessionScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import ru.rmntim.web.tools.MBeanRegistryUtil;

import java.io.Serializable;

@Named
@SessionScoped
public class PercentageBean implements Serializable, PercentageMXBean {
    @Getter
    @Setter
    private long totalPoints;

    @Getter
    @Setter
    private long missedPoints;

    @PostConstruct
    public void init() {
        MBeanRegistryUtil.registerBean(this, "percentage");
    }

    public void destroy(@Observes @Destroyed(SessionScoped.class) Object unused) {
        MBeanRegistryUtil.unregisterBean(this);
    }

    @Override
    public double getMissedPercentage() {
        return (double) missedPoints / totalPoints * 100;
    }
}