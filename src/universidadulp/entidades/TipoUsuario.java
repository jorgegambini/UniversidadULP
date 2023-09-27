
package universidadulp.entidades;


public class TipoUsuario {
    
    private int idTipoUsuario;
    private String descripcion;
    private boolean admin;
    private boolean crud;
    private boolean queries;

    public TipoUsuario(int idTipoUsuario, String descripcion, boolean admin, boolean crud, boolean queries) {
        this.idTipoUsuario = idTipoUsuario;
        this.descripcion = descripcion;
        this.admin = admin;
        this.crud = crud;
        this.queries = queries;
    }

    public TipoUsuario(String descripcion, boolean admin, boolean crud, boolean queries) {
        this.descripcion = descripcion;
        this.admin = admin;
        this.crud = crud;
        this.queries = queries;
    }

    public TipoUsuario() {
    }

    public int getIdTipoUsuario() {
        return idTipoUsuario;
    }

    public void setIdTipoUsuario(int idTipoUsuario) {
        this.idTipoUsuario = idTipoUsuario;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isCrud() {
        return crud;
    }

    public void setCrud(boolean crud) {
        this.crud = crud;
    }

    public boolean isQueries() {
        return queries;
    }

    public void setQueries(boolean queries) {
        this.queries = queries;
    }

    @Override
    public String toString() {
        return descripcion;
    }
    
    public Object[] toTableRow(int rowNum) {

        return new Object[]{getIdTipoUsuario(), getDescripcion(), isAdmin(), isCrud(), this, isQueries()};

    }
    
}
