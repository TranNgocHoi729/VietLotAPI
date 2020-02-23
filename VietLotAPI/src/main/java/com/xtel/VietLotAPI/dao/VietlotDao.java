package com.xtel.VietLotAPI.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.xtel.VietLotAPI.log4j.Logfactory;
import com.xtel.VietLotAPI.model.User;
import com.xtel.VietLotAPI.model.VIetlotPrize;

public class VietlotDao {
	Connection conn;
	Logger logger = Logfactory.getLogger(VietlotDao.class);

	public VietlotDao(Connection conn) {
		super();
		this.conn = conn;
	}

	
	// return All user 	
	public ArrayList<User> getAlluser() {
		ArrayList<User> listUser = new ArrayList<User>();

		if (conn == null) {
			logger.warn(infor());
		} else {
			String sql = "select * from VIETLOT_USER ";
			ResultSet rs = null;
			PreparedStatement ps = null;
			try {
				ps = conn.prepareStatement(sql);
				rs = ps.executeQuery();
				while (rs.next()) {
					String identify = rs.getString(1);
					String vietlot_number = rs.getString(2);
					int vietlot_price = Integer.parseInt(rs.getString(3));
					String date_insert = rs.getString(4);
					listUser.add(new User(identify, vietlot_number, vietlot_price, date_insert));
				}
			} catch (SQLException e) {
				logger.warn(infor(), e);
				e.printStackTrace();
			} finally {
				try {
					rs.close();
					ps.close();
				} catch (SQLException e) {
					logger.warn("Can not Close PrepareStatement or Resultset ");
					e.printStackTrace();
				}
			}

		}
		System.out.println(listUser.size());
		return listUser;
	}

	// Insert User to DB 
	public int insertToDB(User user) {
		String sql = "insert into vietlot_user(identify, VIETLOT_NUMBER, vietlot_price, date_insert) "
				+ "values(?,?,?,SYSDATE)";
		int result = 0;
		if (user == null) {
			logger.warn("User null : " + user.toString());
			return 0;
		}
		if (conn == null) {
			logger.warn("Connection null : " + infor());
		} else {
			PreparedStatement ps = null;
			try {
				ps = conn.prepareStatement(sql);
				ps.setString(1, user.getIdentify());
				ps.setString(2, user.getVietlot_number());
				ps.setInt(3, user.getVietlot_price());
				result = ps.executeUpdate();
			} catch (Exception e) {
				logger.warn("PrepareStatement null ; Connection : " + infor(), e);
			}finally {
				try {
					ps.close();
				} catch (SQLException e) {
					logger.warn("PrepareStatement cnnot Close ! Connection : "+ conn.toString());
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	// Get Result with Identify
	public VIetlotPrize checkPrize(String identify) {
		VIetlotPrize user = null;
		if (identify.isEmpty() || identify == null) {
			logger.warn("input Exception  : " + identify);
			return user;
		} else {
			String sql = "select vietlot_user.identify , vietlot_Prize.prize,vietlot_user.vietlot_price, vietlot_prize.reward\r\n" + 
					"from vietlot_user , Vietlot_prize\r\n" + 
					"where  vietlot_user.identify  = Vietlot_prize.identify and vietlot_user.identify = '"+ identify+"'";
			PreparedStatement ps = null;
			try {
				ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery();
				while(rs.next()) {
					String identifynumber = rs.getString(1);
					String prize = rs.getString(2);
					int cost = rs.getInt(3);
					int reward= rs.getInt(4);
					user = new VIetlotPrize(identifynumber, prize, cost, reward);
				}
				rs.close();
				ps.close();
			} catch (SQLException e) {
				logger.warn("Prepare statement Fault : "+ ps.toString() , e);
				e.printStackTrace();
			}
		}
		return user;
	}

	// Save User Prize to DB
	public int saveResultToDB(ArrayList<VIetlotPrize> list) {
		int result = 0;
		if (list == null) {

		} else {
			String head = "INSERT ALL ";
			String tail = " SELECT * FROM dual";
			String middle = "";
			for (VIetlotPrize prize : list) {
				middle += "INTO vietlot_prize(identify, PRIZE, REWARD)" + " VALUES ('" + prize.getIndentify() + "','"
						+ prize.getPrize() + "'," + prize.getReward() + ") ";
			}
			String sql = head + middle + tail;
			PreparedStatement ps = null;
			try {
				ps = conn.prepareStatement(sql);
				result = ps.executeUpdate();
				ps.close();
			} catch (Exception e) {
				logger.warn("Prepare statement null : " + ps.toString(), e);
				e.printStackTrace();
			}
		}
		return result;
	}

	// Public VietLot Result : All prize
	public int saveVietLotResult(String result) {
		int check = 0;
		if(result.isEmpty()) {
			logger.warn("Viet lot result is empty   !!!");
			return 0;
		}else {
			String sql = "insert into result(public_date, result)" + 
					"values(SYSDATE,?)";
			PreparedStatement ps = null;
			try {
				ps = conn.prepareStatement(sql);
				ps.setString(1, result);
				check = ps.executeUpdate();
				ps.close();
			} catch (Exception e) {
				logger.warn("prepareStatement fault : "+ ps.toString(),e);
				e.printStackTrace();
			}
		}
		return check;
	}
	
	private String infor() {
		return conn.toString();
	}
}
