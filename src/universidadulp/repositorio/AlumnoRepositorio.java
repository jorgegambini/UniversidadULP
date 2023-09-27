package universidadulp.repositorio;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import static universidadulp.connection.DatabaseConnection.getConnection;
import universidadulp.entidades.Alumno;

public class AlumnoRepositorio implements Repositorio<Alumno> {

    @Override
    public List<Alumno> buscarTodos() {
        
        List<Alumno> alumnos = new ArrayList<>();
        
        try (Statement stmt = getConnection().createStatement(); ResultSet rs = stmt.executeQuery("SELECT * FROM alumno ORDER BY apellido, nombre")) {
            
            while (rs.next()) {
                
                Alumno alu = crearAlumno(rs);
                alumnos.add(alu);
                
            }
            
        } catch (SQLException ex) {
            
            ex.printStackTrace();
            
        }
        
        return alumnos;
    }
    
    public List<Alumno> buscarTodosActivos() {
        
        List<Alumno> alumnos = new ArrayList<>();
        
        try (Statement stmt = getConnection().createStatement(); ResultSet rs = stmt.executeQuery("SELECT * FROM alumno WHERE estado = 1 ORDER BY apellido, nombre")) {
            
            while (rs.next()) {
                
                Alumno alu = crearAlumno(rs);
                alumnos.add(alu);
                
            }
            
        } catch (SQLException ex) {
            
            ex.printStackTrace();
            
        }
        
        return alumnos;
    }


    @Override
    public Alumno buscarPorId(int id) {
        
        Alumno alu = null;
        
        try (PreparedStatement stmt = getConnection().prepareStatement("SELECT * FROM alumno WHERE idAlumno = ? ORDER BY apellido, nombre")) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                
                if (rs.next()) {
                    
                    alu = crearAlumno(rs);
                    
                }
                
            }
        
        } catch (SQLException ex) {
            
            ex.printStackTrace();
            
        }
        
        return alu;
        
    }

    @Override
    public Alumno guardar(Alumno alumno) {
        
        String sql;
        
        if (alumno.getIdAlumno() > 0) {
            
            sql = "UPDATE alumno SET dni = ?, apellido = ?, nombre = ?, fechaNacimiento = ?, estado = ? WHERE idAlumno = ?";
            
        } else {
            
            sql = "INSERT INTO alumno (dni, apellido, nombre, fechaNacimiento, estado) VALUES(?,?,?,?,?)";
            
        }
        
        try (PreparedStatement stmt = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, alumno.getDni());
            stmt.setString(2, alumno.getApellido());
            stmt.setString(3, alumno.getNombre());
            stmt.setDate(4, Date.valueOf(alumno.getFechaNac()));
            stmt.setBoolean(5, alumno.isActivo());
            
            if (alumno.getIdAlumno() > 0) {
                
                stmt.setInt(6, alumno.getIdAlumno());
                stmt.executeUpdate();
                
            } else {
                
                stmt.executeUpdate();
                
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    
                    if (rs.next()) {
                        
                        alumno.setIdAlumno(rs.getInt(1));
                    
                    }
                    
                }
                
            }
            
            return alumno;
            
        } catch (SQLException ex) {
            
            ex.printStackTrace();
            return null;
            
        }
        
        //return alumno;
        
    }

    @Override
    public int eliminarPorId(int id) {
        
        try (PreparedStatement stmt = getConnection().prepareStatement("DELETE FROM alumno WHERE idAlumno = ?")) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate();
            
        } catch (SQLException ex) {
            
            ex.printStackTrace();
            
        }
        
        return 0;
        
    }
    
    public Alumno buscarPorDNI(int dni) {
        
        Alumno alu = null;
        
        try (PreparedStatement stmt = getConnection().prepareStatement("SELECT * FROM alumno WHERE dni = ?")) {
            
            stmt.setInt(1, dni);
            
            try (ResultSet rs = stmt.executeQuery()) {
                
                if (rs.next()) {
                    
                    alu = crearAlumno(rs);
                    
                }
                
            }
            
        } catch (SQLException ex) {
            
            ex.printStackTrace();
            
        }
        
        return alu;
        
    }
    
    public int eliminarPorDNI(int dni) {
        
        try (PreparedStatement stmt = getConnection().prepareStatement("UPDATE alumno SET estado = 0 WHERE dni = ?")) {
            
            stmt.setInt(1, dni);
            return stmt.executeUpdate();
            
        } catch (SQLException ex) {
            
            ex.printStackTrace();
            
        }
        
        return 0;
        
    }
    
    public Alumno crearAlumno(ResultSet rs) throws SQLException {
        
        Alumno alu = new Alumno();

        alu.setIdAlumno(rs.getInt("idAlumno"));
        alu.setDni(rs.getInt("dni"));
        alu.setApellido(rs.getString("apellido"));
        alu.setNombre(rs.getString("nombre"));
        alu.setFechaNac(rs.getDate("fechaNacimiento").toLocalDate());
        alu.setActivo(rs.getBoolean("estado"));

        return alu;
        
    }
    
}
