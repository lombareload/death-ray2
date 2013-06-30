package com.deathRay.util;

import com.deathRay.dao.TareaDao;
import com.google.api.services.datastore.DatastoreV1.Entity;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * 
 * @author Michael Rondón
 * @version 20130627
 */
public class Util {

	private static final Log log = LogFactoryUtil.getLog(Util.class);
	private HashMap<Long, String> hashEstados = null;
	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	
	public static Util getInstance() {
        return UtilHolder.INSTANCE;
    }
	
	private Util(){
		
	}

    private static class UtilHolder {

        private static final Util INSTANCE = new Util();
    }

	/**
	 * Método que convierte una fecha en formato Date a formato
	 * TimestampMicroseconds que es el que se almacena en cloud.
	 * 
	 * @param date
	 *            Fecha en formato Date
	 * @return
	 */
	public long dateToTimestampMicroseconds(Date date) {
		long epoch = 0L;
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			epoch = cal.getTimeInMillis() * 1000;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return epoch;
	}

	/**
	 * Método que convierte la fecha en formato TimestampMicroseconds a Date.
	 * 
	 * @param epoch
	 *            fecha en formato TimestampMicroseconds
	 * @return
	 */
	public String timestampMicrosecondsToDate(Long epoch) {
		// String date = new
		// java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new
		// java.util.Date (epoch*1000));
		return epoch==0L?"": dateFormat.format( new java.util.Date(epoch / 1000));

	}

	/**
	 * Método que obtiene los datos de una entidad Tarea y los inserta en un
	 * objeto TareaDao. Devuelve null si los objetos almecenados no tienen la
	 * estructura de una entidad Tarea.
	 * 
	 * @param entity
	 *            Entida de tipo tarea.
	 * @return Objeti tipo TareaDao.
	 */
	public TareaDao entidadToTareaDao(Entity entity) {
		TareaDao tareaDao;
		try {
			String fecha_inicial = timestampMicrosecondsToDate(entity
					.getProperty(0).getValue(0).getTimestampMicrosecondsValue());
			String fecha_final = timestampMicrosecondsToDate(entity
					.getProperty(1).getValue(0).getTimestampMicrosecondsValue());
			Long estado = entity.getProperty(2).getValue(0).getIntegerValue();
			Long userId = entity.getProperty(3).getValue(0).getIntegerValue();
			Long groupId = entity.getProperty(4).getValue(0).getIntegerValue();
			String descripcion = entity.getProperty(5).getValue(0)
					.getStringValue();
			String name = entity.getKey().getPathElement(0).getName();
			tareaDao = new TareaDao(fecha_inicial, fecha_final, estado, userId,
					groupId, name, descripcion);
		} catch (IndexOutOfBoundsException e) {
			tareaDao = new TareaDao();
		} catch (Exception e) {
			tareaDao =  new TareaDao();
		}
		return tareaDao;
	}

	/**
	 * Método que devuelve un HashMap de los estados que lee a la
	 * variable Estado del archivo de configuración.
	 * @return HashMap con el Id del estado como key.
	 */
	public HashMap<Long, String> getEstados() {
		if (hashEstados == null) {
			hashEstados = new HashMap<Long, String>();
			String estados = ConfigurationProperties.getInstance().getProperty(
					"Estado");
			StringTokenizer stringTokeneizer = new StringTokenizer(estados, ",");
			while (stringTokeneizer.hasMoreTokens()) {
				String token = stringTokeneizer.nextToken();
				StringTokenizer t = new StringTokenizer(token, ":");
				long idEstado;
				hashEstados.put(-1L,"");
				while (t.hasMoreTokens()) {
					try {
						idEstado = Long.parseLong(t.nextToken());
						hashEstados.put(idEstado, t.nextToken());
					} catch (NumberFormatException e) {
						log.error(e);
					}

				}
			}
		}
		return hashEstados;
	}
}
