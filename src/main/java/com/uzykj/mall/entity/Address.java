package com.uzykj.mall.entity;

public class Address {
    private String address_areaId;
    private String address_name;
    private Address address_regionId;

    public Address() {
    }

    @Override
    public String toString() {
        return "Address{" +
                "address_areaId='" + address_areaId + '\'' +
                ", address_name='" + address_name + '\'' +
                ", address_regionId=" + address_regionId +
                '}';
    }

    public Address(String address_areaId, String address_name, Address address_regionId) {
        this.address_areaId = address_areaId;
        this.address_name = address_name;
        this.address_regionId = address_regionId;
    }

    public String getAddress_areaId() {
        return address_areaId;
    }

    public Address setAddress_areaId(String address_areaId) {
        this.address_areaId = address_areaId;
        return this;
    }

    public String getAddress_name() {
        return address_name;
    }

    public Address setAddress_name(String address_name) {
        this.address_name = address_name;
        return this;
    }

    public Address getAddress_regionId() {
        return address_regionId;
    }

    public Address setAddress_regionId(Address address_regionId) {
        this.address_regionId = address_regionId;
        return this;
    }
}
