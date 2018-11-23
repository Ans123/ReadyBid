package net.readybid.web;

import java.util.List;

public class RbListViewModel<T> implements RbViewModel<List<T>> {

    private final List<T> list;

    public RbListViewModel(List<T> list){
        this.list = list;
    }

    public Long getCount(){
        return (long) list.size();
    }

    public List<T> getData(){
        return list;
    }
}
