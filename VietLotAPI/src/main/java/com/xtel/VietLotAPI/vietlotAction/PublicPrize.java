package com.xtel.VietLotAPI.vietlotAction;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.xtel.VietLotAPI.dao.VietlotDao;
import com.xtel.VietLotAPI.log4j.Logfactory;
import com.xtel.VietLotAPI.model.User;
import com.xtel.VietLotAPI.model.VIetlotPrize;


public class PublicPrize {
	Logger logger = Logfactory.getLogger(VietlotDao.class);
	ArrayList<User> listUser ;
	VietLotAction vietlotResult;
	
	public PublicPrize(ArrayList<User> listUser) {
		super();
		this.listUser = listUser;
		this.vietlotResult = new VietLotAction();
		logger.info("Result VietLot : "+ vietlotResult.Result());
	}
	public String publicVietLotResult() {
		return vietlotResult.Result();
	}
	
	// lấy danh sách người trúng thưởng từ datatbase
	public ArrayList<VIetlotPrize> rewardList(){
		ArrayList<VIetlotPrize> listReward = new ArrayList<VIetlotPrize>();
		for(User user : listUser) {
			String identify = user.getIdentify();
			String reward = vietlotResult.getReward(user.getVietlot_number());
			int cost = user.getVietlot_price();
			VIetlotPrize prize = new VIetlotPrize(identify, reward, cost);
			listReward.add(prize);
		}
		return listReward;
	}	
	
}
