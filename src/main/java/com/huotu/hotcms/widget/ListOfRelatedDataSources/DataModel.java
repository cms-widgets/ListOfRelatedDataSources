package com.huotu.hotcms.widget.ListOfRelatedDataSources;

import com.huotu.hotcms.service.entity.Category;


/**
 * Created by lhx on 2016/11/24.
 */
public class DataModel {
    private Category category;
    private String contentURI;

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getContentURI() {
        return contentURI;
    }

    public void setContentURI(String contentURI) {
        this.contentURI = contentURI;
    }
}
