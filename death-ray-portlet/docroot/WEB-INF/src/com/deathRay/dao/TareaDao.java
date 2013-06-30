package com.deathRay.dao;

import java.util.Date;

/**
 * 
 * @author Michael Rondón
 * @version 20130627
 */
public class TareaDao {
	
	private String fecha_inicial;
	private String fecha_final;
	private Long estado;
	private Long userId;
	private Long groupId;
	private String name;
	private String descripcion;
	
	public TareaDao() {
		super();
		this.fecha_inicial = "";
		this.fecha_final = "";
		this.estado = -1L;
		this.userId = -1L;
		this.groupId = -1L;
		this.name = "";
		this.descripcion = "";
	}
	
	public TareaDao(String fecha_inicial, String fecha_final, Long estado,
			Long userId, Long groupId, String name, String descripcion) {
		super();
		this.fecha_inicial = fecha_inicial;
		this.fecha_final = fecha_final;
		this.estado = estado;
		this.userId = userId;
		this.groupId = groupId;
		this.name = name;
		this.descripcion = descripcion;
	}
	
	public String getFecha_inicial() {
		return fecha_inicial;
	}
	public void setFecha_inicial(String fecha_inicial) {
		this.fecha_inicial = fecha_inicial;
	}
	public String getFecha_final() {
		return fecha_final;
	}
	public void setFecha_final(String fecha_final) {
		this.fecha_final = fecha_final;
	}
	public Long getEstado() {
		return estado;
	}
	public void setEstado(Long estado) {
		this.estado = estado;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getGroupId() {
		return groupId;
	}
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	@Override
	public String toString() {
		return "TareaDao [fecha_inicial=" + fecha_inicial + ", fecha_final="
				+ fecha_final + ", estado=" + estado + ", userId=" + userId
				+ ", groupId=" + groupId + ", name=" + name + ", descripcion="
				+ descripcion + "]";
	}
	
}
