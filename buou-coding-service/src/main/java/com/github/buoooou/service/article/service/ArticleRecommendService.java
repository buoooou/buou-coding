package com.github.buoooou.service.article.service;

import com.github.buoooou.api.model.vo.PageListVo;
import com.github.buoooou.api.model.vo.PageParam;
import com.github.buoooou.api.model.vo.article.dto.ArticleDTO;

/**
 * @author YiHui
 * @date 2022/9/26
 */
public interface ArticleRecommendService {
    /**
     * 文章关联推荐
     *
     * @param article
     * @param pageParam
     * @return
     */
    PageListVo<ArticleDTO> relatedRecommend(Long article, PageParam pageParam);
}
