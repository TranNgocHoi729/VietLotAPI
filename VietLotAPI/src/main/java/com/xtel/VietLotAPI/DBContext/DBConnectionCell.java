package com.xtel.VietLotAPI.DBContext;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.xtel.VietLotAPI.log4j.Logfactory;


// This class contain Connection and check Connection close and get Timeout
public class DBConnectionCell {
	private String username;
	private String password;
	private String url;
	private long relaxTime;
	private long timeOut;
	
	Logger logger = Logfactory.getLogger(DBConnectionCell.class);
	Connection conn;
	public Connection getDbConnection() {		
		return conn;
	}
	public long getRelaxTime() {
		return relaxTime;
	}
	public long getTimeOut() {
		return timeOut;
	}



	public DBConnectionCell(String username, String password, String url, long relaxTime) {
		super();
		this.username = username;
		this.password = password;
		this.url = url;
		this.relaxTime = relaxTime;
		try {
			conn = DriverManager.getConnection(DBConnectionPoolConfig.URL, DBConnectionPoolConfig.USERNAME, DBConnectionPoolConfig.PASSWORD);
		} catch (SQLException e) {
			logger.info(getInformation());
		} 
	}
	public boolean isTimeOut() {
		if(System.currentTimeMillis() - relaxTime > timeOut) {
			return true;
		}
		return false;
	}
	
	public void close() throws Exception{
		try {
			conn.close();
		} catch (Exception e) {
			logger.warn(getInformation(), e);
		}
	}
	public boolean isClosed() throws Exception{
		return conn.isClosed();
	}
	
	public String getInformation() {
		String infor = "\nInfor :  Connection ( URL :"+url+" , User name : "+username+", Password : "+password+", "+conn.toString()+" )";
		return infor;
	}
	public void setRelaxTime(long relaxTime) {
		this.relaxTime = relaxTime;
	}
	
	
}
