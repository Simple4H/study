package com.simple.service;


import com.github.pagehelper.PageInfo;
import com.simple.common.ServerResponse;
import com.simple.pojo.Shipping;

/**
 * Create by S I M P L E on 2017/09/29
 */
public interface IShippingService {

    ServerResponse add(Integer userId, Shipping shipping);

    ServerResponse<String> del(Integer userId, Integer shippingId);

    ServerResponse<String> update(Integer userId, Shipping shipping);

    ServerResponse<Shipping> select(Integer userId, Integer shippingId);

    ServerResponse<PageInfo> list(Integer userId, int pageNum, int pageSize);
}
