package com.commuker.pocketmbta.datamodel;

/**
 * Created by lyu on 2017/3/19.
 */

public class Route {
    private String id;
    private String name;
    private RouteFamily family;
    private RouteState state;
    private RouteDirection direction;

    public Route(String name, RouteState state) {
        this.name = name;
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RouteState getState() {
        return state;
    }

    public void setState(RouteState state) {
        this.state = state;
    }
}
