package es.upm.dit.isst.barriocovid.controller;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.upm.dit.isst.barriocovid.model.LineaPedido;
import es.upm.dit.isst.barriocovid.model.Pedido;
import es.upm.dit.isst.barriocovid.model.Producto;
import es.upm.dit.isst.barriocovid.model.Usuario;
import es.upm.dit.isst.barriocovid.repository.LineaPedidoRepository;
import es.upm.dit.isst.barriocovid.repository.PedidoRepository;
import es.upm.dit.isst.barriocovid.repository.ProductoRepository;
import es.upm.dit.isst.barriocovid.repository.UsuarioRepository;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CompradorController {

    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;
    private final LineaPedidoRepository lineaPedidoRepository;
    private final PedidoRepository pedidoRepository;

    private final Logger logger = LoggerFactory.getLogger(CompradorController.class);


    public CompradorController(ProductoRepository productoRepository, UsuarioRepository usuarioRepository,
            LineaPedidoRepository lineaPedidoRepository, PedidoRepository pedidoRepository) {
        this.productoRepository = productoRepository;
        this.usuarioRepository = usuarioRepository;
        this.lineaPedidoRepository = lineaPedidoRepository;
        this.pedidoRepository = pedidoRepository;
    }


    @GetMapping("/comprador/{idcomprador}")
    public String homeComprador(Model model, @PathVariable Integer idcomprador) {
        model.addAttribute("vendedores", usuarioRepository.findByTipo("VENDEDOR"));
        model.addAttribute("idcomprador", idcomprador);
        return "comprador/home";
    }

    @PostMapping("/comprador/{idcomprador}/buscartienda")
    public String buscarTienda(@RequestParam String nombre, @PathVariable Integer idcomprador) {
        Optional<Usuario> usuario = usuarioRepository.findByNombre(nombre);
        Integer idTienda = 0;
        try {
            idTienda = usuario.get().getId();
            return "redirect:/comprador/" + idcomprador + "/tienda/" + idTienda;
        } catch (Exception e) {
        }
        return "redirect:/comprador/" + idcomprador;
    }

    @GetMapping("/comprador/{idcomprador}/tienda/{id}")
    public String tiendaSeleccionada(@PathVariable Integer id, Model model, @PathVariable Integer idcomprador) {
        Usuario usuario = usuarioRepository.findById(id).get();
        model.addAttribute("productos", productoRepository.findByUsuarioAndIsDeletedIsFalse(usuario));
        model.addAttribute("idcomprador", idcomprador);
        return "comprador/productos_tiendas";
    }

    @PostMapping("/carrito")
    public String añadirAlCarrito(@RequestParam Integer idproducto, @RequestParam Integer cantidad, Model model, Principal principal) {
        LineaPedido lineaPedido = new LineaPedido();
        Producto producto = productoRepository.findById(idproducto).get();
        Usuario usuario = usuarioRepository.findByEmail(principal.getName()).get();
        List<LineaPedido> lineaPedidos= lineaPedidoRepository.findByUsuarioAndProcessedIsFalse(usuario);

        List<LineaPedido> lineaPedido1= lineaPedidos.stream().filter(pd->pd.getProducto().getId().equals(producto.getId())).collect(Collectors.toList());
        if(lineaPedido1.size() > 0) {
            lineaPedidoRepository.delete(lineaPedido1.get(0));
        }
        List<LineaPedido> checkShopFilter= lineaPedidos.stream().filter(pd->!pd.getProducto().getUsuario().getId().equals(producto.getUsuario().getId())).collect(Collectors.toList());
        if(checkShopFilter.size() > 0) {
            lineaPedidoRepository.deleteAll(checkShopFilter);
        }
        BigDecimal total = producto.getPrecio().multiply(new BigDecimal(cantidad));
            lineaPedido.setCantidad(cantidad);
            lineaPedido.setProcessed(false);
            lineaPedido.setPrecio(producto.getPrecio());
            lineaPedido.setNombre(producto.getNombre());
            lineaPedido.setDescripcion(producto.getDescripcion());
            lineaPedido.setTotal(total);
            lineaPedido.setProducto(producto);
            lineaPedido.setUsuario(usuario);
            lineaPedidoRepository.save(lineaPedido);
        Integer idVendedor = producto.getUsuario().getId();
        return "redirect:/comprador/" + usuario.getId() + "/tienda/" + idVendedor;
    }

    @GetMapping("/verCarrito")
    public String verCarrito(Model model ,Principal principal) {
        logger.info("Estos son los detalles de los productos {}", lineaPedidoRepository.findAll());
        Usuario usuario = usuarioRepository.findByEmail(principal.getName()).get();
        List<LineaPedido> lineaPedidos = lineaPedidoRepository.findByUsuarioAndProcessedIsFalse(usuario);
        BigDecimal amount = BigDecimal.ZERO;
        for (LineaPedido p : lineaPedidos) {
            amount = amount.add(p.getTotal());
        }
        model.addAttribute("total", amount);
        model.addAttribute("lineaP", lineaPedidos);
        model.addAttribute("idcomprador", usuario.getId());
        return "comprador/carrito";
    }

    @GetMapping("/borrarCarrito/{id}")
    public String quitarDelCarrito(@PathVariable Integer id, Model model,Principal principal) {
        LineaPedido pedido1 = lineaPedidoRepository.findById(id).get();
        lineaPedidoRepository.delete(pedido1);
        Usuario usuario= usuarioRepository.findByEmail(principal.getName()).get();
        model.addAttribute("idcomprador", usuario.getId());
        return "redirect:/verCarrito";
    }

    @GetMapping("/comprador/pedido")
    public String verPedido(Model model,Principal principal) {
        Optional<Usuario> usuario= usuarioRepository.findByEmail(principal.getName());
        List<Pedido> pedidos =  pedidoRepository.findByUsuario(usuario.get());
        model.addAttribute("pedidos", pedidos);
        model.addAttribute("idcomprador", usuario.get().getId());
        return "comprador/pedido";
    }

    @GetMapping("/comprador/pedidoGuardado")
    public String guardarPedido(Model model, Principal principal, RedirectAttributes redirectAttributes) {
        Usuario usuario= usuarioRepository.findByEmail(principal.getName()).get();
        List<LineaPedido> lineaPedidos = lineaPedidoRepository.findByUsuarioAndProcessedIsFalse(usuario);
        BigDecimal amount = BigDecimal.ZERO;
        for (LineaPedido p : lineaPedidos) {
            amount = amount.add(p.getTotal());
        }
        lineaPedidoRepository.saveAll(lineaPedidos);

        if(lineaPedidos.size() > 0) {
            Pedido pedido1 = new Pedido();
            pedido1.setLineaPedidos(lineaPedidos);
            pedido1.setTicket(amount);
            pedido1.setEstado("Esperando confirmación del vendedor solamente");

            pedido1.setUsuario(usuario);
            if(usuario.isVulnerable()) {
                try {
                    pedido1.setEstado("Esperando la confirmación del vendedor");
                    Optional<Usuario> usuario1 = pedidoRepository.findByLeastOrders();
                    if (usuario1.isPresent()) {
                        pedido1.setVoluntario(usuario1.get());
                    } else if (usuarioRepository.findByTipo("VOLUNTARIO").size() > 0) {
                        pedido1.setVoluntario(usuarioRepository.findByTipo("VOLUNTARIO").get(0));
                    }
                }
                catch(Exception e){
                    pedido1.setVoluntario(usuarioRepository.findByTipo("VOLUNTARIO").get(0));
                }
            }
            pedidoRepository.save(pedido1);
        }
        redirectAttributes.addFlashAttribute("message", "Pedido realizado correctamente");
        return "redirect:/comprador/" + usuario.getId();
    }

    @GetMapping("/comprador/pedido/{id}/cancelar")
    public String pagocancelado(@PathVariable("id") Integer id,RedirectAttributes redirectAttributes) {
        Optional<Pedido> pedido= pedidoRepository.findById(id);
        if(pedido.get().getEstado().equals("Aceptado por el vendedor") ) {
            redirectAttributes.addFlashAttribute("message","El pedido ya fue aceptado por el vendedor, no se puede cancelar el pedido");
            return "redirect:/comprador/" + pedido.get().getUsuario().getId();
        }
        if(pedido.get().getEstado().equals("Aceptado por voluntario") ) {
            redirectAttributes.addFlashAttribute("message","El pedido ya fue aceptado por el vendedor y el voluntario, no se puede cancelar el pedido");
            return "redirect:/comprador/" + pedido.get().getUsuario().getId();
        }
        if(pedido.get().getEstado().equals("Pedido finalizado") ) {
            redirectAttributes.addFlashAttribute("message","El pedido que intenta cancelar ya ha sido entregado");
            return "redirect:/comprador/" + pedido.get().getUsuario().getId();
        }
        pedido.get().setEstado("Pedido cancelado");
        pedidoRepository.save(pedido.get());
        return "redirect:/comprador/" + pedido.get().getUsuario().getId();
    }
}
