package org.example;

import java.sql.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Main {
    private static final Connection con;

    static {
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/tarea5", "root", "toor");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        System.out.println("Apellidos de los empleados sin repeticiones: ");
        obtenerApellidos().forEach(System.out::println);
        System.out.println();

        System.out.println("Datos de empleados que trabajan para el departamento 37 y 77: ");
        obtenerDatosEmpleado(37, 77).forEach(System.out::println);
        System.out.println();

        System.out.println("Número de empleados en cada departamento: ");
        Map<Integer, Integer> map = obtenerEmpleadosDepartamentos();
        for (int departamento : map.keySet()) {
            System.out.println("El departamento " + departamento + " tiene " + map.get(departamento) + " empleados");
        }
        System.out.println();

        System.out.println("Nombres y apellidos de los empleados que trabajen en departamentos cuyo presupuesto sea mayor de 60.000€: ");
        for (Empleado empleado : empleadosDepartamentosPresupuestoMayorQue(60000)) {
            System.out.println("Empleado, nombre: " + empleado.getNombre() + " | apellidos: " + empleado.getApellidos());
        }
        System.out.println();

        System.out.println("Datos de los departamentos cuyo presupuesto es superior al presupuesto medio de todos los departamentos: ");
        departamentosPresupuestoSuperiorMedio().forEach(System.out::println);
        System.out.println();

        System.out.println("Añadir departamento y empleado vinculado: ");
        addDepartamento(11, "Calidad", 40000);
        addEmpleado(89267109, "Esther", "Vázquez", 11);
        System.out.println();
    }

    private static void addDepartamento(int codigo, String nombre, int presupuesto) {
        String sql = "insert into departamentos values (?,?,?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, codigo);
            ps.setString(2, nombre);
            ps.setInt(3, presupuesto);

            int ins = ps.executeUpdate();
            System.out.println("Se han insertado " + ins + " valores en la tabla Departamentos");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void addEmpleado(int dni, String nombre, String apellidos, int departamento) {
        String sql = "insert into empleados values (?,?,?,?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, dni);
            ps.setString(2, nombre);
            ps.setString(3, apellidos);
            ps.setInt(4, departamento);

            int ins = ps.executeUpdate();
            System.out.println("Se han insertado " + ins + " valores en la tabla Empleados");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static Set<Departamento> departamentosPresupuestoSuperiorMedio() {
        Set<Departamento> departamentos = new HashSet<>();
        String sql = "select * from departamentos where Presupuesto > (select avg(Presupuesto) from departamentos)";
        try (ResultSet rs = con.createStatement().executeQuery(sql)) {
            while (rs.next()) {
                int codigo = rs.getInt("Codigo");
                String nombre = rs.getString("Nombre");
                int presupuesto = rs.getInt("Presupuesto");

                Departamento departamento = new Departamento(codigo, nombre, presupuesto);
                departamentos.add(departamento);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return departamentos;
    }

    private static Set<Empleado> empleadosDepartamentosPresupuestoMayorQue(int presupuesto) {
        Set<Empleado> empleados = new HashSet<>();
        String sql = "select * from empleados where Departamento in (select Codigo from departamentos where Presupuesto > ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, presupuesto);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int dni = rs.getInt("DNI");
                    String nombre = rs.getString("Nombre");
                    String apellidos = rs.getString("Apellidos");
                    int departamento = rs.getInt("Departamento");

                    Empleado empleado = new Empleado(dni, nombre, apellidos, departamento);
                    empleados.add(empleado);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return empleados;
    }

    private static Map<Integer, Integer> obtenerEmpleadosDepartamentos() {
        Map<Integer, Integer> empleadosDepartamento = new HashMap<>();
        try (Statement statement = con.createStatement(); ResultSet rs = statement.executeQuery("select Departamento, count(*) from empleados group by Departamento")) {
            while (rs.next()) {
                int departamento_id = rs.getInt(1);
                int empleados = rs.getInt(2);
                empleadosDepartamento.putIfAbsent(departamento_id, empleados);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return empleadosDepartamento;
    }

    private static Set<Empleado> obtenerDatosEmpleado(int... departamentos) {
        Set<Empleado> empleados = new HashSet<>();

        StringBuilder sql = new StringBuilder("select * from empleados where Departamento in (");
        for (int i = 0; i < departamentos.length; i++) {
            sql.append("?");
            if (i < (departamentos.length - 1)) sql.append(",");
        }
        sql.append(")");

        try (PreparedStatement ps = con.prepareStatement(sql.toString())) {
            for (int i = 0; i < departamentos.length; i++) {
                ps.setInt((i + 1), departamentos[i]);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Empleado empleado = new Empleado(rs.getInt("DNI"), rs.getString("Nombre"), rs.getString("Apellidos"), rs.getInt("Departamento"));
                    empleados.add(empleado);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return empleados;
    }

    private static Set<String> obtenerApellidos() {
        Set<String> apellidos = new HashSet<>();
        try (Statement statement = con.createStatement(); ResultSet rs = statement.executeQuery("select distinct Apellidos from empleados");) {
            while (rs.next()) {
                apellidos.add(rs.getString(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return apellidos;
    }
}