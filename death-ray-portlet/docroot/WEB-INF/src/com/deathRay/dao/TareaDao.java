package com.deathRay.dao;

import java.util.Date;

/**
 * 
 * @author Michael Rondón
 * @version 20130627
 */
public class TareaDao {
	
	private Date fecha_inicial;
	private Date fecha_final;
	private Long estado;
	private Long userId;
	private Long groupId;
	private Long taskId;
			
	public TareaDao(Date fecha_inicial, Date fecha_final, Long estado,
			Long userId, Long groupId, Long taskId) {
		super();
		this.fecha_inicial = fecha_inicial;
		this.fecha_final = fecha_final;
		this.estado = estado;
		this.userId = userId;
		this.groupId = groupId;
		this.taskId = taskId;
	}
	
	public Date getFecha_inicial() {
		return fecha_inicial;
	}
	public void setFecha_inicial(Date fecha_inicial) {
		this.fecha_inicial = fecha_inicial;
	}
	public Date getFecha_final() {
		return fecha_final;
	}
	public void setFecha_final(Date fecha_final) {
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
	public Long getTaskId() {
		return taskId;
	}
	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}	

}
