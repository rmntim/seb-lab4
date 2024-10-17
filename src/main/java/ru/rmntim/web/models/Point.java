package ru.rmntim.web.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
@Table(name = "point")
public class Point implements Serializable {
    @Id
    @GeneratedValue
    private int id;

    private double x;
    private double y;
    private double r;
    private boolean insideArea;
    private Date timestamp;
    private long executionTime;

    public Point(double x, double y, double r) {
        this.x = x;
        this.y = y;
        this.r = r;
    }

    public void calc() {
        long now = System.nanoTime();
        insideArea = (x <= 0 && y >= 0 && x >= -r && y <= (x + r) / 2.0) ||
                (x >= 0 && y >= 0 && x <= r && y <= r / 2.0) ||
                (x >= 0 && y <= 0 && (Math.pow(x, 2) + Math.pow(y, 2) <= Math.pow(r / 2, 2)));

        timestamp = new Date(System.currentTimeMillis());
        executionTime = System.nanoTime() - now;
    }

}