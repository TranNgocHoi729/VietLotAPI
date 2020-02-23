package com.xtel.VietLotAPI.model;

public class User {
	private String identify;
	private String vietlot_number;
	private int vietlot_price;
	private String date;
	public User(String identify, String vietlot_number, int vitelot_price,String date) {
		super();
		this.identify = identify;
		this.vietlot_number = vietlot_number;
		this.vietlot_price = vitelot_price;
		this.date = date;
	}
	public User(String identify, String vietlot_number, int vitelot_price) {
		super();
		this.identify = identify;
		this.vietlot_number = vietlot_number;
		this.vietlot_price = vitelot_price;

	}
	public String getIdentify() {
		return identify;
	}
	public String getVietlot_number() {
		return vietlot_number;
	}
	public int getVietlot_price() {
		return vietlot_price;
	}
	public String getDate() {
		return date;
	}
	
	
}
