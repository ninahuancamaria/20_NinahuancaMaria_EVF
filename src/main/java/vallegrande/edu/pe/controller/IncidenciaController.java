package vallegrande.edu.pe.controller;

import vallegrande.edu.pe.model.*;
import java.util.List;

public class IncidenciaController {

    IncidenciaDAO dao = new IncidenciaDAO();

    public boolean registrar(Incidencia i) { return dao.registrar(i); }

    public List<Incidencia> listar() { return dao.listar(); }

    public boolean actualizar(Incidencia i) { return dao.actualizar(i); }

    public boolean eliminar(int id) { return dao.eliminar(id); }

    public List<Incidencia> buscar(String filtro) { return dao.buscar(filtro); }
}
