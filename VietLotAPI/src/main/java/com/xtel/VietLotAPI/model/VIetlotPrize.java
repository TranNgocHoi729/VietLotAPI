package com.xtel.VietLotAPI.model;

public class VIetlotPrize {
	private String indentify;
	private String prize;
	private int cost;
	private int reward;
	public VIetlotPrize(String indentify, String prize, int cost) {
		super();
		this.indentify = indentify;
		this.prize = prize;
		this.cost = cost;
		reward = getReward(cost);
	}
	
	
	public VIetlotPrize(String indentify, String prize, int cost, int reward) {
		super();
		this.indentify = indentify;
		this.prize = prize;
		this.cost = cost;
		this.reward = reward;
	}


	public String getIndentify() {
		return indentify;
	}
	public String getPrize() {
		return prize;
	}
	public int getCost() {
		return cost;
	}
	public int getReward() {
		return this.reward;
	}
	public int getReward(int cost) {
		switch (prize) {
		case "giaiDacBiet":
			return cost * 1000;
		case "giaiNhat" :
			return cost * 500;
		case "giaiNhi" :
			return cost * 300;
		case "giaiBa":
			return cost* 200;
		case "giaiTu":
			return cost*100;
		default:			
			return 0;
		}
	}
}
