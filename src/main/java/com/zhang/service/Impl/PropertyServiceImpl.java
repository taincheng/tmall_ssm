package com.zhang.service.Impl;

import com.zhang.mapper.PropertyMapper;
import com.zhang.pojo.Property;
import com.zhang.pojo.PropertyExample;
import com.zhang.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author TianCheng
 * @Date 2019/9/7 22:36
 */
@Service
public class PropertyServiceImpl implements PropertyService{

    @Autowired
    PropertyMapper propertyMapper;

    @Override
    public void add(Property property) {
        propertyMapper.insert(property);
    }

    @Override
    public void delete(int id) {
        propertyMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(Property property) {
        propertyMapper.updateByPrimaryKeySelective(property);
    }

    @Override
    public List list(int cid) {
        PropertyExample example =new PropertyExample();
        example.createCriteria().andCidEqualTo(cid);
        example.setOrderByClause("id desc");
        return propertyMapper.selectByExample(example);
    }

    @Override
    public Property get(int id) {
        return propertyMapper.selectByPrimaryKey(id);
    }
}
