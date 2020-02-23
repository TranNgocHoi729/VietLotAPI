package com.xtel.VietLotAPI.dao;

import java.sql.Connection;

import com.xtel.VietLotAPI.model.User;

// Create Insert User to DB for Pool

public class ExecuteInsertThread extends Thread{
	
	private Connection conn;
	private VietlotDao insert;
	private User user;

	
	public ExecuteInsertThread(Connection conn, User user) {
		super();
		this.conn = conn;	
		this.user = user;
		this.insert = new VietlotDao(conn);
	}

	@Override
	public void run() {
		 insert.insertToDB(user);
		 try {
			sleep(20);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	 

}
