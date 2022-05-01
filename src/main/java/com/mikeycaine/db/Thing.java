package com.mikeycaine.db;

import org.locationtech.jts.geom.Polygon;

import javax.persistence.*;

@Entity
@Table(name = "thing")
public class Thing {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;

    @Column
    private Polygon polygon;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Polygon getPolygon() {
        return polygon;
    }

    public void setPolygon(Polygon polygon) {
        this.polygon = polygon;
    }
}