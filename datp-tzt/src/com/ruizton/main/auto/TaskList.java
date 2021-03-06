package com.ruizton.main.auto;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.ruizton.main.Enum.ValidateMailStatusEnum;
import com.ruizton.main.Enum.ValidateMessageStatusEnum;
import com.ruizton.main.model.Fvalidateemail;
import com.ruizton.main.model.Fvalidatemessage;
import com.ruizton.main.model.Fvirtualcointype;
import com.ruizton.main.service.admin.AdminService;
import com.ruizton.main.service.admin.EntrustlogService;
import com.ruizton.main.service.admin.VirtualCoinService;
import com.ruizton.main.service.admin.VirtualWalletService;
import com.ruizton.main.service.front.FrontValidateService;

/**
 * 定时任务
 * @author   Dylan
 * @data     2018年8月13日
 * @typeName TaskList
 * 说明 ：存储到LinkedList中 待定时线程进行处理
 * 		
 */
public class TaskList {
	@Autowired
	private FrontValidateService frontValidateService ;
	@Autowired
	private EntrustlogService entrustlogService ;
	@Autowired
	private AdminService adminService;
	@Autowired
	private VirtualCoinService virtualCoinService;
	@Autowired
	private VirtualWalletService virtualWalletService;
	
	private LinkedList<String> messageList ;
	private LinkedList<String> mailList ;
	private int frandomnumber;
	private int totalPerson;
	private int totaltrade;
	private Map<String,BigDecimal> coinMap = new HashMap<String,BigDecimal>();
	
	public Map<String, BigDecimal> getCoinMap() {
		return coinMap;
	}

	public void setCoinMap(Map<String, BigDecimal> coinMap) {
		this.coinMap = coinMap;
	}
	
	public int getTotalPerson() {
		return totalPerson;
	}

	public void setTotalPerson(int totalPerson) {
		this.totalPerson = totalPerson;
	}

	public int getTotaltrade() {
		return totaltrade;
	}

	public void setTotaltrade(int totaltrade) {
		this.totaltrade = totaltrade;
	}
	
	public int getFrandomnumber() {
		return frandomnumber;
	}

	public void setFrandomnumber(int frandomnumber) {
		this.frandomnumber = frandomnumber;
	}

	public TaskList(){
		//message
		messageList = new LinkedList<String>() ;
		//mail
		mailList = new LinkedList<String>() ;
		
	}
	
	public void init(){
	//	 long startTime=System.currentTimeMillis();	
		readData() ;
//        long endTime=System.currentTimeMillis();
//       float excTime=(float)(endTime-startTime)/1000;
//       System.out.println("++++");
//       System.out.println("TaskList 执行时间："+excTime+"s");
//		this.setFrandomnumber(new Random().nextInt(80)+11);
		//交易金额
//		setTotaltrade(this.entrustlogService.getTotalTradeAmt().intValue());
//		String filter = "where fstatus=1";
//		setTotalPerson(this.adminService.getAllCount("Fuser", filter));
		String xx = "where fstatus=1";
		List<Fvirtualcointype> coins = this.virtualCoinService.list(0, 0, xx, false);
		for (Fvirtualcointype fvirtualcointype : coins) {
			getCoinMap().put(fvirtualcointype.getFid(), this.virtualWalletService.getTotalQty(fvirtualcointype.getFid()));
		}
	}
	private void readData(){
		//message
		List<Fvalidatemessage> list1 = this.frontValidateService.findFvalidateMessageByProperty("fstatus", ValidateMessageStatusEnum.Not_send) ;
		
		for (Fvalidatemessage fvalidatemessage : list1) {
			messageList.add(fvalidatemessage.getFid()) ;
		}
		//mail
		List<Fvalidateemail> list2 = this.frontValidateService.findValidateMailsByProperty("fstatus", ValidateMailStatusEnum.Not_send) ;
		for(Fvalidateemail fvalidateemail:list2){
			this.mailList.add(fvalidateemail.getFid()) ;
		}
	}

	/**
	 * 
	 *  作者：           Dylan
	 *  标题：           getOneMessage 
	 *  时间：           2018年8月13日
	 *  描述：          从LinkedList的对象messageList集合列表中弹出一个数据
	 *  	         根据LinkedList的pop()函数属性 ，弹出后一个数据后 则列表中就会减少一个数据
	 *  	         相反的函数有push()函数 ，将一个元素推送到LinkedList列表中
	 *  @return
	 */
	public synchronized String getOneMessage() {
		synchronized (messageList) {
			String id = "" ;
			if(messageList.size()>0){
				id = messageList.pop() ; //从LinkedList的对象messageList集合列表中弹出一个数据
			}
			return id ;
		}
	}
	
	public synchronized void returnMessageList(String id){
		synchronized (messageList) {
			messageList.add(id) ;
		}
	}
	
	public String getOneMail() {
		synchronized(this.mailList){
			String id = "" ;
			if(this.mailList.size()>0){
				id = mailList.pop() ;
			}
			return id ;
		}
	}
	
	public synchronized void returnMailList(String id){
		synchronized(this.mailList){
			mailList.add(id) ;
		}
	}
	
}
