package universidadulp.repositorio;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import static universidadulp.connection.DatabaseConnection.getConnection;
import universidadulp.entidades.Inscripcion;


public class InscripcionRepositorio implements Repositorio<Inscripcion> {

    @Override
    public List<Inscripcion> buscarTodos() {
        
        List<Inscripcion> inscripciones = new ArrayList<>();
        
        try (Statement stmt = getConnection().createStatement(); ResultSet rs = stmt.executeQuery("SELECT * FROM inscripcion ORDER BY idInscripto")) {
            
            while (rs.next()) {
                
                Inscripcion ins = crearInscripcion(rs);
                inscripciones.add(ins);
                
            }
            
        } catch (SQLException ex) {
            
            ex.printStackTrace();
            
        }
        
        return inscripciones;
        
    }

    public List<Inscripcion> buscarTodosxAlu(int idAlumno, boolean matInactive) {
        
        String sql = "";
        
        if(matInactive){
            
            sql = "SELECT i.* FROM inscripcion i JOIN materia m USING (idMateria) WHERE i.idAlumno = ? ORDER BY m.Anio, m.Nombre";
            
        }else{
            
            sql = "SELECT i.* FROM inscripcion i JOIN materia m USING (idMateria) WHERE m.estado = 1 AND i.idAlumno = ? ORDER BY m.Anio, m.Nombre";
            
        }
        
        List<Inscripcion> inscripciones = new ArrayList<>();
        
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            
            stmt.setInt(1, idAlumno);
            
            try (ResultSet rs = stmt.executeQuery()) {
                
                while (rs.next()) {
                    
                    Inscripcion ins = crearInscripcion(rs);
                    inscripciones.add(ins);
                    
                }
                
            }
            
        } catch (SQLException ex) {
            
            ex.printStackTrace();
            
        }
        
