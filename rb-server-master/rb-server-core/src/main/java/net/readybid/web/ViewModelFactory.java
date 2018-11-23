package net.readybid.web;


import net.readybid.app.core.transaction.exceptions.NotFoundException;
import net.readybid.utils.ListResult;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by DejanK on 1/13/2017.
 *
 */
public interface ViewModelFactory<T, S extends ViewModel<T>> {

    S create(T t);

    default S createView(T t){
        if(t == null) {
            throw new NotFoundException();
        } else {
            return create(t);
        }
    }

    default List<S> createList(List<? extends T> tList){
        return tList == null ? null : tList.stream().map(this::createAsPartial).collect(Collectors.toList());
    }

    default S createAsPartial(T t){
        return t == null ? null : create(t);
    }

    default ListResult<S> createListResult(ListResult<? extends T> tListResult) {
        return new ListResult<S>(createList(tListResult.data), tListResult.total);
    }
}
