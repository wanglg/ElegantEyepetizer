package com.android.leo.toutiao.mvp.model.entity;

import java.io.Serializable;
import java.util.List;


public class ImageEntity implements Serializable {
    /**
     * url : http://p3.pstatp.com/list/300x196/2c23000095ae9f56b15f.webp
     * width : 700
     * url_list : [{"url":"http://p3.pstatp.com/list/300x196/2c23000095ae9f56b15f.webp"},{"url":"http://pb9.pstatp.com/list/300x196/2c23000095ae9f56b15f.webp"},{"url":"http://pb1.pstatp.com/list/300x196/2c23000095ae9f56b15f.webp"}]
     * uri : list/2c23000095ae9f56b15f
     * height : 393
     */

    public String url;
    public int width;
    public String uri;
    public int height;
    public List<UrlListBeanX> url_list;

    public static class UrlListBeanX {
        /**
         * url : http://p3.pstatp.com/list/300x196/2c23000095ae9f56b15f.webp
         */

        public String url;
    }
}
