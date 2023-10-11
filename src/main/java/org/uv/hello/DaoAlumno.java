/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package org.uv.hello;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import org.uv.crudalumnos.Alumno;
import org.uv.crudalumnos.ConexionDB;
import org.uv.crudalumnos.TransactionDB;

/**
 *
 * @author zarcorp
 */
@Named(value = "daoAlumno")
@ViewScoped
public class DaoAlumno implements Serializable {

    private Alumno alumno = new Alumno();
    private List<Alumno> alumnosL = findAll();
    private Alumno selectedAlumno;

    public Alumno getSelectedAlumno() {
        return selectedAlumno;
    }

    public void setSelectedAlumno(Alumno selectedAlumno) {
        this.selectedAlumno = selectedAlumno;
    }
   

    public List<Alumno> getAlumnosL() {
        return alumnosL;
    }

    public void setAlumnosL(List<Alumno> alumnos) {
        this.alumnosL = alumnos;
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }

    public String create() {
        Alumno p = this.alumno;
        ConexionDB cx = ConexionDB.getInstance();
        TransactionDB tbd = new TransactionDB<Alumno>(p) {
            @Override
            public boolean execute(Connection con) {
                try ( PreparedStatement psm = con.prepareStatement("INSERT INTO pejemplo(clave, nombre, direccion, telefono) VALUES (?, ?, ?, ?)")) {
                    psm.setInt(1, p.getClave());
                    psm.setString(2, p.getNombre());
                    psm.setString(3, p.getDireccion());
                    psm.setString(4, p.getTelefono());
                    psm.execute();
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Guardado Correctamente"));
                    alumno.setClave(0);
                    alumno.setNombre(null);
                    alumno.setDireccion(null);
                    alumno.setTelefono(null);
                    return true;
                } catch (SQLException ex) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Aviso", "No se pudo guardar"));
                    return false;
                }
            }
        };
        cx.execute(tbd);

        String viewId = FacesContext.getCurrentInstance().getViewRoot().getViewId();
        return viewId + "?faces-redirect=true";
    }
    
    public List<Alumno> findAll() {
        final List<Alumno> alumnos = new ArrayList<>();
        ConexionDB cx = ConexionDB.getInstance();
        TransactionDB tbd = new TransactionDB<List<Alumno>>(alumnos) {
            @Override
            public boolean execute(Connection con) {
                try ( PreparedStatement psm = con.prepareStatement("SELECT * FROM pejemplo");  ResultSet rs = psm.executeQuery()) {

                    while (rs.next()) {
                        Alumno alumno = new Alumno();
                        alumno.setClave(rs.getInt("clave"));
                        alumno.setNombre(rs.getString("nombre"));
                        alumno.setDireccion(rs.getString("direccion"));
                        alumno.setTelefono(rs.getString("telefono"));
                        alumnos.add(alumno);
                    }
                    return true;
                } catch (SQLException ex) {
                    return false;
                } catch (NullPointerException ex) {
                    return false;
                }
            }
        };
        boolean resp = cx.execute(tbd);
        if (resp) {
            return alumnos;
        } else {
            return null;
        }
    }

}
