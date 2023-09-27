
package universidadulp.repositorio;

import java.util.List;

public  interface Repositorio <T>{
    
    List<T> buscarTodos();
    
   T buscarPorId(int id);
    
    T guardar(T t);
    
    int eliminarPorId(int id);
        
}
