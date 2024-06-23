package com.github.buoooou.web.front.article.vo;

import com.github.buoooou.api.model.vo.PageListVo;
import com.github.buoooou.api.model.vo.article.dto.ArticleDTO;
import lombok.Data;

/**
 * @author YiHui
 * @date 2022/10/28
 */
@Data
public class ArticleListVo {
    /**
     * 归档类型
     */
    private String archives;
    /**
     * 归档id
     */
    private Long archiveId;

    private PageListVo<ArticleDTO> articles;
}
