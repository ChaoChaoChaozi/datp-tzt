package com.ruizton.main.Enum;

public class VirtualCapitalOperationInStatusEnum {
	public static final int WAIT_0 = 0 ;
	public static final int WAIT_1 = 1 ;
	public static final int WAIT_2 = 2 ;
	public static final int SUCCESS = 3 ;
	
	public static String getEnumString(int value) {
		String name = "";
		if(value == WAIT_0){
			name = "待确认";
		}else if(value == WAIT_1){
			name = "待确认";
		}else if(value == WAIT_2){
			name = "待确认";
		}else if(value == SUCCESS){
			name = "转入成功";
		}
		return name;
	}
}
