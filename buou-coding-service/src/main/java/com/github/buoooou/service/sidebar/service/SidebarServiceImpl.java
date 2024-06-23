package com.github.buoooou.service.sidebar.service;

import com.github.buoooou.api.model.enums.ConfigTypeEnum;
import com.github.buoooou.api.model.enums.SidebarStyleEnum;
import com.github.buoooou.api.model.enums.rank.ActivityRankTimeEnum;
import com.github.buoooou.api.model.vo.PageListVo;
import com.github.buoooou.api.model.vo.PageParam;
import com.github.buoooou.api.model.vo.article.dto.SimpleArticleDTO;
import com.github.buoooou.api.model.vo.banner.dto.ConfigDTO;
import com.github.buoooou.api.model.vo.rank.dto.RankItemDTO;
import com.github.buoooou.api.model.vo.recommend.RateVisitDTO;
import com.github.buoooou.api.model.vo.recommend.SideBarDTO;
import com.github.buoooou.api.model.vo.recommend.SideBarItemDTO;
import com.github.buoooou.core.util.JsonUtil;
import com.github.buoooou.core.util.SpringUtil;
import com.github.buoooou.service.article.repository.dao.ArticleDao;
import com.github.buoooou.service.article.service.ArticleReadService;
import com.github.buoooou.service.config.service.ConfigService;
import com.github.buoooou.service.rank.service.UserActivityRankService;
import com.google.common.base.Splitter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author YiHui
 * @date 2022/9/6
 */
@Service
public class SidebarServiceImpl implements SidebarService {

    @Autowired
    private ArticleReadService articleReadService;

    @Autowired
    private ConfigService configService;

    @Autowired
    private ArticleDao articleDao;

    /**
     * 使用caffeine本地缓存，来处理侧边栏不怎么变动的消息
     * <p>
     * cacheNames -> 类似缓存前缀的概念
     * key -> SpEL 表达式，可以从传参中获取，来构建缓存的key
     * cacheManager -> 缓存管理器，如果全局只有一个时，可以省略
     *
     * @return
     */
    @Override
    @Cacheable(key = "'homeSidebar'", cacheManager = "caffeineCacheManager", cacheNames = "home")
    public List<SideBarDTO> queryHomeSidebarList() {
        List<SideBarDTO> list = new ArrayList<>();
        list.add(noticeSideBar());
        list.add(columnSideBar());
        list.add(hotArticles());
        SideBarDTO bar = rankList();
        if (bar != null) {
            list.add(bar);
        }
        return list;
    }

    /**
     * 公告信息
     *
     * @return
     */
    private SideBarDTO noticeSideBar() {
        List<ConfigDTO> noticeList = configService.getConfigList(ConfigTypeEnum.NOTICE);
        List<SideBarItemDTO> items = new ArrayList<>(noticeList.size());
        noticeList.forEach(configDTO -> {
            List<Integer> configTags;
            if (StringUtils.isBlank(configDTO.getTags())) {
                configTags = Collections.emptyList();
            } else {
                configTags = Splitter.on(",").splitToStream(configDTO.getTags()).map(s -> Integer.parseInt(s.trim())).collect(Collectors.toList());
            }
            items.add(new SideBarItemDTO()
                    .setName(configDTO.getName())
                    .setTitle(configDTO.getContent())
                    .setUrl(configDTO.getJumpUrl())
                    .setTime(configDTO.getCreateTime().getTime())
                    .setTags(configTags)
            );
        });
        return new SideBarDTO()
                .setTitle("关于布欧社区")
                // todo 补充公告图片和地址
                .setImg("")
                .setUrl("")
                .setItems(items)
                .setStyle(SidebarStyleEnum.NOTICE.getStyle());
    }


    /**
     * 推荐教程的侧边栏
     *
     * @return
     */
    private SideBarDTO columnSideBar() {
        List<ConfigDTO> columnList = configService.getConfigList(ConfigTypeEnum.COLUMN);
        List<SideBarItemDTO> items = new ArrayList<>(columnList.size());
        columnList.forEach(configDTO -> {
            SideBarItemDTO item = new SideBarItemDTO();
            item.setName(configDTO.getName());
            item.setTitle(configDTO.getContent());
            item.setUrl(configDTO.getJumpUrl());
            item.setImg(configDTO.getBannerUrl());
            items.add(item);
        });
        return new SideBarDTO().setTitle("精选教程").setItems(items).setStyle(SidebarStyleEnum.COLUMN.getStyle());
    }


    /**
     * 热门文章
     *
     * @return
     */
    private SideBarDTO hotArticles() {
        PageListVo<SimpleArticleDTO> vo = articleReadService.queryHotArticlesForRecommend(PageParam.newPageInstance(1, 8));
        List<SideBarItemDTO> items = vo.getList().stream().map(s -> new SideBarItemDTO().setTitle(s.getTitle()).setUrl("/article/detail/" + s.getId()).setTime(s.getCreateTime().getTime())).collect(Collectors.toList());
        return new SideBarDTO().setTitle("热门文章").setItems(items).setStyle(SidebarStyleEnum.ARTICLES.getStyle());
    }


