package net.readybid.web;

/**
 * Created by DejanK on 9/6/2016.
 *
 */
public interface RbViewModel<T> {

    default Long getCount() { return null; }

    T getData();
}
