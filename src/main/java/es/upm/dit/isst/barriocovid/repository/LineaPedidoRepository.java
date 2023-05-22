package es.upm.dit.isst.barriocovid.repository;

import java.util.List;
import java.util.Optional;

import es.upm.dit.isst.barriocovid.model.Usuario;
import org.springframework.data.repository.CrudRepository;

import es.upm.dit.isst.barriocovid.model.LineaPedido;
import es.upm.dit.isst.barriocovid.model.Producto;

public interface LineaPedidoRepository extends CrudRepository<LineaPedido,Integer> {

    Optional <LineaPedido> findByProducto(Producto producto);
    List<LineaPedido> findByUsuarioAndProcessedIsFalse(Usuario usuario);
    
}
