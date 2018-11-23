package net.readybid.web;

public class NullViewModel implements RbViewModel {

    @Override
    public Long getCount() {
        return null;
    }

    @Override
    public Object getData() {
        return null;
    }
}
