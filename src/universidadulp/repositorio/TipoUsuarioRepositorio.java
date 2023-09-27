
package universidadulp.repositorio;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import static universidadulp.connection.DatabaseConnection.getConnection;
import universidadulp.entidades.TipoUsuario;


public class TipoUsuarioRepositorio {
    
    public List<TipoUsuario> buscarTodos() {
        
        List<TipoUsuario> tipos = new ArrayList<>();
        
        try (Statement stmt = getConnection().createStatement(); ResultSet rs = stmt.executeQuery("SELECT * FROM tipousuario ORDER BY descripcion")) {
            
            while (rs.next()) {
                
                TipoUsuario tipo = crearTipoUsuario(rs);
                tipos.add(tipo);
                
            }
            
            
        } catch (SQLException ex) {
            
             ex.getMessage();
            
        }finally{
            
            return tipos;
            
        }
       
    }
    
    public TipoUsuario buscarPorId(int id) {
        
        TipoUsuario tu = null;
        
        try (PreparedStatement stmt = getConnection().prepareStatement("SELECT * FROM tipousuario WHERE idtipousuario = ?")) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                
                if (rs.next()) {
                    
                    tu = crearTipoUsuario(rs);
                    
                }
                
            }
           
        } catch (SQLException ex) {
            
            ex.getMessage();
           
        }finally{
            
            return tu;
            
        }
       
    }
    
    public String buscarAcceso(int id) {
        
        String result = null;
        
        try (PreparedStatement stmt = getConnection().prepareStatement("SELECT * FROM tipousuario WHERE idtipousuario = ?")) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                
                if (rs.next()) {
                    
                    if(rs.getBoolean("admin")){
                        
                        result = "ADMIN";
                        
                    }else if(rs.getBoolean("crud")){
                        
                        result = "CRUD";
                        
                    }else{
                        
                        result = "QUERIES";
                        
                    }
                    
                }
                
            }
           
        } catch (SQLException ex) {
            
            ex.getMessage();
           
        }finally{
            
            return result;
            
        }
       
    }
    
    public TipoUsuario guardar(TipoUsuario tipo) {
        
        String sql;
        
        if (tipo.getIdTipoUsuario() > 0) {
            
            sql = "UPDATE tipousuario SET descripcion = ?, admin = ?, crud = ?, queries = ? WHERE idtipousuario = ?";
            
        } else {
            
            sql = "INSERT INTO tipousuario (descripcion, admin, crud, queries) VALUES(?,?,?,?)";
            
        }
        
        try (PreparedStatement stmt = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
           
            stmt.setString(1, tipo.getDescripcion());
            stmt.setBoolean(2, tipo.isAdmin());
            stmt.setBoolean(3, tipo.isCrud());
            stmt.setBoolean(4, tipo.isQueries());
            
            
            if (tipo.getIdTipoUsuario() > 0) {
                
                stmt.setInt(5, tipo.getIdTipoUsuario());
                stmt.executeUpdate();
                
            } else {
                
                stmt.executeUpdate();
                
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    
                    if (rs.next()) {
                        
                        tipo.setIdTipoUsuario(rs.getInt(1));
                    
                    }
                    
                }
                
            }
           
        } catch (SQLException ex) {
            
             ex.getMessage();
            tipo = null;
            
        }finally{
            
            return tipo;
            
        }
       
    }
    
    public TipoUsuario crearTipoUsuario(ResultSet rs) throws SQLException {
        
        TipoUsuario tipo = new TipoUsuario();

        tipo.setIdTipoUsuario(rs.getInt("idtipousuario"));
        tipo.setDescripcion(rs.getString("descripcion"));
        tipo.setAdmin(rs.getBoolean("admin"));
        tipo.setCrud(rs.getBoolean("crud"));
        tipo.setQueries(rs.getBoolean("queries"));

        return tipo;
        
    }
    
}
