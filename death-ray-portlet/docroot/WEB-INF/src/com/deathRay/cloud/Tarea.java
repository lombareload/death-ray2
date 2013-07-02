package com.deathRay.cloud;

import com.deathRay.dao.TareaDao;
import com.deathRay.util.Util;
import com.google.api.services.datastore.DatastoreV1.*;
import com.google.api.services.datastore.client.Datastore;
import com.google.api.services.datastore.client.DatastoreException;
import com.google.api.services.datastore.client.DatastoreHelper;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * 
 * @author Michael Rondón
 * @version 20130627
 */
public class Tarea {

	private static Util util = Util.getInstance();
	private static Datastore datastore;
	private static final Log log = LogFactoryUtil.getLog(Util.class);

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
	 * 
	 * @param fecha_inicial
	 *            Fecha de inicio de la tarea.
	 * @param fecha_final
	 *            Fecha de terminación de la tarea.
	 * @param estado
	 *            Estado de la tarea.
	 * @param userId
	 *            Identificador del usuario en liferay.
	 * @param groupId
	 *            Identificador del grupo (proyecto).
	 * @param nombre
	 * @param descripcion
	 * @return
	 * @throws DatastoreException
	 */
	public TareaDao createOrUpdateTarea(Date fecha_inicial, Date fecha_final,
			Long estado, Long userId, Long groupId, String nombre,
			String descripcion) throws DatastoreException {

		log.info("Entra a createOrUpdateTarea");
		Long time_inicial=0L;
		Long time_final=0L;
		if (fecha_inicial != null) {
			time_inicial = util.dateToTimestampMicroseconds(fecha_inicial);
		}
		if (fecha_final != null) {
			time_final = util.dateToTimestampMicroseconds(fecha_final);
		}
		Entity entityFound = null;

		Entity.Builder tarea = Entity.newBuilder();

		Key.Builder key = Key.newBuilder().addPathElement(
				Key.PathElement.newBuilder().setKind("Tarea").setName(nombre));

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

		tarea.addProperty(Property.newBuilder().setName("descripcion")
				.addValue(Value.newBuilder().setStringValue(descripcion)));

		BlindWriteRequest.Builder req = BlindWriteRequest.newBuilder();
		req.getMutationBuilder().addUpsert(tarea);

		datastore.blindWrite(req.build());
		LookupRequest.Builder lreq = LookupRequest.newBuilder();
		lreq.addKey(key);

		LookupResponse lresp = datastore.lookup(lreq.build());
		entityFound = lresp.getFound(0).getEntity();

		return util.entidadToTareaDao(entityFound);

	}

	/**
	 * Método que busca todas las tareas
	 * 
	 * @return
	 * @throws DatastoreException
	 */
	public List<EntityResult> findAllTask() throws DatastoreException {
		Query.Builder queryBuilder = Query.newBuilder();
		queryBuilder.addKindBuilder().setName("Tarea");
		queryBuilder.addOrder(DatastoreHelper.makeOrder("fecha_inicio",
				PropertyOrder.Direction.DESCENDING));

		RunQueryRequest request = RunQueryRequest.newBuilder()
				.setQuery(queryBuilder).build();

		RunQueryResponse response = datastore.runQuery(request);
		List<EntityResult> results = new ArrayList<EntityResult>(response
				.getBatch().getEntityResultList());

		return results;
	}

	/**
	 * Lista todas las tareas pertenecientes a un proyecto.
	 * 
	 * @param groupId
	 *            Identificador del proyecto o grupo.
	 * @return
	 * @throws DatastoreException
	 */
	public List<EntityResult> findAllTaskByProyecto(Long groupId)
			throws DatastoreException {

		Filter projectFilter = com.google.api.services.datastore.client.DatastoreHelper
				.makeFilter(
						"proyecto",
						PropertyFilter.Operator.EQUAL,
						com.google.api.services.datastore.client.DatastoreHelper
								.makeValue(groupId)).build();

		Query.Builder queryBuilder = Query.newBuilder();
		queryBuilder.addKindBuilder().setName("Tarea");

		queryBuilder.setFilter(projectFilter);

		/*
		 * queryBuilder.addOrder(DatastoreHelper.makeOrder("fecha_inicio",
		 * PropertyOrder.Direction.DESCENDING));
		 */

		RunQueryRequest request = RunQueryRequest.newBuilder()
				.setQuery(queryBuilder).build();

		RunQueryResponse response = datastore.runQuery(request);

		List<EntityResult> results = new ArrayList<EntityResult>(response
				.getBatch().getEntityResultList());

		ListIterator<EntityResult> listIterator=results.listIterator(results.size());
		List<EntityResult> newResults=new LinkedList<EntityResult>();
		while(listIterator.hasPrevious()){
			newResults.add(listIterator.previous());
		}
		

		if (response.getBatch().getMoreResults() == QueryResultBatch.MoreResultsType.NOT_FINISHED) {
			log.warn("WARNING: partial results\n");
		}

		return newResults;
	}

