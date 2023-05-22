package es.upm.dit.isst.barriocovid.repository;

import es.upm.dit.isst.barriocovid.model.LineaPedido;
import es.upm.dit.isst.barriocovid.model.Producto;
import es.upm.dit.isst.barriocovid.model.Role;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RoleRepository extends CrudRepository<Role,Integer> {

    Optional<Role> findByName(String name);

}
