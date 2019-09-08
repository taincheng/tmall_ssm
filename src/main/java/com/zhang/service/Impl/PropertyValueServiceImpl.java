package com.zhang.service.Impl;

import com.zhang.mapper.PropertyValueMapper;
import com.zhang.pojo.Product;
import com.zhang.pojo.Property;
import com.zhang.pojo.PropertyValue;
import com.zhang.pojo.PropertyValueExample;
import com.zhang.service.PropertyService;
import com.zhang.service.PropertyValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author TianCheng
 * @Date 2019/9/8 13:14
 */
@Service
public class PropertyValueServiceImpl implements PropertyValueService {

    @Autowired
    PropertyValueMapper propertyValueMapper;
    @Autowired
    PropertyService propertyService;

    @Override
    public void init(Product p) {
        //获取分类下的所有属性
        List<Property> propertys = propertyService.list(p.getCid());
        for(Property pt : propertys){
            PropertyValue propertyValue = get(pt.getId(), p.getId());
            if(null == propertyValue){
                propertyValue = new PropertyValue();
                propertyValue.setPid(p.getId());
                propertyValue.setPtid(pt.getId());
                propertyValueMapper.insert(propertyValue);
            }
        }
    }

    @Override
    public void update(PropertyValue pv) {
        propertyValueMapper.updateByPrimaryKeySelective(pv);
    }

    @Override
    public PropertyValue get(int ptid, int pid) {
        PropertyValueExample example = new PropertyValueExample();
        example.createCriteria().andPidEqualTo(pid).andPtidEqualTo(ptid);
        List<PropertyValue> propertyValues = propertyValueMapper.selectByExample(example);
        if(propertyValues.isEmpty()){
            return null;
        }
        return propertyValues.get(0);
    }

    @Override
    public List<PropertyValue> list(int pid) {
        PropertyValueExample example = new PropertyValueExample();
        example.createCriteria().andPidEqualTo(pid);
        List<PropertyValue> propertyValues = propertyValueMapper.selectByExample(example);
        for(PropertyValue pv : propertyValues){
            Property property = propertyService.get(pv.getPtid());
            pv.setProperty(property);
        }
        return propertyValues;
    }
}
