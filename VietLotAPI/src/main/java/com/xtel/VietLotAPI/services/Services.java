package com.xtel.VietLotAPI.services;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.LinkedBlockingQueue;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.spi.LoggerFactory;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.xtel.VietLotAPI.DBContext.BaseConnectionPool;
import com.xtel.VietLotAPI.DBContext.DBConnectionCell;
import com.xtel.VietLotAPI.dao.ExecuteInsertThread;
import com.xtel.VietLotAPI.dao.VietlotDao;
import com.xtel.VietLotAPI.log4j.Logfactory;
import com.xtel.VietLotAPI.model.User;
import com.xtel.VietLotAPI.model.VIetlotPrize;
import com.xtel.VietLotAPI.publicResult.RollVietLotNumber;
import com.xtel.VietLotAPI.vietlotAction.PublicPrize;

import oracle.jdbc.proxy.annotation.Post;

@Path("services")
public class Services {
	static String vlResult="";
	Gson gson = new Gson();
	static BaseConnectionPool DBPool = BaseConnectionPool.getInstace();
	static Logger logger = Logfactory.getLogger(Services.class);
	private LinkedBlockingQueue<String> insertQueue = new LinkedBlockingQueue<String>();
	static Date publicDate;
	public static void setResult(String result) {
		vlResult = result;
	}
	static {
		DBPool.start();
		startTaskRollNumberVietLot();
	}

	// Set Schedule daily
	private static void startTaskRollNumberVietLot() {
		
		Calendar calendar = Calendar.getInstance();
		Calendar currentTime = calendar;
		calendar.set(Calendar.HOUR_OF_DAY, 18);
		calendar.set(Calendar.MINUTE, 30);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		publicDate = calendar.getTime();
		long period = 24 * 60 * 60 * 1000;
		logger.info("Start Task schedule at : " + currentTime.getTime().toString());
		DBConnectionCell cnc = DBPool.getConnection();
		try {
			RollVietLotNumber task = new RollVietLotNumber(cnc.getDbConnection());
			Timer timer = new Timer();
			timer.schedule(task, publicDate, period);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	// get USer information with Prize
	@GET
	@Produces({ MediaType.TEXT_PLAIN })
	@Path("result/{identify}")
	public String getResult(@PathParam("identify") String id) {
		String result = "";
		Calendar cal = Calendar.getInstance();
		if(!cal.getTime().before(publicDate)) {
			if (id.isEmpty()) {
				logger.info("Identify not Correct : " + id);
				return "ID can not Null";
			} else {
				DBConnectionCell cnc = DBPool.getConnection();
				System.out.println(cnc.getInformation());
				try {
					Connection conn = cnc.getDbConnection();
					VietlotDao vlDao = new VietlotDao(conn);
					VIetlotPrize user = vlDao.checkPrize(id);
					System.out.println(user.toString());
					result = gson.toJson(user);
				} catch (Exception e) {
					logger.warn("Can not get Connection !! Identify = " + id, e);
					e.printStackTrace();
				}
			}
		}else {
			result = "Time is before 18.30 ! Please wait to public time";
		}	
		return result;
	}

	
	// Vì hàm After của thư viện Calendar bị khóa nên dùng java.time.localDateTime
	@POST
	@Path("add")
	public String addUser(String body) throws Exception {
		String result = "Complete";
		int hour = java.time.LocalDateTime.now().getHour();
		if (hour < 18) {
			if (body.isEmpty()) {
				result = "No Information to Add";
			} else {
				insertQueue.put(body);
				DBConnectionCell cnc = DBPool.getConnection();
				User user = parseBody(insertQueue.take(), User.class);
				ExecuteInsertThread insert = new ExecuteInsertThread(cnc.getDbConnection(), user);
				insert.start();
				DBPool.releaseConnection(cnc);
			}
		}
		return result;
	}

	
	@GET
	@Path("users")
	public String getAllUser() {
		ArrayList<User> listOfUserByDate = new ArrayList<User>();
		DBConnectionCell cnc = DBPool.getConnection();
		VietlotDao getInfor;
		try {
			getInfor = new VietlotDao(cnc.getDbConnection());
			listOfUserByDate = getInfor.getAlluser();
		} catch (Exception e) {
			logger.warn("Can not getConnection !! ", e);
			e.printStackTrace();
		}

		System.out.println(gson.toJson(listOfUserByDate));
		return gson.toJson(listOfUserByDate);
	}

	private <T> T parseBody(String body, Class<T> t) {
		return gson.fromJson(body, t);
	}
	
	@GET
	@Path("result")
	public String getVietLotResult() {
		return gson.toJson(vlResult);
	}
}
