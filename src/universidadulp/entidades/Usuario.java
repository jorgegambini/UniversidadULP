package universidadulp.entidades;


public class Usuario {

    private int idUsuario;
    private String nombre;
    private String apellido;
    private boolean genero;
    private TipoUsuario tipoUsuario;
    private String usuario;
    private String password;
    private boolean remember;
    private boolean activo;

    public Usuario(int idUsuario, String nombre, String apellido, boolean genero, TipoUsuario tipoUsuario, String usuario, String password, boolean remember, boolean activo) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.apellido = apellido;
        this.genero = genero;
        this.tipoUsuario = tipoUsuario;
        this.usuario = usuario;
        this.password = password;
        this.remember = remember;
        this.activo = activo;
    }

    public Usuario(String nombre, String apellido, boolean genero, TipoUsuario tipoUsuario, String usuario, String password, boolean remember, boolean activo) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.genero = genero;
        this.tipoUsuario = tipoUsuario;
        this.usuario = usuario;
        this.password = password;
        this.remember = remember;
        this.activo = activo;
    }

    public Usuario() {
    }
    
    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public boolean isGenero() {
        return genero;
    }

    public void setGenero(boolean genero) {
        this.genero = genero;
    }
    
    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isRemember() {
        return remember;
    }

    public void setRemember(boolean remember) {
        this.remember = remember;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
    
    @Override
    public String toString() {
        return usuario;
    }

     public Object[] toTableRow(int rowNum) {

        return new Object[]{getIdUsuario(), getUsuario(), getPassword(), isRemember(), isActivo()};

    }

}
