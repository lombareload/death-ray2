package com.deathRay.util;

import com.deathRay.dao.TareaDao;
import com.google.api.services.datastore.DatastoreV1.Entity;

import java.util.Calendar;
import java.util.Date;

/**
 * 
 * @author Michael Rondón
 * @version 20130627
 */
public class Util {


	/**
	 * Método que convierte una fecha en formato Date a 
	 * formato TimestampMicroseconds que es el que se 
	 * almacena en cloud.
	 * @param date Fecha en formato Date
	 * @return
	 */
	public long dateToTimestampMicroseconds(Date date) {
		long epoch = 0L;
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			epoch = cal.getTimeInMillis() / 1000;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return epoch;
	}
	
	/**
	 * Método que convierte la fecha en formato TimestampMicroseconds
	 * a Date.
	 * @param epoch fecha en formato TimestampMicroseconds
	 * @return
	 */
	public Date timestampMicrosecondsToDate(Long epoch) {
//		String date = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new java.util.Date (epoch*1000));
		return new java.util.Date (epoch*1000);
		
	}
	
	/**
	 * Método que obtiene los datos de una entidad Tarea y los inserta en un objeto TareaDao.
	 * @param entity Entida de tipo tarea.
	 * @return Objeti tipo TareaDao.
	 */
	public TareaDao entidadToTareaDao(Entity entity){
		Date fecha_inicial = timestampMicrosecondsToDate(entity.getProperty(0).getValue(0).getTimestampMicrosecondsValue());
		Date fecha_final = timestampMicrosecondsToDate(entity.getProperty(1).getValue(0).getTimestampMicrosecondsValue());
		Long estado = entity.getProperty(2).getValue(0).getIntegerValue();
		Long userId = entity.getProperty(3).getValue(0).getIntegerValue();
		Long groupId = entity.getProperty(4).getValue(0).getIntegerValue();
		Long taskId = entity.getKey().getPathElement(0).getId();
		TareaDao tareaDao = new TareaDao(fecha_inicial, fecha_final, estado, userId, groupId, taskId);
		return tareaDao;
		//entityFound.getProperty(0).getValue(0).getTimestampMicrosecondsValue()
	}
}
