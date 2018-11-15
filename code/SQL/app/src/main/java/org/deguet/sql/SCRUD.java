package org.deguet.sql;

import java.util.List;

/**
 * Created by joris on 15-03-20.
 */
public interface SCRUD<T> {

    // List<T> search(String s);

    long save(T o);

    List<Long> saveMany(List<T> o);

    T getById(Long p);

    List<T> getAll();

    void deleteOne(Long o);

    void deleteAll();
}
