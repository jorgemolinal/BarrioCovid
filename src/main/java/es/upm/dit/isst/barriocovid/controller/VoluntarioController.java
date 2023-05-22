package es.upm.dit.isst.barriocovid.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import es.upm.dit.isst.barriocovid.model.Pedido;
import es.upm.dit.isst.barriocovid.model.Usuario;
import es.upm.dit.isst.barriocovid.repository.LineaPedidoRepository;
import es.upm.dit.isst.barriocovid.repository.PedidoRepository;
import es.upm.dit.isst.barriocovid.repository.UsuarioRepository;

@Controller
public class VoluntarioController {

    private final UsuarioRepository usuarioRepository;
    private final LineaPedidoRepository lineaPedidoRepository;
    private final PedidoRepository pedidoRepository;



    public VoluntarioController(UsuarioRepository usuarioRepository, LineaPedidoRepository lineaPedidoRepository,
            PedidoRepository pedidoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.lineaPedidoRepository = lineaPedidoRepository;
        this.pedidoRepository = pedidoRepository;
    }

    @GetMapping("/voluntario/{idvoluntario}")
    public String homeVoluntario(Model model, @PathVariable("idvoluntario") Integer idvoluntario) {
        model.addAttribute("voluntario", idvoluntario);
        Optional<Usuario> volunUsuario = usuarioRepository.findById(idvoluntario);
        List<Pedido> pedidos = pedidoRepository.findByVoluntario(volunUsuario.get());
        List<Pedido> filtered= pedidos.stream().filter(p->p.getEstado().equals("Aceptado por el vendedor")).collect(Collectors.toList());
        try {
            model.addAttribute("pedidos", filtered);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            model.addAttribute("mensaje", "El voluntario no tiene pedidos");
            throw e;

        }
        return "voluntario/home";
    }

 
    @GetMapping("/voluntario/{idpedido}/pedidoAceptado")
    public String aceptarPedidoVol(@PathVariable("idpedido") Integer idpedido) {
        Optional<Pedido> pedido = pedidoRepository.findById(idpedido);
        if (pedido.get().getEstado().equals("Aceptado por el vendedor") || pedido.get().getEstado().equals("Pedido entregado") ){
            pedido.get().setEstado("Aceptado por voluntario");
            pedidoRepository.save(pedido.get());
        }
        return "redirect:/voluntario/" + pedido.get().getVoluntario().getId();
    }

    @GetMapping("/voluntario/{idpedido}/pedidoentregado")
    public String entregadoPedidoVol(@PathVariable("idpedido") Integer idpedido) {
        Optional<Pedido> pedido = pedidoRepository.findById(idpedido);
        if (pedido.get().getEstado().equals("Aceptado por el vendedor") ||pedido.get().getEstado().equals("Aceptado por voluntario")  || pedido.get().getEstado().equals("Pedido entregado") ){
            pedido.get().setEstado("Pedido finalizado");
            pedidoRepository.save(pedido.get());
        }
        return "redirect:/voluntario/" + pedido.get().getVoluntario().getId();
    }

    @GetMapping("/voluntario/{idpedido}/pedidoDenegado")
    public String denegarPedidoVol(@PathVariable("idpedido") Integer idpedido) {
        Optional<Pedido> pedido = pedidoRepository.findById(idpedido);
        if (pedido.get().getEstado().equals("Aceptado por el vendedor")){
            pedido.get().setEstado("Rechazada por voluntaria");
            pedidoRepository.save(pedido.get());
        }
        return "redirect:/voluntario/" + pedido.get().getVoluntario().getId();
    }


    @GetMapping("/voluntario/{idvoluntario}/pedidosconfirmados")
    public String verpedidosconfirmados(Model model, @PathVariable("idvoluntario") Integer idvoluntario) {
        Optional<Usuario> usuario= usuarioRepository.findById(idvoluntario);
        List<Pedido> pedidos = (List<Pedido>) pedidoRepository.findByVoluntario(usuario.get());
        List<Pedido> filtered= pedidos.stream().filter(p->p.getEstado().equals("Pedido entregado") || p.getEstado().equals("Pedido finalizado")).collect(Collectors.toList());
        model.addAttribute("pedidos",filtered);
        model.addAttribute("idvoluntario",idvoluntario);
        return "voluntario/pedidosconfirmados";

    }


    @GetMapping("/voluntario/{idvoluntario}/pedidosdenegado")
    public String verpedidosRechazada(Model model, @PathVariable("idvoluntario") Integer idvoluntario) {
        Optional<Usuario> usuario= usuarioRepository.findById(idvoluntario);
        List<Pedido> pedidos = (List<Pedido>) pedidoRepository.findByVoluntario(usuario.get());
        List<Pedido> filtered= pedidos.stream().filter(p->p.getEstado().equals("Rechazada por voluntaria")).collect(Collectors.toList());
        model.addAttribute("pedidos",filtered);
        model.addAttribute("idvoluntario",idvoluntario);
        return "voluntario/pedidosdenegado";

    }
}
