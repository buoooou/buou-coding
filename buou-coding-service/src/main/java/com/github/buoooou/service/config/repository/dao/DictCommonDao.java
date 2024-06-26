package com.github.buoooou.service.config.repository.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.buoooou.api.model.vo.article.dto.DictCommonDTO;
import com.github.buoooou.service.config.repository.entity.DictCommonDO;
import com.github.buoooou.service.config.converter.DictCommonConverter;
import com.github.buoooou.service.config.repository.mapper.DictCommonMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Louzai
 * @date 2022/9/2
 */
@Repository
public class DictCommonDao extends ServiceImpl<DictCommonMapper, DictCommonDO> {

    /**
     * 获取所有字典列表
     * @return
     */
    public List<DictCommonDTO> getDictList() {
        List<DictCommonDO> list = lambdaQuery().list();
        return DictCommonConverter.toDTOS(list);
    }
}
