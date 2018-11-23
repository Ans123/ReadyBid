package net.readybid.web;

/**
 * Created by DejanK on 4/4/2018.
 *
 */
public class RbViewModels {

    public static final RbViewModel ACTION_SUCCESS;

    private static final RbViewModel NULL_VIEW_MODEL;

    private RbViewModels(){}

    static {
        NULL_VIEW_MODEL = new RbViewModel() {
            @Override
            public Long getCount() {
                return null;
            }

            @Override
            public Object getData() {
                return null;
            }
        };

        ACTION_SUCCESS = NULL_VIEW_MODEL;
    }
}