    /**
     * 以用户 + 文章维度进行缓存设置
     *
     * @param author    文章作者id
     * @param articleId 文章id
     * @return
     */
    @Override
    @Cacheable(key = "'sideBar_' + #articleId", cacheManager = "caffeineCacheManager", cacheNames = "article")
    public List<SideBarDTO> queryArticleDetailSidebarList(Long author, Long articleId) {
        List<SideBarDTO> list = new ArrayList<>(2);
        // 不能直接使用 pdfSideBar()的方式调用，会导致缓存不生效
        list.add(SpringUtil.getBean(SidebarServiceImpl.class).pdfSideBar());
        list.add(recommendByAuthor(author, articleId, PageParam.DEFAULT_PAGE_SIZE));
        return list;
    }

    /**
     * PDF 优质资源
     *
     * @return
     */
    @Cacheable(key = "'sideBar'", cacheManager = "caffeineCacheManager", cacheNames = "article")
    public SideBarDTO pdfSideBar() {
        List<ConfigDTO> pdfList = configService.getConfigList(ConfigTypeEnum.PDF);
        List<SideBarItemDTO> items = new ArrayList<>(pdfList.size());
        pdfList.forEach(configDTO -> {
            SideBarItemDTO dto = new SideBarItemDTO();
            dto.setName(configDTO.getName());
            dto.setUrl(configDTO.getJumpUrl());
            dto.setImg(configDTO.getBannerUrl());
            RateVisitDTO visit;
            if (StringUtils.isNotBlank(configDTO.getExtra())) {
                visit = (JsonUtil.toObj(configDTO.getExtra(), RateVisitDTO.class));
            } else {
                visit = new RateVisitDTO();
            }
            visit.incrVisit();
            // 更新阅读计数
            configService.updateVisit(configDTO.getId(), JsonUtil.toStr(visit));
            dto.setVisit(visit);
            items.add(dto);
        });
        return new SideBarDTO().setTitle("优质PDF").setItems(items).setStyle(SidebarStyleEnum.PDF.getStyle());
    }


    /**
     * 作者的文章列表推荐
     *
     * @param authorId
     * @param size
     * @return
     */
    public SideBarDTO recommendByAuthor(Long authorId, Long articleId, long size) {
        List<SimpleArticleDTO> list = articleDao.listAuthorHotArticles(authorId, PageParam.newPageInstance(PageParam.DEFAULT_PAGE_NUM, size));
        List<SideBarItemDTO> items = list.stream().filter(s -> !s.getId().equals(articleId))
                .map(s -> new SideBarItemDTO()
                        .setTitle(s.getTitle()).setUrl("/article/detail/" + s.getId())
                        .setTime(s.getCreateTime().getTime()))
                .collect(Collectors.toList());
        return new SideBarDTO().setTitle("相关文章").setItems(items).setStyle(SidebarStyleEnum.ARTICLES.getStyle());
    }


    /**
     * 查询教程的侧边栏信息
     *
     * @return
     */
    @Override
    @Cacheable(key = "'columnSidebar'", cacheManager = "caffeineCacheManager", cacheNames = "column")
    public List<SideBarDTO> queryColumnSidebarList() {
        List<SideBarDTO> list = new ArrayList<>();
        list.add(subscribeSideBar());
        return list;
    }


    /**
     * 订阅公众号
     *
     * @return
     */
    private SideBarDTO subscribeSideBar() {
        return new SideBarDTO().setTitle("订阅").setSubTitle("布欧社区")
                .setImg("//cdn.tobebetterjavaer.com/paicoding/a768cfc54f59d4a056f79d1c959dcae9.jpg")
                .setContent("校招必刷八股文")
                .setStyle(SidebarStyleEnum.SUBSCRIBE.getStyle());
    }


    @Autowired
    private UserActivityRankService userActivityRankService;

    /**
     * 排行榜
     *
     * @return
     */
    private SideBarDTO rankList() {
        List<RankItemDTO> list = userActivityRankService.queryRankList(ActivityRankTimeEnum.MONTH, 8);
        if (list.isEmpty()) {
            return null;
        }
        SideBarDTO sidebar = new SideBarDTO().setTitle("月度活跃排行榜").setStyle(SidebarStyleEnum.ACTIVITY_RANK.getStyle());
        List<SideBarItemDTO> itemList = new ArrayList<>();
        for (RankItemDTO item : list) {
            SideBarItemDTO sideItem = new SideBarItemDTO().setName(item.getUser().getName())
                    .setUrl(String.valueOf(item.getUser().getUserId()))
                    .setImg(item.getUser().getAvatar())
                    .setTime(item.getScore().longValue());
            itemList.add(sideItem);
        }
        sidebar.setItems(itemList);
        return sidebar;
    }
}
