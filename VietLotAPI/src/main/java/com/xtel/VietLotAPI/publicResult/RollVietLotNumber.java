package com.xtel.VietLotAPI.publicResult;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.TimerTask;



import com.xtel.VietLotAPI.DBContext.DBConnectionCell;
import com.xtel.VietLotAPI.dao.VietlotDao;
import com.xtel.VietLotAPI.model.User;
import com.xtel.VietLotAPI.model.VIetlotPrize;
import com.xtel.VietLotAPI.services.Services;
import com.xtel.VietLotAPI.vietlotAction.PublicPrize;


// lấy dữ liệu từ DB , quay Sổ số và lấy ra danh sách người trúng, lưu vào DB 

public class RollVietLotNumber extends TimerTask{

	Connection conn;
	
	public RollVietLotNumber(Connection conn) {
		super();
		this.conn = conn;
	}

	@Override
	public void run() {
		VietlotDao vlDao = new VietlotDao(conn);	
		ArrayList<User> listAllUser = vlDao.getAlluser();
		PublicPrize publicResult = new PublicPrize(listAllUser);
		String vietLotResult = publicResult.publicVietLotResult();
		Services.setResult(vietLotResult);
		vlDao.saveVietLotResult(vietLotResult);
		ArrayList<VIetlotPrize> listPrize = publicResult.rewardList();
		vlDao.saveResultToDB(listPrize);
	}
}
