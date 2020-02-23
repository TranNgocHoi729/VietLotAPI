package com.xtel.VietLotAPI.vietlotAction;

import java.util.Random;


// class này Quay sổ số và trả về danh sách các giải

public class VietLotAction {
	private String giaiDacBiet, giaiNhat;
	private String[] giaiNhi;
	private String[] giaiBa, giaiTu;

	private String getRandomNumber() {
		String result = "";
		Random random = new Random();
		for (int i = 0; i < 3; i++) {
			result += String.valueOf(random.nextInt(9));
		}
		return result;
	}

	private String[] setKetQua(String[] giai, int scale) {
		giai = new String[scale];
		for (int i = 0; i < giai.length; i++) {
			giai[i] = getRandomNumber();
		}
		return giai;
	}

	public VietLotAction() {
		this.giaiTu = setKetQua(giaiTu, 3);
		this.giaiBa = setKetQua(giaiBa, 3);
		this.giaiNhi = setKetQua(giaiNhi, 2);
		this.giaiNhat = getRandomNumber();
		this.giaiDacBiet = getRandomNumber();
	}

	public String getReward(String number) {
		String reward = "";
		if (number.equals(giaiDacBiet)) {
			reward = "giaiDacBiet";
		} else if (number.equals(giaiNhat)) {
			reward = "giaiNhat";
		} else if (checkReward(number, giaiBa)) {
			reward = "giaiba";
		} else if (checkReward(number, giaiTu)) {
			reward = "giaiTu";
		} else {
			reward = "no reward";
		}
		return reward;
	}

	private boolean checkReward(String number, String[] reward) {
		for (int i = 0; i < reward.length; i++) {
			if (number.equals(reward[i])) {
				return true;
			}
		}
		return false;
	}
	
	public String getDetailResult(String[] result) {
		String rs = "";
		for (int i = 0; i < result.length; i++) {
			rs += result[i]+ "  ";
		}
		return rs;
	}
	public String Result() {
		String giaiDacBiet = "\n Giai Dac Biet : "+ this.giaiDacBiet;
		String giaiNhat = "\n Giai Nhat : "+ this.giaiNhat;
		String giaiNhi = "\n Giai nhi : "+ getDetailResult(this.giaiNhi);
		String giaiBa = "\n Giai Ba : "+ getDetailResult(this.giaiBa);
		String giaiTu = "\n Giai Tu : "+ getDetailResult(this.giaiTu);
		return giaiDacBiet+giaiNhat + giaiNhi+ giaiBa + giaiTu ; 
	}
}
