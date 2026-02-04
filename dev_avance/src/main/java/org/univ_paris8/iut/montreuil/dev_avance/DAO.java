package org.univ_paris8.iut.montreuil.dev_avance;

import java.util.List;

public abstract class DAO<T> {
    public abstract boolean create(T obj);
    public abstract boolean delete(int id);
    public abstract boolean update(T obj);
    public abstract T find(int id);
    public abstract List<T> findAll();
}