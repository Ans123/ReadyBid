package net.readybid.entities.core;

import net.readybid.app.core.entities.entity.EntityImage;

/**
 * Created by DejanK on 12/26/2016.
 *
 */
public class EntityImageImpl implements EntityImage {

    private String url;
    private String thumbnailUrl;
    private String caption;
    private int width;
    private int height;

    public String getUrl() {
        return url;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public String getCaption() {
        return caption;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }
}
