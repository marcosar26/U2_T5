package org.example;

public class Departamento {
    private int codigo;
    private String nombre;
    private int presupuesto;

    public Departamento(int codigo, String nombre, int presupuesto) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.presupuesto = presupuesto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Departamento that = (Departamento) o;

        return codigo == that.codigo;
    }

    @Override
    public int hashCode() {
        return codigo;
    }

    @Override
    public String toString() {
        return "Departamento{" + "codigo=" + codigo + ", nombre='" + nombre + '\'' + ", presupuesto=" + presupuesto + '}';
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getPresupuesto() {
        return presupuesto;
    }

    public void setPresupuesto(int presupuesto) {
        this.presupuesto = presupuesto;
    }
}
