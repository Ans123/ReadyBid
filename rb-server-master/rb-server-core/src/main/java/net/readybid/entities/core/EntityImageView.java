package net.readybid.entities.core;


import net.readybid.app.core.entities.entity.EntityImage;
import net.readybid.web.ViewModel;
import net.readybid.web.ViewModelFactory;

/**
 * Created by DejanK on 12/26/2016.
 *
 */
public class EntityImageView implements ViewModel<EntityImage> {

    public static final ViewModelFactory<EntityImage, EntityImageView> FACTORY = EntityImageView::new;
    public String url;
    public String thumbnailUrl;
    public String caption;
    public int width;
    public int height;

    public EntityImageView() {}

    public EntityImageView(EntityImage image) {
        final EntityImageImpl e = (EntityImageImpl) image;
        url = e.getUrl();
        thumbnailUrl = e.getThumbnailUrl();
        caption = e.getCaption();
        width = e.getWidth();
        height = e.getHeight();
    }
}
