package es.upm.dit.isst.barriocovid.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import es.upm.dit.isst.barriocovid.model.Producto;
import es.upm.dit.isst.barriocovid.model.Usuario;

public interface ProductoRepository extends CrudRepository<Producto,Integer> {
   
    List <Producto> findByUsuarioAndIsDeletedIsFalse(Usuario usuario);

    Optional <Producto> findById(Integer id);
}
