package io.florianlopes.usersmanagement.api.common.web.dto;

public class OrderDto {

    public enum Direction {
        ASC,
        DESC
    }

    private String property;

    private Direction direction;

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }
}
