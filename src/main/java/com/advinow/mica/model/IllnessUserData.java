package com.advinow.mica.model;

import java.util.List;

public class IllnessUserData extends JSonModel {
	
private List<IllnessModel> userData;

public List<IllnessModel> getUserData() {
	return userData;
}

public void setUserData(List<IllnessModel> userData) {
	this.userData = userData;
}

}
