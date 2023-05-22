package es.upm.dit.isst.barriocovid.model;

import java.util.List;

import javax.validation.constraints.NotEmpty;

import javax.persistence.*;

@Entity
@Table(name="usuarios")
public class Usuario {

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(unique = true, length = 20)
	private String username;

	@Column(length = 60)
	private String password;
	
    @NotEmpty
	private String nombre;
	
	@Column(unique = true)
	private String email;

    @Column(length = 60)
    private String direccion;

    @Column
    private boolean vulnerable;

	@Column
	private String tipo;


	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(
			name = "users_roles",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "role_id")
	)
	private List<Role> roles;

	@OneToMany(mappedBy = "usuario")
    private List<Producto> productos;

    @OneToMany(mappedBy = "usuario")
    private List<Pedido> pedidos;

	public Usuario() {
	}

	public Usuario(String username, String password, @NotEmpty String nombre, String email,
			String direccion, boolean vulnerable, String tipo) {
		this.username = username;
		this.password = password;
		this.nombre = nombre;
		this.email = email;
		this.direccion = direccion;
		this.vulnerable = vulnerable;
		this.tipo = tipo;
	}

	public Usuario(Integer id, String username, String password, @NotEmpty String nombre, String email,
			String direccion, boolean vulnerable, String tipo) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.nombre = nombre;
		this.email = email;
		this.direccion = direccion;
		this.vulnerable = vulnerable;
		this.tipo = tipo;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public boolean isVulnerable() {
		return vulnerable;
	}

	public void setVulnerable(boolean vulnerable) {
		this.vulnerable = vulnerable;
	}

	public List<Producto> getProductos() {
		return productos;
	}

	public void setProductos(List<Producto> productos) {
		this.productos = productos;
	}

	public List<Pedido> getPedidos() {
		return pedidos;
	}

	public void setPedidos(List<Pedido> pedidos) {
		this.pedidos = pedidos;
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
		Usuario other = (Usuario) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	

}
