/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.ListOfRelatedDataSources;

import java.util.*;

import com.huotu.hotcms.service.common.PageType;
import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.exception.PageNotFoundException;
import com.huotu.hotcms.service.repository.CategoryRepository;
import com.huotu.hotcms.widget.*;
import com.huotu.hotcms.widget.entity.PageInfo;
import com.huotu.hotcms.widget.service.PageService;
import me.jiangcai.lib.resource.service.ResourceService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.util.Map;


/**
 * @author CJ
 */
public class WidgetInfo implements Widget, PreProcessWidget {
    /*
     * 指定风格的模板类型 如：html,text等
     */
    public static final String VALID_STYLE_TEMPLATE = "styleTemplate";

    @Override
    public String groupId() {
        return "com.huotu.hotcms.widget.ListOfRelatedDataSources";
    }

    @Override
    public String widgetId() {
        return "ListOfRelatedDataSources";
    }

    @Override
    public String name(Locale locale) {
        if (locale.equals(Locale.CHINA)) {
            return "A custom Widget";
        }
        return "ListOfRelatedDataSources";
    }

    @Override
    public String description(Locale locale) {
        if (locale.equals(Locale.CHINA)) {
            return "这是一个 A custom Widget，你可以对组件进行自定义修改。";
        }
        return "This is a ListOfRelatedDataSources,  you can make custom change the component.";
    }

    @Override
    public String dependVersion() {
        return "1.0-SNAPSHOT";
    }

    @Override
    public WidgetStyle[] styles() {
        return new WidgetStyle[]{new DefaultWidgetStyle()};
    }

    @Override
    public Resource widgetDependencyContent(MediaType mediaType) {
        if (mediaType.equals(Widget.Javascript))
            return new ClassPathResource("js/widgetInfo.js", getClass().getClassLoader());
        return null;
    }

    @Override
    public Map<String, Resource> publicResources() {
        Map<String, Resource> map = new HashMap<>();
        map.put("thumbnail/defaultStyleThumbnail.png", new ClassPathResource("thumbnail/defaultStyleThumbnail.png"
                , getClass().getClassLoader()));
        return map;
    }

    @Override
    public void valid(String styleId, ComponentProperties componentProperties) throws IllegalArgumentException {
        WidgetStyle style = WidgetStyle.styleByID(this, styleId);
        //加入控件独有的属性验证

    }

    @Override
    public PageType supportedPageType() {
        return PageType.DataContent;
    }

    @Override
    public Class springConfigClass() {
        return null;
    }


    @Override
    public ComponentProperties defaultProperties(ResourceService resourceService) throws IOException {
        ComponentProperties properties = new ComponentProperties();
        return properties;
    }

    @Override
    public void prepareContext(WidgetStyle style, ComponentProperties properties, Map<String, Object> variables
            , Map<String, String> parameters) {
        CategoryRepository categoryRepository = getCMSServiceFromCMSContext(CategoryRepository.class);
        Category category = CMSContext.RequestContext().getAbstractContent() != null
                ? CMSContext.RequestContext().getAbstractContent().getCategory() : null;
        PageService pageService = getCMSServiceFromCMSContext(PageService.class);
        List<DataModel> dataModels = new ArrayList<>();
        if (category != null && category.getParent() != null) {
            variables.put("contentCategory", category);
            variables.put("parentCategory", category.getParent());
            List<Category> categories = categoryRepository.findByParent_SerialAndDeletedFalse(category.getParent().getSerial());
            for (Category c : categories) {
                DataModel dataModel = new DataModel();
                dataModel.setCategory(c);
                try {
                    PageInfo pageInfo = pageService.getClosestContentPage(c, null,PageType.DataIndex);
                    if (pageInfo != null) {
                        dataModel.setContentURI(pageInfo.getPagePath());
                    }
                } catch (PageNotFoundException e) {
                    e.printStackTrace();
                    dataModel.setContentURI("");
                }
                dataModels.add(dataModel);
            }
            variables.put("dataModels", dataModels);
        } else {
            variables.put("contentCategory", null);
            variables.put("parentCategory", null);
            variables.put("dataModels", dataModels);
        }
    }

}
