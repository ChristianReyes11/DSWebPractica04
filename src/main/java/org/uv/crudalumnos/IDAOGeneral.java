/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.uv.crudalumnos;

import java.util.List;

/**
 *
 * @author zarcorp
 */
public interface IDAOGeneral<T, ID>{
    public T create(T p);
    public boolean delete(ID clave);
    public T update(ID clave, T p);
    
    public List<T> findAll();
    public T findById(ID clave);
}
