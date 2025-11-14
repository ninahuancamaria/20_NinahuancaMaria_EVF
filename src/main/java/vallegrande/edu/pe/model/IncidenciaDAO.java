package vallegrande.edu.pe.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IncidenciaDAO {

    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    public boolean registrar(Incidencia i) {
        String sql = "INSERT INTO incidencia(tipo, aula, fecha, estado, descripcion) VALUES(?,?,?,?,?)";
        try {
            con = Conexion.conectar();
            ps = con.prepareStatement(sql);
            ps.setString(1, i.getTipo());
            ps.setString(2, i.getAula());
            ps.setString(3, i.getFecha());
            ps.setString(4, i.getEstado());
            ps.setString(5, i.getDescripcion());
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public List<Incidencia> listar() {
        List<Incidencia> lista = new ArrayList<>();
        String sql = "SELECT * FROM incidencia";
        try {
            con = Conexion.conectar();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new Incidencia(
                        rs.getInt("id"),
                        rs.getString("tipo"),
                        rs.getString("aula"),
                        rs.getString("fecha"),
                        rs.getString("estado"),
                        rs.getString("descripcion")
                ));
            }
        } catch (Exception e) {
        }
        return lista;
    }

    public boolean actualizar(Incidencia i) {
        String sql = "UPDATE incidencia SET tipo=?, aula=?, fecha=?, estado=?, descripcion=? WHERE id=?";
        try {
            con = Conexion.conectar();
            ps = con.prepareStatement(sql);
            ps.setString(1, i.getTipo());
            ps.setString(2, i.getAula());
            ps.setString(3, i.getFecha());
            ps.setString(4, i.getEstado());
            ps.setString(5, i.getDescripcion());
            ps.setInt(6, i.getId());
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM incidencia WHERE id=?";
        try {
            con = Conexion.conectar();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public List<Incidencia> buscar(String filtro) {
        List<Incidencia> lista = new ArrayList<>();
        String sql = "SELECT * FROM incidencia WHERE tipo LIKE ? OR aula LIKE ? OR estado LIKE ?";
        try {
            con = Conexion.conectar();
            ps = con.prepareStatement(sql);
            ps.setString(1, "%" + filtro + "%");
            ps.setString(2, "%" + filtro + "%");
            ps.setString(3, "%" + filtro + "%");
            rs = ps.executeQuery();

            while (rs.next()) {
                lista.add(new Incidencia(
                        rs.getInt("id"),
                        rs.getString("tipo"),
                        rs.getString("aula"),
                        rs.getString("fecha"),
                        rs.getString("estado"),
                        rs.getString("descripcion")
                ));
            }
        } catch (Exception e) {
        }
        return lista;
    }
}
