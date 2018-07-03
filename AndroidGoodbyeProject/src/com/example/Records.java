package com.example;

public class Records {
	 int groupId;
     int recordNum;
     int userAccount;
     String time;
      String content;
     Boolean isLogState;
     Boolean isImgState;
     
     public	Records(
    		 int groupId,
    		 int userAccount,int recordNum,
    		 String time,
    		 String content,
    		 Boolean isLogState,
    		 Boolean isImgState) {
		this.content = content;
		this.groupId = groupId;
		this.isImgState = isImgState;
		this.userAccount = userAccount;
		this.recordNum = recordNum;
		this.time = time;
		this.isLogState = isLogState;
		
	}
}
