package com.xtel.VietLotAPI.DBContext;

import org.apache.log4j.Logger;

import com.xtel.VietLotAPI.log4j.Logfactory;

public abstract class AbsThread extends Thread{
	Logger logger = Logfactory.getLogger(AbsThread.class);
	
	@Override
	public void run() {
		while(true) {
			synchronized (this) {
				try {
					sleep(10);
				} catch (Exception e) {
					logger.info(e);
				}
			}
		}
	}
	
	public abstract void execute();
}
