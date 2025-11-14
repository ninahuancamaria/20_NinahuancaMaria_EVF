
package vallegrande.edu.pe.model;

public class Incidencia {
    private int id;
    private String tipo;
    private String aula;
    private String fecha;
    private String estado;
    private String descripcion;

    public Incidencia() {}

    public Incidencia(int id, String tipo, String aula, String fecha, String estado, String descripcion) {
        this.id = id;
        this.tipo = tipo;
        this.aula = aula;
        this.fecha = fecha;
        this.estado = estado;
        this.descripcion = descripcion;
    }

    // Getters y setters (encapsulamiento)
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getAula() { return aula; }
    public void setAula(String aula) { this.aula = aula; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}
