package com.github.buoooou.service.config.service;

import com.github.buoooou.api.model.vo.PageVo;
import com.github.buoooou.api.model.vo.config.GlobalConfigReq;
import com.github.buoooou.api.model.vo.config.SearchGlobalConfigReq;
import com.github.buoooou.api.model.vo.config.dto.GlobalConfigDTO;

/**
 * 微信搜索「沉默王二」，回复 Java
 *
 * @author 沉默王二
 * @date 6/30/23
 */
public interface GlobalConfigService {
    PageVo<GlobalConfigDTO> getList(SearchGlobalConfigReq req);

    void save(GlobalConfigReq req);

    void delete(Long id);

    /**
     * 添加敏感词白名单
     *
     * @param word
     */
    void addSensitiveWhiteWord(String word);
}