	/**
	 * Lista todas las tareas con determinado estado.
	 * 
	 * @param estado
	 *            Identificador del estado de la tarea.
	 * @return
	 * @throws DatastoreException
	 */
	public List<EntityResult> findAllTaskByEstado(Long estado)
			throws DatastoreException {

		Filter projectFilter = com.google.api.services.datastore.client.DatastoreHelper
				.makeFilter(
						"estado",
						PropertyFilter.Operator.EQUAL,
						com.google.api.services.datastore.client.DatastoreHelper
								.makeValue(estado)).build();

		Query.Builder queryBuilder = Query.newBuilder();
		queryBuilder.addKindBuilder().setName("Tarea");

		queryBuilder.setFilter(projectFilter);

		/*
		 * queryBuilder.addOrder(DatastoreHelper.makeOrder("fecha_inicio",
		 * PropertyOrder.Direction.DESCENDING));
		 */

		RunQueryRequest request = RunQueryRequest.newBuilder()
				.setQuery(queryBuilder).build();

		RunQueryResponse response = datastore.runQuery(request);

		List<EntityResult> results = new ArrayList<EntityResult>(response
				.getBatch().getEntityResultList());

		if (response.getBatch().getMoreResults() == QueryResultBatch.MoreResultsType.NOT_FINISHED) {
			log.warn("WARNING: partial results\n");
		}

		return results;
	}

	/**
	 * Lista todas las tareas pertenecientes a un usuario.
	 * 
	 * @param userId
	 *            Identificador del usuario.
	 * @return
	 * @throws DatastoreException
	 */
	public List<EntityResult> findAllTaskByUsuario(Long userId)
			throws DatastoreException {

		Filter projectFilter = com.google.api.services.datastore.client.DatastoreHelper
				.makeFilter(
						"usuario",
						PropertyFilter.Operator.EQUAL,
						com.google.api.services.datastore.client.DatastoreHelper
								.makeValue(userId)).build();

		Query.Builder queryBuilder = Query.newBuilder();
		queryBuilder.addKindBuilder().setName("Tarea");

		queryBuilder.setFilter(projectFilter);

		/*
		 * queryBuilder.addOrder(DatastoreHelper.makeOrder("fecha_inicio",
		 * PropertyOrder.Direction.DESCENDING));
		 */

		RunQueryRequest request = RunQueryRequest.newBuilder()
				.setQuery(queryBuilder).build();

		RunQueryResponse response = datastore.runQuery(request);

		List<EntityResult> results = new ArrayList<EntityResult>(response
				.getBatch().getEntityResultList());

		ListIterator<EntityResult> listIterator=results.listIterator(results.size());
		List<EntityResult> newResults=new LinkedList<EntityResult>();
		while(listIterator.hasPrevious()){
			newResults.add(listIterator.previous());
		}
		
		if (response.getBatch().getMoreResults() == QueryResultBatch.MoreResultsType.NOT_FINISHED) {
			log.warn("WARNING: partial results\n");
		}

		return newResults;
	}

	public TareaDao findTaskByName(String nombre) throws DatastoreException {
		Entity entityFound = null;
		if (!nombre.isEmpty()) {
			Key.Builder key = Key.newBuilder().addPathElement(
					Key.PathElement.newBuilder().setKind("Tarea")
							.setName(nombre));

			LookupRequest.Builder lreq = LookupRequest.newBuilder();
			lreq.addKey(key);
			LookupResponse lresp = datastore.lookup(lreq.build());
			if(lresp!=null&& lresp.getFoundCount()>0 &&lresp.getFound(0)!=null&&lresp.getFound(0).getEntity()!=null){
				entityFound = lresp.getFound(0).getEntity();
			}
		}
		return util.entidadToTareaDao(entityFound);
	}
}
