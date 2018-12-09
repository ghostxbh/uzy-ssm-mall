package club.aiit.bookstore.service.impl;

import club.aiit.bookstore.dao.LastIDMapper;
import club.aiit.bookstore.service.LastIDService;
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
