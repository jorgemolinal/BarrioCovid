package es.upm.dit.isst.barriocovid.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import es.upm.dit.isst.barriocovid.model.LineaPedido;
import es.upm.dit.isst.barriocovid.model.Pedido;
import es.upm.dit.isst.barriocovid.model.Producto;
import es.upm.dit.isst.barriocovid.model.Usuario;
import es.upm.dit.isst.barriocovid.repository.LineaPedidoRepository;
import es.upm.dit.isst.barriocovid.repository.PedidoRepository;
import es.upm.dit.isst.barriocovid.repository.ProductoRepository;
import es.upm.dit.isst.barriocovid.repository.UsuarioRepository;

@Controller
public class TiendaController {

    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;
    private final LineaPedidoRepository lineaPedidoRepository;
    private final PedidoRepository pedidoRepository;

    public TiendaController(ProductoRepository productoRepository, UsuarioRepository usuarioRepository,
            LineaPedidoRepository lineaPedidoRepository, PedidoRepository pedidoRepository) {
        this.productoRepository = productoRepository;
        this.usuarioRepository = usuarioRepository;
        this.lineaPedidoRepository = lineaPedidoRepository;
        this.pedidoRepository = pedidoRepository;
    }

    private final Logger logger = LoggerFactory.getLogger(TiendaController.class);
    @GetMapping("/misproductos")
    public String verProductos(Model model, Principal principal) {
        Optional<Usuario> usuario= usuarioRepository.findByEmail(principal.getName());
        model.addAttribute("misproductos", productoRepository.findByUsuarioAndIsDeletedIsFalse(usuario.get()));
        return "vendedor/verProducto";
    }

    @GetMapping("/misproductos/nuevoProducto")
    public String crearProducto() {
        return "vendedor/crearProducto";
    }

    @PostMapping("/misproductos/productoGuardado")
    public String guardarProducto(Producto producto,Principal principal) {
        Optional<Usuario> usuario= usuarioRepository.findByEmail(principal.getName());
        producto.setUsuario(usuario.get());
        producto.setDeleted(false);
        productoRepository.save(producto);
        logger.info("Este es el objeto producto {}", producto);
        return "redirect:/misproductos";
    }

    @GetMapping("/misproductos/editarProducto/{id}")
    public String editarProducto(@PathVariable Integer id, Model model) {
        Producto producto = new Producto();
        producto = productoRepository.findById(id).get();
        model.addAttribute("producto", producto);
        logger.info("Producto buscado: {}", producto);
        return "/vendedor/editarProducto";
    }

    @PostMapping("/misproductos/productoEditado")
    public String productoEditado(Producto producto,Principal principal) {
        Optional<Usuario> usuario= usuarioRepository.findByEmail(principal.getName());
        producto.setUsuario(usuario.get());
        producto.setDeleted(false);
        productoRepository.save(producto);
        return "redirect:/misproductos";
    }

    @GetMapping("/misproductos/borrarProducto/{id}")
    public String borrarProducto(@PathVariable Integer id) {
        Optional<Producto> producto= productoRepository.findById(id);
        producto.get().setDeleted(true);
        productoRepository.save(producto.get());
        return "redirect:/misproductos";
    }

    @GetMapping("/misproductos/pedidos")
    public String verpedidos(Model model,Principal principal) {
        Optional<Usuario> usuario= usuarioRepository.findByEmail(principal.getName());
        model.addAttribute("comprador", usuario.get());
        List<Pedido> pedidos = (List<Pedido>) pedidoRepository.findAll();

        List<Pedido> pedidos1= new ArrayList<>();

        for(Pedido pedido: pedidos) {
             if (pedido.getLineaPedidos().get(0).getProducto().getUsuario().getId().equals(usuario.get().getId()) && (pedido.getEstado().equals("Esperando la confirmación del vendedor") || pedido.getEstado().equals("Esperando confirmación del vendedor solamente"))) {
                 pedidos1.add(pedido);
             }
     }
     model.addAttribute("pedidos",pedidos1);
     return "vendedor/pedido";
    }


    //gets confirmed orders
    @GetMapping("/misproductos/pedidos/confirmado")
    public String confirmarpedido(Model model,Principal principal) {
        List<Pedido> pedidos = (List<Pedido>) pedidoRepository.findAll();
        List<Pedido> pedidos1 = new ArrayList<>();
        Optional<Usuario> usuario = usuarioRepository.findByEmail(principal.getName());
        for (Pedido pedido : pedidos) {
            if (pedido.getLineaPedidos().get(0).getProducto().getUsuario().getId().equals(usuario.get().getId()) && (pedido.getEstado().equals("Aceptado por voluntario") || pedido.getEstado().equals("Esperando confirmación del vendedor solamente") || pedido.getEstado().equals("Aceptado por el vendedor")) ) {
                pedidos1.add(pedido);
            } 

        }

        model.addAttribute("pedidos",pedidos1);
        return "vendedor/pedidoconfirmado";
    }

    @GetMapping("/misproductos/pedidosConfirmados/{id}")
    public String verpedidosconfirmados(@PathVariable("id") Integer id,Model model , Principal principal){
        Optional<Usuario> usuario= usuarioRepository.findByEmail(principal.getName());
        model.addAttribute("comprador", usuario.get());
        Optional<Pedido> pedido= pedidoRepository.findById(id);
        pedido.get().setEstado("Aceptado por el vendedor");
        pedidoRepository.save(pedido.get());
        return "redirect:/misproductos/pedidos";
    }

    @GetMapping("/misproductos/pedidosterminado")
    public String verpedidoterminadolisto(Model model,Principal principal) {
        List<Pedido> pedidos = (List<Pedido>) pedidoRepository.findAll();
        List<Pedido> pedidos1 = new ArrayList<>();
        Optional<Usuario> usuario = usuarioRepository.findByEmail(principal.getName());
        for (Pedido pedido : pedidos) {
            if (pedido.getLineaPedidos().get(0).getProducto().getUsuario().getId().equals(usuario.get().getId()) && (pedido.getEstado().equals("Pedido entregado") || pedido.getEstado().equals("Aceptado por voluntario") || pedido.getEstado().equals("Pedido finalizado"))) {
                pedidos1.add(pedido);
            }

        }
        model.addAttribute("pedidos",pedidos1);
        return "vendedor/pedidosterminado";
    }

    @GetMapping("/misproductos/pedidosterminad/{id}")
    public String pedidosterminado(@PathVariable("id") Integer id,Model model , Principal principal){
        Optional<Usuario> usuario= usuarioRepository.findByEmail(principal.getName());
        model.addAttribute("comprador", usuario.get());
        Optional<Pedido> pedido= pedidoRepository.findById(id);
        pedido.get().setEstado("Pedido entregado");
        pedidoRepository.save(pedido.get());
        return "redirect:/misproductos/pedidos";
    }

}
