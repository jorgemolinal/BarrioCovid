package es.upm.dit.isst.barriocovid.model;

import javax.validation.constraints.NotEmpty;

import java.math.BigDecimal;

import javax.persistence.*;

@Entity
@Table(name = "productos")
public class Producto {

    @Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

	@NotEmpty(message ="no puede estar vacio")
    @Column(length = 25, nullable = false)
    private String nombre;

    @NotEmpty(message ="No puede estar vacio")
    @Column(length = 255)
    private String descripcion;

    @NotEmpty(message ="No puede estar vacio")
    @Column(nullable = false)
    private BigDecimal precio;


    private boolean isDeleted;

    @NotEmpty(message ="No puede estar vacio")
    @Column(nullable = false, columnDefinition = "long default 0")
    private int stock;

    @ManyToOne
    private Usuario usuario;

    public Producto() {
    }

    public Producto(@NotEmpty(message = "no puede estar vacio") String nombre, String descripcion,
                    @NotEmpty(message = "No puede estar vacio") BigDecimal precio,
                    @NotEmpty(message = "No puede estar vacio") int stock) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
    }


    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public Producto(Integer id, @NotEmpty(message = "no puede estar vacio") String nombre, String descripcion,
                    @NotEmpty(message = "No puede estar vacio") BigDecimal precio,
                    @NotEmpty(message = "No puede estar vacio") int stock) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
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
        Producto other = (Producto) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }  
}
