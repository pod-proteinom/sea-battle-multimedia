package com.multimedia.seabattle.dwr;

import org.directwebremoting.Browser;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.directwebremoting.ui.dwr.Util;
import org.springframework.stereotype.Service;

import com.multimedia.seabattle.model.beans.DwrTestBean;

@Service("DwrTestService")
@RemoteProxy(name="DwrTestService")
public class DwrTestService{
	private String msg = "not started;";

	public void updateClock(String msg){
		this.msg = msg;
		getTime();
	}

	@RemoteMethod
	public DwrTestBean getTestBean(){
		DwrTestBean b = new DwrTestBean();
		b.setText("hello, now "+System.currentTimeMillis()+" o'clock");
		return b;
	}

	@RemoteMethod
	public void getTime(){
		String page = "/seabattle/resources/dwr-test.html";
		Browser.withPage(page, new Runnable() {
			public void run() {
				Util.setValue("msg", msg);
			}
		});
	}
}
