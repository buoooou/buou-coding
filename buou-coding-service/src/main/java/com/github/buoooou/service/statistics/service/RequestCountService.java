package com.github.buoooou.service.statistics.service;

import com.github.buoooou.api.model.vo.statistics.dto.StatisticsDayDTO;
import com.github.buoooou.service.statistics.repository.entity.RequestCountDO;

import java.util.List;

/**
 * 微信搜索「沉默王二」，回复 Java
 *
 * @author 沉默王二
 * @date 5/24/23
 */
public interface RequestCountService {
    RequestCountDO getRequestCount(String host);

    void insert(String host);

    void incrementCount(Long id);

    Long getPvTotalCount();

    List<StatisticsDayDTO> getPvUvDayList(Integer day);
}
