package universidadulp.repositorio;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import static universidadulp.connection.DatabaseConnection.getConnection;
import universidadulp.entidades.Materia;


public class MateriaRepositorio implements Repositorio<Materia> {

    @Override
    public List<Materia> buscarTodos() {
        
        List<Materia> materias = new ArrayList<>();
        
        try (Statement stmt = getConnection().createStatement(); ResultSet rs = stmt.executeQuery("SELECT * FROM materia ORDER BY nombre")) {
            
            while (rs.next()) {
                
                Materia mat = crearMateria(rs);
                materias.add(mat);
                
            }
            
        } catch (SQLException ex) {
            
            ex.printStackTrace();
            
        }
        
        return materias;
        
    }
    
    public List<Materia> buscarTodosActivos() {
        
        List<Materia> materias = new ArrayList<>();
        
        try (Statement stmt = getConnection().createStatement(); ResultSet rs = stmt.executeQuery("SELECT * FROM materia WHERE estado = 1 ORDER BY nombre")) {
            
            while (rs.next()) {
                
                Materia mat = crearMateria(rs);
                materias.add(mat);
                
            }
            
        } catch (SQLException ex) {
            
            ex.printStackTrace();
            
        }
        
        return materias;
        
    }

    @Override
    public Materia buscarPorId(int id) {
        
        Materia mat = null;
        
        try (PreparedStatement stmt = getConnection().prepareStatement("SELECT * FROM materia WHERE idMateria = ?  ORDER BY nombre")) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                
                if (rs.next()) {
                    
                    mat = crearMateria(rs);
                    
                }
                
            }
            
        } catch (SQLException ex) {
            
            ex.printStackTrace();
            
        }
        
        return mat;
        
    }

    @Override
    public Materia guardar(Materia materia) {
        
        String sql;
        
        if (materia.getIdMateria() > 0) {
            
            sql = "UPDATE materia SET nombre = ?, anio = ?, estado = ? WHERE idMateria = ?";
            
        } else {
            
            sql = "INSERT INTO materia (nombre,anio,estado) VALUES(?,?,?)";
            
        }
        
        try (PreparedStatement stmt = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, materia.getNombre());
            stmt.setInt(2, materia.getAnioMateria());
            stmt.setBoolean(3, materia.isActivo());
            
            if (materia.getIdMateria() > 0) {
                
                stmt.setInt(4, materia.getIdMateria());
                stmt.executeUpdate();
                
            } else {
                
                stmt.executeUpdate();
                
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    
                    if (rs.next()) {
                        
                        materia.setIdMateria(rs.getInt(1));
                        
                    }
                    
                }
                
            }
            
            return materia;
            
        } catch (SQLException ex) {
            
            ex.printStackTrace();
            
        }
        
        return materia;
    }

    @Override
    public int eliminarPorId(int id) {
//        try (PreparedStatement stmt = getConnection().prepareStatement("DELETE FROM materia WHERE idMateria = ?")) {

//            stmt.setInt(1, id);
//            return stmt.executeUpdate();
//
//        } catch (SQLException ex) {

//            ex.printStackTrace();

//        }

//        return 0;

        try (PreparedStatement stmt = getConnection().prepareStatement("UPDATE materia SET estado = 0 WHERE idMateria = ?")) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate();
            
        } catch (SQLException ex) {
            
            ex.printStackTrace();
            
        }
        
        return 0;
        
    }

    public List<Materia> buscarMateriasConWhere(String where) {
        
        List<Materia> materias = new ArrayList<>();
        
        try (Statement stmt = getConnection().createStatement(); ResultSet rs = stmt.executeQuery("SELECT m.* FROM materia m " + where)) {
            
            while (rs.next()) {
                
                Materia mat = crearMateria(rs);
                materias.add(mat);
                
            }
            
        } catch (SQLException ex) {
            
            ex.printStackTrace();
            
        }
        
        return materias;
        
    }
    
    private Materia crearMateria(ResultSet rs) throws SQLException {
        
        Materia mat = new Materia();

        mat.setIdMateria(rs.getInt("idMateria"));
        mat.setNombre(rs.getString("nombre"));
        mat.setAnioMateria(rs.getInt("anio"));
        mat.setActivo(rs.getBoolean("estado"));
        return mat;
        
    }
    
}
