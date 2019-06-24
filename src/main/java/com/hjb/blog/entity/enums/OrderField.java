package com.hjb.blog.entity.enums;

/**
 * 排序字段
 * @author 胡江斌
 * @version 1.0
 * @title: OrderField
 * @projectName blog
 * @description: TODO
 * @date 2019/6/17 20:06
 */
public class OrderField {

    /**
     * 排序的字段(java实体类字段）
     */
    private String orderField;

    /**
     * 排序方式
     */
    private OrderStatus orderType;

    /**
     * 不排序
     */
    public static final OrderField NONE = orderBy(null, OrderStatus.NONE);

    /**
     * 排序方式
     */
    public enum OrderStatus{
        /** 顺序 */
        ASC,
        /** 倒叙 */
        DESC,
        /** 不进行排序 */
        NONE;
    }

    public OrderField() {
    }

    public OrderField(String orderField, OrderStatus orderType) {
        this.orderField = orderField;
        this.orderType = orderType;
    }

    public static OrderField orderBy(String orderField, OrderStatus orderStatus) {
        return new OrderField(orderField, orderStatus);
    }

    /**
     * 不排序
     * @param orderField
     * @return
     */
    public static OrderField orderBy(String orderField) {
        return new OrderField(orderField, OrderStatus.NONE);
    }

    public static OrderField orderByAsc(String orderField) {
        return orderBy(orderField, OrderStatus.ASC);
    }

    public static OrderField orderByDesc(String orderField) {
        return orderBy(orderField, OrderStatus.DESC);
    }

    public String getOrderField() {
        return orderField;
    }

    public void setOrderField(String orderField) {
        this.orderField = orderField;
    }

    public OrderStatus getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderStatus orderType) {
        this.orderType = orderType;
    }

    @Override
    public String toString() {
        return "OrderField{" +
                "orderField='" + orderField + '\'' +
                ", orderType=" + orderType +
                '}';
    }
}
