package com.xtel.VietLotAPI.DBContext;

public class DBConnectionPoolConfig {
	private static final String SERVER_NAME = "localhost"; 
	public static final int MAX_POOL_SIZE = 20;
	public static final int MIN_POOL_SIZE = 5;
	public static final int INIT_POOL_SIZE = 10;
	public static final String DB_PORT = "1521";
	public static final String USERNAME = "vietlot";
	public static final String PASSWORD = "hoi1999bac";
	public static final String SID = "orcl";
	public static final String URL =  "jdbc:oracle:thin:@" + SERVER_NAME + ":" + DB_PORT + ":" + SID;
	public static final long TIME_OUT = 200;
}
