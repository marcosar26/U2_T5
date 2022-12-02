package org.example;

public class Empleado {
    private int dni;
    private String nombre;
    private String apellidos;
    private int departamento;

    public Empleado(int dni, String nombre, String apellidos, int departamento) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.departamento = departamento;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Empleado empleado = (Empleado) o;

        return dni == empleado.dni;
    }

    @Override
    public int hashCode() {
        return dni;
    }

    @Override
    public String toString() {
        return "Empleado{" + "dni=" + dni + ", nombre='" + nombre + '\'' + ", apellidos='" + apellidos + '\'' + ", departamento=" + departamento + '}';
    }

    public int getDni() {
        return dni;
    }

    public void setDni(int dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public int getDepartamento() {
        return departamento;
    }

    public void setDepartamento(int departamento) {
        this.departamento = departamento;
    }
}
