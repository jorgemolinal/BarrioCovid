package es.upm.dit.isst.barriocovid.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import es.upm.dit.isst.barriocovid.model.Pedido;
import es.upm.dit.isst.barriocovid.model.Usuario;

public interface PedidoRepository extends CrudRepository<Pedido,Integer> {

    List<Pedido> findByUsuario(Usuario usuario);
    List<Pedido> findByVoluntario(Usuario usuario);

    @Query(value = "SELECT *  FROM usuarios  WHERE id = ( SELECT voluntario_id FROM pedidos WHERE voluntario_id IS NOT NULL GROUP BY voluntario_id ORDER BY COUNT(*) ASC LIMIT 1 )  ORDER BY id LIMIT 1",nativeQuery = true)
    Optional<Usuario> findByLeastOrders();

    
}
