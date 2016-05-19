package com.tianyi.yw.model;

import java.util.List;

import com.tianyi.yw.common.utils.Page;

public class User extends Page {
    private Integer id;

    private String name;

    private String account;

    private String password;

    private String salt;

    
    private int selectedMainMemu;
    
    private int selectedChildMenu;
    
    private List<Functions> childMenuList;
    
    public int getSelectedMainMemu() {
		return selectedMainMemu;
	}

	public void setSelectedMainMemu(int selectedMainMemu) {
		this.selectedMainMemu = selectedMainMemu;
	}

	public int getSelectedChildMenu() {
		return selectedChildMenu;
	}

	public void setSelectedChildMenu(int selectedChildMenu) {
		this.selectedChildMenu = selectedChildMenu;
	}

	public List<Functions> getChildMenuList() {
		return childMenuList;
	}

	public void setChildMenuList(List<Functions> childMenuList) {
		this.childMenuList = childMenuList;
	}

	public boolean isRememberMe() {
		return rememberMe;
	}

	public void setRememberMe(boolean rememberMe) {
		this.rememberMe = rememberMe;
	}

	private boolean rememberMe;
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
}