        return inscripciones;
        
    }
    
    public List<Inscripcion> buscarTodosxMat(int idMateria, boolean aluInactive) {
        
        String sql = "";
        
        if(aluInactive){
            
            sql = "SELECT i.* FROM inscripcion i JOIN alumno a USING (idAlumno) WHERE i.idMateria = ? ORDER BY a.apellido, a.nombre";
            
        }else{
            
             sql = "SELECT i.* FROM inscripcion i JOIN alumno a USING (idAlumno) WHERE a.estado = 1 AND i.idMateria = ? ORDER BY a.apellido, a.nombre";
            
        }
        
        List<Inscripcion> inscripciones = new ArrayList<>();
        
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            
            stmt.setInt(1, idMateria);
            
            try (ResultSet rs = stmt.executeQuery()) {
                
                while (rs.next()) {
                    
                    Inscripcion ins = crearInscripcion(rs);
                    inscripciones.add(ins);
                    
                }
                
            }
            
        } catch (SQLException ex) {
            
            ex.printStackTrace();
            
        }
        
        return inscripciones;
        
    }

    @Override
    public Inscripcion buscarPorId(int id) {
        
        Inscripcion ins = null;
        
        try (PreparedStatement stmt = getConnection().prepareStatement("SELECT * FROM inscripcion WHERE idInscripto = ?")) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                
                if (rs.next()) {
                    
                    ins = crearInscripcion(rs);
                    
                }
                
            }
            
        } catch (SQLException ex) {
            
            ex.printStackTrace();
            
        }
        
        return ins;
        
    }

    @Override
    public Inscripcion guardar(Inscripcion inscripcion) {
        
        String sql;
        
        if (inscripcion.getIdInscripcion() > 0) {
            
            sql = "UPDATE inscripcion SET nota = ?, idAlumno = ?, idMateria = ?  WHERE idInscripto = ?";
            
        } else {
            
            sql = "INSERT INTO inscripcion (nota,idAlumno,idMateria) VALUES(?,?,?)";
            
        }
        
        try (PreparedStatement stmt = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setDouble(1, inscripcion.getNota());
            stmt.setInt(2, inscripcion.getAlumno().getIdAlumno());
            stmt.setInt(3, inscripcion.getMateria().getIdMateria());

            if (inscripcion.getIdInscripcion() > 0) {
                
                stmt.setInt(4, inscripcion.getIdInscripcion());
                stmt.executeUpdate();
                
            } else {
                
                stmt.executeUpdate();
                
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    
                    if (rs.next()) {
                        
                        inscripcion.setIdInscripcion(rs.getInt(1));
                        
                    }
                    
                }
                
            }
            
            return inscripcion;
            
        } catch (SQLException ex) {
            
            ex.printStackTrace();
            
        }
        
        return inscripcion;
        
    }

    @Override
    public int eliminarPorId(int id) {
        
        try (PreparedStatement stmt = getConnection().prepareStatement("DELETE FROM inscripcion WHERE  idInscripto = ?")) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate();

        } catch (SQLException ex) {
            
            ex.printStackTrace();
            
        }
        
        return 0;
        
    }

    public int eliminarPorAluyMat(int idAlumno, int idMateria) {
        
        try (PreparedStatement stmt = getConnection().prepareStatement("DELETE FROM inscripcion WHERE idAlumno = ? AND idMateria = ?")) {
            
            stmt.setInt(1, idAlumno);
            stmt.setInt(2, idMateria);
            return stmt.executeUpdate();

        } catch (SQLException ex) {
            
            ex.printStackTrace();
            
        }
        
        return 0;
        
    }
    
    public double devolverNotaPorAluyMat(int idAlumno, int idMateria) {
        
        try (PreparedStatement stmt = getConnection().prepareStatement("Select Nota FROM inscripcion WHERE idAlumno = ? AND idMateria = ?")) {
            
            stmt.setInt(1, idAlumno);
            stmt.setInt(2, idMateria);
            
            try (ResultSet rs = stmt.executeQuery()) {
                
                if (rs.next()) {
                    
                    return rs.getDouble("nota");
                    
                }
                
            }
           
        } catch (SQLException ex) {
            
            ex.printStackTrace();
            
        }
        
        return -1;
        
    }
    
    public boolean devolverNotaPorAlu(int dni) {
       
        try (PreparedStatement stmt = getConnection().prepareStatement("Select i.Nota FROM inscripcion i JOIN materia m USING(idMateria) JOIN alumno a USING (idAlumno) WHERE i.Nota > 0 AND m.estado = 1 AND a.dni = ?")) {
            
            stmt.setInt(1, dni);
            
            try (ResultSet rs = stmt.executeQuery()) {
                
                if (rs.next()) {
                    
                    return true;
                    
                }
                
            }
           
        } catch (SQLException ex) {
            
            ex.printStackTrace();
            
        }
        
        return false;
        
    }
    
    public boolean devolverNotaPorMat(int idMateria) {
       
        try (PreparedStatement stmt = getConnection().prepareStatement("Select i.Nota FROM inscripcion i JOIN materia m USING(idMateria) JOIN alumno a USING (idAlumno) WHERE i.Nota > 0 AND a.estado = 1 AND m.idMateria = ?")) {
            
            stmt.setInt(1, idMateria);
            
            try (ResultSet rs = stmt.executeQuery()) {
                
                if (rs.next()) {
                    
                    return true;
                    
                }
                
            }
           
        } catch (SQLException ex) {
            
            ex.printStackTrace();
            
        }
        
        return false;
        
    }

    private Inscripcion crearInscripcion(ResultSet rs) throws SQLException {

        Inscripcion ins = new Inscripcion();
        AlumnoRepositorio aluRep = new AlumnoRepositorio();
        MateriaRepositorio matRep = new MateriaRepositorio();

        ins.setIdInscripcion(rs.getInt("idInscripto"));
        ins.setNota(rs.getDouble("nota"));
        ins.setAlumno(aluRep.buscarPorId(rs.getInt("idAlumno")));
        ins.setMateria(matRep.buscarPorId(rs.getInt("idMateria")));

        return ins;

    }

}
