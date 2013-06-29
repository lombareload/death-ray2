package com.deathRay.test;

import com.deathRay.cloud.Tarea;
import com.deathRay.dao.TareaDao;
import com.deathRay.util.Util;
import com.google.api.services.datastore.DatastoreV1.Entity;
import com.google.api.services.datastore.DatastoreV1.EntityResult;
import com.google.api.services.datastore.client.DatastoreException;

import java.util.Date;
import java.util.List;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Tarea tarea = new Tarea();
		TareaDao tareaDao;
		Util util = new Util();
		try {
			// Crea o actualiza una tarea:
			tareaDao = tarea.createOrUpdateTarea(new Date(), new Date(), 4L,
					10723L, 10719L, "tarea1", "Arreglar Portlet");
			System.out.println("Tarea Creada : " + tareaDao.toString());
			tareaDao = tarea.createOrUpdateTarea(new Date(), new Date(), 4L,
					10723L, 10719L, "tarea3", "Arreglar Portlet");
			System.out.println("Tarea Creada : " + tareaDao.toString());tareaDao = tarea.createOrUpdateTarea(new Date(), new Date(), 4L,
					10723L, 10719L, "tarea4", "Arreglar Portlet");
			System.out.println("Tarea Creada : " + tareaDao.toString());tareaDao = tarea.createOrUpdateTarea(new Date(), new Date(), 4L,
					10723L, 10719L, "tarea5", "Arreglar Portlet");
			System.out.println("Tarea Creada : " + tareaDao.toString());
			
			System.out.println("Todas las tareas:");
			List<EntityResult> results = tarea.findAllTask();
			Entity entity;
			TareaDao foundTareaDao;
			for (EntityResult entityResult : results) {
				entity = entityResult.getEntity();
				foundTareaDao = util.entidadToTareaDao(entity);
				System.out.println(foundTareaDao.toString() + "\n");
			}

			System.out.println("Tareas por proyecto:");
			List<EntityResult> resultsByProject = tarea.findAllTaskByProyecto(10719L);
			for (EntityResult entityResult : resultsByProject) {
				entity = entityResult.getEntity();
				foundTareaDao = util.entidadToTareaDao(entity);
				System.out.println(foundTareaDao.toString() + "\n");
			}

			System.out.println("Tareas por usuario:");
			List<EntityResult> resultsByUser = tarea.findAllTaskByUsuario(99L);
			for (EntityResult entityResult : resultsByUser) {
				entity = entityResult.getEntity();
				foundTareaDao = util.entidadToTareaDao(entity);
				System.out.println(foundTareaDao.toString() + "\n");
			}

			System.out.println("Tareas por estado:");
			List<EntityResult> resultsByStatus = tarea.findAllTaskByEstado(4L);
			for (EntityResult entityResult : resultsByStatus) {
				entity = entityResult.getEntity();
				foundTareaDao = util.entidadToTareaDao(entity);
				System.out.println(foundTareaDao.toString() + "\n");
			}

		} catch (DatastoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
