package es.upm.dit.isst.barriocovid;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.assertFalse;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import es.upm.dit.isst.barriocovid.model.Producto;
import es.upm.dit.isst.barriocovid.model.Role;
import es.upm.dit.isst.barriocovid.model.Usuario;
import es.upm.dit.isst.barriocovid.repository.ProductoRepository;
import es.upm.dit.isst.barriocovid.repository.RoleRepository;
import es.upm.dit.isst.barriocovid.repository.UsuarioRepository;

@SpringBootTest
class BarriocovidApplicationTests {

	@Autowired
	private UsuarioRepository usuarioRepo;

	@Autowired
	private ProductoRepository productoRepo;

	@Autowired
	private RoleRepository roleRepo;


	@Test
	final void UsuarioTest(){

		Usuario usuario = new Usuario();
		usuario.setId(6);
		usuario.setUsername("Prueba1");
		usuario.setPassword("1234");
		usuario.setNombre("Prueba");
		usuario.setEmail("prueba@gmail");
		usuario.setDireccion("Prueba Unitaria");
		usuario.setVulnerable(false);
		usuario.setTipo("COMPRADOR");

		usuarioRepo.save(usuario);

		Optional<Usuario>usuario2 = usuarioRepo.findById(6);

		assertEquals(usuario2.get().getId(), usuario.getId());
		assertEquals(usuario2.get().getUsername(), usuario.getUsername());
		assertEquals(usuario2.get().getPassword(), usuario.getPassword());
		assertEquals(usuario2.get().getNombre(), usuario.getNombre());
		assertEquals(usuario2.get().getDireccion(), usuario.getDireccion());
		assertEquals(usuario2.get().isVulnerable(), usuario.isVulnerable());
		assertEquals(usuario2.get().getTipo(), usuario.getTipo());

		usuario.setNombre("Prueba2");
		usuarioRepo.save(usuario);
		usuario2 = usuarioRepo.findById(6);
		assertNotEquals(usuario2.get().getNombre(), "Prueba1");

		usuarioRepo.delete(usuario);
		usuario2 = usuarioRepo.findById(6);
		assertFalse(usuario2.isPresent());
	}

	@Test
	final void ProductoTest(){

		Usuario usuario = new Usuario();
		usuarioRepo.save(usuario);

		Producto producto = new Producto();
		producto.setId(7);
		producto.setNombre("PruebaProducto");
		producto.setDescripcion("Pruebas");
		producto.setPrecio(BigDecimal.valueOf(1.5).setScale(2));
		producto.setStock(4);
		producto.setUsuario(usuario);

		productoRepo.save(producto);

		Optional<Producto>producto2 = productoRepo.findById(7);

		assertEquals(producto2.get().getId(), producto.getId());
		assertEquals(producto2.get().getNombre(), producto.getNombre());
		assertEquals(producto2.get().getDescripcion(), producto.getDescripcion());
		assertEquals(producto2.get().getPrecio(), producto.getPrecio());
		assertEquals(producto2.get().getStock(), producto.getStock());
		assertEquals(producto2.get().getUsuario(), producto.getUsuario());

		producto.setNombre("PruebaProducto2");
		productoRepo.save(producto);
		producto2 = productoRepo.findById(7);
		assertNotEquals(producto2.get().getNombre(), "PruebaProducto1");

		productoRepo.delete(producto);
		producto2 = productoRepo.findById(7);
		assertFalse(producto2.isPresent());
	}

	@Test
	final void RolTest(){
		Role rol = new Role();

		rol.setId(1L);
		rol.setName("COMPRADOR");
		roleRepo.save(rol);

		Optional<Role> rol2 = roleRepo.findByName("COMPRADOR");

		assertEquals(rol2.get().getId(), rol.getId());
		assertEquals(rol2.get().getName(), rol.getName());

	}
}


