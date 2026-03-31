package com.spring.ecommerce.model;

import java.util.Date;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "ordenes")
public class Orden {
	@Id // Id is primary key
	@GeneratedValue(strategy = GenerationType.IDENTITY) // generating auto ID
	private Integer id;
	private String numero;
	private Date fechaCreacion;
	private Date fechaRecepcion;
	private Double total;
	@Column(columnDefinition = "TINYINT DEFAULT 0") // default value = 0
	private byte esFavorita;

	public byte getEsFavorita() {
		return esFavorita;
	}

	public void setEsFavorita(byte esFavorita) {
		this.esFavorita = esFavorita;
	}

	@ManyToOne
	private Usuario usuario; // one order to many users

	@OneToMany(mappedBy = "orden")
	private List<DetalleOrden> detalle; // list , because there is an order registered with many details

	public Orden() { // empty constructor required by Hibernate JPA
	}

	public Orden(Integer id, String numero, Date fechaCreacion, Date fechaRecepcion, Double total) {
		// super();
		this.id = id;
		this.numero = numero;
		this.fechaCreacion = fechaCreacion;
		this.fechaRecepcion = fechaRecepcion;
		this.total = total;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public Date getFechaRecepcion() {
		return fechaRecepcion;
	}

	public void setFechaRecepcion(Date fechaRecepcion) {
		this.fechaRecepcion = fechaRecepcion;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public List<DetalleOrden> getDetalle() {
		return detalle;
	}

	public void setDetalle(List<DetalleOrden> detalle) {
		this.detalle = detalle;
	}

	@Override
	public String toString() {
		return "Orden [id=" + id + ", numero=" + numero + ", fechaCreacion=" + fechaCreacion + ", fechaRecepcion="
				+ fechaRecepcion + ", total=" + total + "]";
	} // method heredated of Object class{} to write a read attribute values on string

}
