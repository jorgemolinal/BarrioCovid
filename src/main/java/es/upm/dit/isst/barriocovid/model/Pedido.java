package es.upm.dit.isst.barriocovid.model;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String estado;

    @Column
    private BigDecimal ticket;

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @ManyToOne
    private Usuario usuario;

    @ManyToOne
    private Usuario voluntario;

    public Usuario getVoluntario() {
        return voluntario;
    }

    public void setVoluntario(Usuario voluntario) {
        this.voluntario = voluntario;
    }

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "pedido_linear_pedidos", joinColumns = @JoinColumn(name = "pedido_id"), inverseJoinColumns = @JoinColumn(name = "linea_pedido_id"))
    private List<LineaPedido> lineaPedidos;

    public Pedido() {
    }

    public List<LineaPedido> getLineaPedidos() {
        return lineaPedidos;
    }

    public void setLineaPedidos(List<LineaPedido> lineaPedidos) {
        this.lineaPedidos = lineaPedidos;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getTicket() {
        return ticket;
    }

    public void setTicket(BigDecimal ticket) {
        this.ticket = ticket;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public String toString() {

        String result = "";
        for (int i = 0; i < this.getLineaPedidos().size(); i++) {
            result += "Nombre : " + this.getLineaPedidos().get(i).getProducto().getNombre() + ", Cantidad : "
                    + this.getLineaPedidos().get(i).getCantidad() + ", Total : €"
                    + this.getLineaPedidos().get(i).getTotal() + "--";
        }

        return result;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Pedido other = (Pedido) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
