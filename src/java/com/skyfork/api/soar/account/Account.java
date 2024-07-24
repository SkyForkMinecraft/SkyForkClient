package com.skyfork.api.soar.account;


import com.skyfork.api.yapeteam.util.TimerUtil;
import lombok.Getter;
import lombok.Setter;
import com.skyfork.api.soar.animation.SimpleAnimation;

@Getter
@Setter
public class Account {

	private AccountType accountType;
	
	private String username, uuid, token;
	
	private String info;
	
	private TimerUtil timer;
	
	public SimpleAnimation opacityAnimation = new SimpleAnimation(0.0F);
	public boolean isDone;
	
	public Account(AccountType accountType, String username, String uuid, String token) {
		this.accountType = accountType;
		this.username = username;
		this.uuid = uuid;
		this.token = token;
		this.info = "";
		this.timer = new TimerUtil();
		this.isDone = true;
	}
}