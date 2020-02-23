package com.xtel.VietLotAPI.DBContext;

import java.sql.Connection;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

import com.xtel.VietLotAPI.log4j.Logfactory;

public class BaseConnectionPool {
	Logger logger = Logfactory.getLogger(BaseConnectionPool.class);

	private LinkedBlockingQueue<DBConnectionCell> pool = new LinkedBlockingQueue<DBConnectionCell>();
	protected int numOfConnectionCreated = 0;
	
	protected String sid;
	protected int max_pool_size;
	protected int init_pool_size;
	protected int min_pool_size;
	protected long time_out = 10000;

	protected String url;
	protected String user;
	protected String password;

	protected Thread thread;
	protected long start_time;
	protected long end_time;
	protected static BaseConnectionPool instancePool;

	
	public BaseConnectionPool() {
	}

	//Setting for Pool
	public synchronized static BaseConnectionPool getInstace() {
		if (instancePool == null) {
			instancePool = new BaseConnectionPool();
			instancePool.setInit_pool_size(DBConnectionPoolConfig.INIT_POOL_SIZE);
			instancePool.setMax_pool_size(DBConnectionPoolConfig.MAX_POOL_SIZE);
			instancePool.setMin_pool_size(DBConnectionPoolConfig.MIN_POOL_SIZE);
			instancePool.setUrl(DBConnectionPoolConfig.URL);
			instancePool.setUser(DBConnectionPoolConfig.USERNAME);
			instancePool.setPassword(DBConnectionPoolConfig.PASSWORD);
			instancePool.setTime_out(DBConnectionPoolConfig.TIME_OUT);
			instancePool.sid = DBConnectionPoolConfig.SID;
			instancePool.thread = new AbsThread() {

				/*
				 * When the number of connection > min connection , close TimeOut Connection
				 */
				@Override
				public void execute() {
					for (DBConnectionCell connection : instancePool.pool) {
						if (instancePool.numOfConnectionCreated > instancePool.min_pool_size) {
							if (connection.isTimeOut()) {
								try {
									connection.close();
									instancePool.pool.remove(connection);
									instancePool.numOfConnectionCreated--;
								} catch (Exception e) {
									logger.info("Waring : Connection can not close in timeOut !");
								}
							}
						}
					}

				}
			};
		}
		return instancePool;
	}

	public void start() {
		logger.info("Create Connection pool........................ ");
		// Load Connection to Pool
		start_time = System.currentTimeMillis();
		try {
			for (int i = 0; i < init_pool_size; i++) {
				DBConnectionCell connection = new DBConnectionCell(url, user, password, time_out);
				pool.put(connection);
				numOfConnectionCreated++;
			}
		} catch (Exception e) {
			logger.warn(String.format(
					"[Message : can not start connection pool] - [Connection pool : %s] - " + "[Exception : %s]",
					this.toString(), e));
		}
		thread.start();
		end_time = System.currentTimeMillis();
		logger.info("Start Connection pool in : " + end_time + start_time + " ms .");
	}

	
	public synchronized DBConnectionCell getConnection() {
		DBConnectionCell connectionWraper = null;
		if (pool.size() == 0 && numOfConnectionCreated < max_pool_size) {
			connectionWraper = new DBConnectionCell(url, user, password, time_out);
			try {
				pool.put(connectionWraper);
			} catch (InterruptedException e) {
				logger.warn("Can not PUT Connection to Pool, Current Poll size = " + pool.size()
						+ " , Number Connection : " + numOfConnectionCreated, e);
				e.printStackTrace();
			}
			numOfConnectionCreated++;
		}
		try {
			connectionWraper = pool.take();
		} catch (InterruptedException e) {
			logger.warn("Can not GET Connection from Pool, Current Poll size = " + pool.size()
					+ " , Number Connection : " + numOfConnectionCreated);
			e.printStackTrace();
		}
		connectionWraper.setRelaxTime(System.currentTimeMillis());
		return connectionWraper;
	}

	
	public void releaseConnection(DBConnectionCell conn) {
		try {
			if (conn.isClosed()) {
				pool.remove(conn);
				DBConnectionCell connection = new DBConnectionCell(url, user, password, time_out);
				pool.put(connection);
			} else {
				pool.put(conn);
			}
		} catch (Exception e) {
			logger.info("Connection : " + conn.toString(), e);
		}
	}

	@Override
    public String toString() {
        return "ConnectionPool{" +
                "pool=" + pool +
                ", max_pool_size=" + max_pool_size +
                ", init_pool_size=" + init_pool_size +
                ", min_pool_size=" + min_pool_size +
                ", time_out=" + time_out +
                ", url='" + url + '\'' +
                ", user='" + user + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
	
	public void setMax_pool_size(int max_pool_size) {
		this.max_pool_size = max_pool_size;
	}

	public void setInit_pool_size(int init_pool_size) {
		this.init_pool_size = init_pool_size;
	}

	public void setMin_pool_size(int min_pool_size) {
		this.min_pool_size = min_pool_size;
	}

	public void setTime_out(long time_out) {
		this.time_out = time_out;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setEnd_time(long end_time) {
		this.end_time = end_time;
	}

}
