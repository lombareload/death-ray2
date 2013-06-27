package com.deathRay.cloud;

import com.deathRay.dao.TareaDao;
import com.deathRay.util.Util;
import com.google.api.services.datastore.DatastoreV1.*;
import com.google.api.services.datastore.client.Datastore;
import com.google.api.services.datastore.client.DatastoreException;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Date;

/**
 * 
 * @author Michael Rondón
 * @version 20130627
 */
public class Tarea {

	private static Util util = new Util();
	private static Datastore datastore;

	public Tarea() {
		try {
			datastore = DatastoreManager.getDatastore();
		} catch (GeneralSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Método para crear o actualizar una tarea.
	 * @param fecha_inicial Fecha de inicio de la tarea.
	 * @param fecha_final Fecha de terminación de la tarea.
	 * @param estado Estado de la tarea.
	 * @param userId Identificador del usuario en liferay.
	 * @param groupId Identificador del grupo (proyecto).
	 * @param taskId Identificador de la tarea.
	 * @return
	 * @throws DatastoreException
	 */
	public TareaDao createOrUpdateTarea(Date fecha_inicial, Date fecha_final,
			Long estado, Long userId, Long groupId, Long taskId)
			throws DatastoreException {

		Long time_inicial = util.dateToTimestampMicroseconds(fecha_inicial);
		Long time_final = util.dateToTimestampMicroseconds(fecha_final);
		Entity entityFound = null;

		Entity.Builder tarea = Entity.newBuilder();

		Key.Builder key = Key.newBuilder().addPathElement(
				Key.PathElement.newBuilder().setKind("Tarea").setId(taskId));

		tarea.setKey(key);

		tarea.addProperty(Property
				.newBuilder()
				.setName("fecha_inicio")
				.addValue(
						Value.newBuilder().setTimestampMicrosecondsValue(
								time_inicial)));

		tarea.addProperty(Property
				.newBuilder()
				.setName("fecha_fin")
				.addValue(
						Value.newBuilder().setTimestampMicrosecondsValue(
								time_final)));

		tarea.addProperty(Property.newBuilder().setName("estado")
				.addValue(Value.newBuilder().setIntegerValue(estado)));

		tarea.addProperty(Property.newBuilder().setName("usuario")
				.addValue(Value.newBuilder().setIntegerValue(userId)));

		tarea.addProperty(Property.newBuilder().setName("proyecto")
				.addValue(Value.newBuilder().setIntegerValue(groupId)));

		BlindWriteRequest.Builder req = BlindWriteRequest.newBuilder();
		req.getMutationBuilder().addUpsert(tarea);

		datastore.blindWrite(req.build());
		LookupRequest.Builder lreq = LookupRequest.newBuilder();
		lreq.addKey(key);

		LookupResponse lresp = datastore.lookup(lreq.build());
		entityFound = lresp.getFound(0).getEntity();

		return util.entidadToTareaDao(entityFound);		
		
	}

}
