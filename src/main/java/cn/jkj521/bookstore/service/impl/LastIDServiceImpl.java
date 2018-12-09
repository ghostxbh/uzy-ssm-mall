package cn.jkj521.bookstore.service.impl;

import cn.jkj521.bookstore.dao.LastIDMapper;
import cn.jkj521.bookstore.service.LastIDService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("lastIDService")
public class LastIDServiceImpl implements LastIDService {
    private LastIDMapper lastIDMapper;

    @Resource(name = "lastIDMapper")
    public void setLastIDMapper(LastIDMapper lastIDMapper) {
        this.lastIDMapper = lastIDMapper;
    }

    @Override
    public int selectLastID() {
        return lastIDMapper.selectLastID();
    }
}
