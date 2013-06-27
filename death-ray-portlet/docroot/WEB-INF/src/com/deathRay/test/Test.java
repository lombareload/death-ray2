package com.deathRay.test;

import com.deathRay.cloud.Tarea;
import com.deathRay.dao.TareaDao;
import com.deathRay.util.Util;
import com.google.api.services.datastore.DatastoreV1.Entity;
import com.google.api.services.datastore.client.DatastoreException;

import java.util.Date;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Tarea tarea = new Tarea();
		TareaDao tareaDao;
		try {
			tareaDao=tarea.createOrUpdateTarea(new Date(), new Date(), 1L, 2L, 3L,4L);
			System.out.println("getEstado : "+tareaDao.getEstado());
			System.out.println("getGroupId : "+tareaDao.getGroupId());
			System.out.println("getTaskId : "+tareaDao.getTaskId());
			System.out.println("getUserId : "+tareaDao.getUserId());
			System.out.println("getClass : "+tareaDao.getClass());
			System.out.println("getFecha_final : "+tareaDao.getFecha_final());
			System.out.println("getFecha_inicial : "+tareaDao.getFecha_inicial());
		} catch (DatastoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		/*System.out.println(System.getenv("DATASTORE_PRIVATE_KEY_FILE"));
		System.out.println(System.getenv("PATH"));*/
		
		
	}

}
