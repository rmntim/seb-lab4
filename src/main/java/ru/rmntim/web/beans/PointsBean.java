package ru.rmntim.web.beans;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.Destroyed;
import jakarta.enterprise.context.SessionScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import ru.rmntim.web.models.Point;
import ru.rmntim.web.tools.DBCommunicator;
import ru.rmntim.web.tools.MBeanRegistryUtil;

import java.io.Serializable;
import java.util.ArrayList;

import javax.management.AttributeChangeNotification;
import javax.management.MBeanNotificationInfo;
import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;

@Named
@SessionScoped
public class PointsBean extends NotificationBroadcasterSupport implements Serializable, PointsMXBean {
    @Getter
    private double x;
    @Getter
    private double y;
    @Setter
    @Getter
    private double r;

    private long sequenceNumber = 1;

    @Setter
    @Getter
    private ArrayList<Point> bigList;
    private DBCommunicator dbCommunicator;

    @Inject
    private PercentageBean percentageBean;

    @PostConstruct
    public void init() {
        x = 0;
        y = 0;
        r = 3;
        dbCommunicator = DBCommunicator.getInstance();
        bigList = dbCommunicator.getAll();
        if (bigList == null) {
            bigList = new ArrayList<>();
        }
        MBeanRegistryUtil.registerBean(this, "points");
    }

    public void reset() {
        dbCommunicator.clearAll();
        bigList.clear();
    }

    public void destroy(@Observes @Destroyed(SessionScoped.class) Object unused) {
        MBeanRegistryUtil.unregisterBean(this);
    }

    @Override
    public MBeanNotificationInfo[] getNotificationInfo() {
        var types = new String[] { AttributeChangeNotification.ATTRIBUTE_CHANGE };
        var name = AttributeChangeNotification.class.getName();
        var description = "The point is outside of area.";
        var info = new MBeanNotificationInfo(types, name, description);
        return new MBeanNotificationInfo[] { info };
    }

    public String calc() {
        var point = new Point(x, y, r);
        point.calc();

        bigList.add(point);
        dbCommunicator.sendOne(point);

        if (!point.isInsideArea()) {
            var notification = new Notification(
                    "Point is outside of area",
                    getClass().getSimpleName(),
                    sequenceNumber++,
                    "Point is outside of area");
            sendNotification(notification);
        }

        percentageBean.setTotalPoints(getTotalPoints());
        percentageBean.setMissedPoints(getMissedPoints());

        return "update";
    }

    public void setX(double x) {
        this.x = ((Long) Math.round(x * 1000)).doubleValue() / 1000;
    }

    public void setY(double y) {
        this.y = ((Long) Math.round(y * 1000)).doubleValue() / 1000;
    }

    @Override
    public long getTotalPoints() {
        return bigList.size();
    }

    @Override
    public long getMissedPoints() {
        return bigList.stream().filter(point -> !point.isInsideArea()).count();
    }
} 