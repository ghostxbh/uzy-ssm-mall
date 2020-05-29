package com.uzykj.mall.service;

import com.uzykj.mall.entity.Address;

import java.util.List;

public interface AddressService {
    boolean add(Address address);

    boolean update(Address address);

    List<Address> getList(String address_name, String address_regionId);

    Address get(String address_areaId);

    List<Address> getRoot();
}
