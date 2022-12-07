package huce.it.datnbackend.services;

import java.util.List;

public interface IFunctionService<E> {
    List<E> getAll();

    E getObjectById(int id);

    int insertObject(E e);

    int updateObject(E e);

    int deleteObject(int id);
}
