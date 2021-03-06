package com.ruizton.main.service.front;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruizton.main.Enum.VirtualCapitalOperationInStatusEnum;
import com.ruizton.main.Enum.VirtualCapitalOperationOutStatusEnum;
import com.ruizton.main.Enum.VirtualCapitalOperationTypeEnum;
import com.ruizton.main.Enum.VirtualCoinTypeStatusEnum;
import com.ruizton.main.dao.FfeesDAO;
import com.ruizton.main.dao.FsystemargsDAO;
import com.ruizton.main.dao.FvirtualaddressDAO;
import com.ruizton.main.dao.FvirtualaddressWithdrawDAO;
import com.ruizton.main.dao.FvirtualcaptualoperationDAO;
import com.ruizton.main.dao.FvirtualcointypeDAO;
import com.ruizton.main.dao.FvirtualwalletDAO;
import com.ruizton.main.model.Ffees;
import com.ruizton.main.model.Fuser;
import com.ruizton.main.model.Fvirtualaddress;
import com.ruizton.main.model.FvirtualaddressWithdraw;
import com.ruizton.main.model.Fvirtualcaptualoperation;
import com.ruizton.main.model.Fvirtualcointype;
import com.ruizton.main.model.Fvirtualwallet;
import com.ruizton.util.Utils;

/**
 * @author   Dylan
 * @data     2018年8月14日
 * @typeName FrontVirtualCoinService
 * 说明 ：币种信息业务层
 *
 */
@Service
public class FrontVirtualCoinService {
	@Autowired
	private FvirtualcointypeDAO fvirtualcointypeDAO ;
	@Autowired
	private FfeesDAO ffeesDAO ;
	@Autowired
	private FvirtualaddressDAO fvirtualaddressDAO ;
	@Autowired
	private FvirtualaddressWithdrawDAO fvirtualaddressWithdrawDAO ;
	@Autowired
	private FvirtualcaptualoperationDAO fvirtualcaptualoperationDAO ;
	@Autowired
	private FvirtualwalletDAO fvirtualwalletDAO ;
	@Autowired
	private FsystemargsDAO systemargsDAO;
	
	public List<Fvirtualcointype> findFvirtualCoinType(int status){
		List<Fvirtualcointype> list = this.fvirtualcointypeDAO.findByParam(0, 0, " where fstatus="+status+" order by fid asc ", false, "Fvirtualcointype") ;
		return list ;
	}
	
	/**
	 * 
	 *  作者：           Dylan
	 *  标题：           findFvirtualCoinById 
	 *  时间：           2018年8月14日
	 *  描述：           根据币种id获取币种信息
	 *  	          使用hibernate的get方法获取数据库信息 区别与load方法 会直接实例化查找的对象
	 *  @param id 币种id
	 *  @return Fvirtualcointype 币种信息
	 */
	public Fvirtualcointype findFvirtualCoinById(String id){
		Fvirtualcointype fvirtualcointype = this.fvirtualcointypeDAO.findById(id) ;
		return fvirtualcointype ;
	}
	
	/**
	 * 
	 *  作者：           Dylan
	 *  标题：           findFirstFirtualCoin 
	 *  时间：           2018年8月14日
	 *  描述：            如果type！=0 则查找所有虚拟币区的正常状态且可以交易的货币信息
	 *  	            如果type =0 则查找所有正常状态且可以交易的币种信息  没有type虚拟币条件限制
	 *  
	 *  @param type 取值 ： A_VALUE = 1;//正常  “虚拟币区”   类 ：CoinTypeEnum 
	 *  @return Fvirtualcointype
	 */
	public Fvirtualcointype findFirstFirtualCoin(int type){
		Fvirtualcointype fvirtualcointype = null ;
		//条件 状态正常 可以交易
		String filter ="where fstatus="+VirtualCoinTypeStatusEnum.Normal+" and fisShare=1";
		if(type !=0){//可以交易
			filter = filter +" and ftype="+type;
		}
		//查询出虚拟币区可以交易的货币集合
		List<Fvirtualcointype> list = this.fvirtualcointypeDAO.list(0, 0, filter, false);
		if(list.size()>0){
			fvirtualcointype = list.get(0) ;
		}else{
			if(type != 0){
				fvirtualcointype = findFirstFirtualCoin(0);
			}
		}
		return fvirtualcointype;
	}
	
	public Fvirtualcointype findFirstFirtualCoin_Wallet(){
		Fvirtualcointype fvirtualcointype = null ;
		String filter = "where fstatus="+VirtualCoinTypeStatusEnum.Normal+" and FIsWithDraw=1";
		List<Fvirtualcointype> list = this.fvirtualcointypeDAO.list(0, 0, filter, false);
		if(list.size()>0){
			fvirtualcointype = list.get(0) ;
		}
		return fvirtualcointype ;
	}
	
	/**
	 * 
	 *  作者：           Dylan
	 *  标题：           findFvirtualaddress 
	 *  时间：           2018年8月14日
	 *  描述：           根据当前用户和币种类型获取一个用户币种地址
	 *  
	 *  @param fuser 当前登录用户
	 *  @param fvirtualcointype 币种类型
	 *  @return Fvirtualaddress 用户币种地址表
	 */
	public Fvirtualaddress findFvirtualaddress(Fuser fuser,Fvirtualcointype fvirtualcointype){
		return this.fvirtualaddressDAO.findFvirtualaddress(fuser, fvirtualcointype) ;
	}
	
	public List<Fvirtualaddress> findFvirtualaddress(Fvirtualcointype fvirtualcointype,String address){
		return this.fvirtualaddressDAO.findFvirtualaddress(fvirtualcointype, address) ;
	}
	
	public FvirtualaddressWithdraw findFvirtualaddressWithdraw(String fid){
		return this.fvirtualaddressWithdrawDAO.findById(fid);
	}
	
	public List<FvirtualaddressWithdraw> findFvirtualaddressWithdraws(int firstResult, int maxResults,
			String filter,boolean isFY) {
		return this.fvirtualaddressWithdrawDAO.list(firstResult, maxResults, filter,isFY);
	}
	
	public List<Fvirtualcaptualoperation> findFvirtualcaptualoperation(int firstResult, int maxResults,
			String filter,boolean isFY){
		List<Fvirtualcaptualoperation> list = this.fvirtualcaptualoperationDAO.list(firstResult, maxResults, filter,isFY) ;
		for (Fvirtualcaptualoperation fvirtualcaptualoperation : list) {
			fvirtualcaptualoperation.getFvirtualcointype().getFname() ;
		}
		return list ;
	}
	public int findFvirtualcaptualoperationCount(
			Fuser fuser,int type[],int status[],Fvirtualcointype[] fvirtualcointypes,String order){
		return this.fvirtualcaptualoperationDAO.findFvirtualcaptualoperationCount(fuser, type, status, fvirtualcointypes, order) ;
	}
	
	public List<Fvirtualcaptualoperation> findFvirtualcaptualoperations(int firstResult, int maxResults,String filter, boolean isFY){
		return this.fvirtualcaptualoperationDAO.findByParam(firstResult, maxResults, filter, isFY, "Fvirtualcaptualoperation") ;
	}
	public int findFvirtualcaptualoperationsCount(String filter){
		return this.fvirtualcaptualoperationDAO.findByParamCount(filter, "Fvirtualcaptualoperation") ;
	}
	
	public void updateFvirtualaddressWithdraw(FvirtualaddressWithdraw fvirtualaddressWithdraw){
		this.fvirtualaddressWithdrawDAO.save(fvirtualaddressWithdraw) ;
	}
	
	public void updateDelFvirtualaddressWithdraw(FvirtualaddressWithdraw fvirtualaddressWithdraw){
		this.fvirtualaddressWithdrawDAO.delete(fvirtualaddressWithdraw) ;
	}
	
	public Ffees findFfees(String virtualCoinTypeId,int level){
		return this.ffeesDAO.findFfee(virtualCoinTypeId, level) ;
	}
	
	public void updateWithdrawBtc(FvirtualaddressWithdraw fvirtualaddressWithdraw,Fvirtualcointype fvirtualcointype,Fvirtualwallet fvirtualwallet ,double withdrawAmount,Fuser fuser){
		try {
			fvirtualwallet.setFtotal(fvirtualwallet.getFtotal()-withdrawAmount) ;
			fvirtualwallet.setFfrozen(fvirtualwallet.getFfrozen()+withdrawAmount) ;
			fvirtualwallet.setFlastUpdateTime(Utils.getTimestamp()) ;
			this.fvirtualwalletDAO.attachDirty(fvirtualwallet) ;
			
			double feeRate = this.ffeesDAO.findFfee(fvirtualcointype.getFid(), fuser.getFscore().getFlevel()).getWithdraw();
			Fvirtualcaptualoperation fvirtualcaptualoperation = new Fvirtualcaptualoperation() ;
			fvirtualcaptualoperation.setFamount(withdrawAmount*(1-feeRate)) ;
			fvirtualcaptualoperation.setFcreateTime(Utils.getTimestamp()) ;
			fvirtualcaptualoperation.setFfees(withdrawAmount*feeRate) ;
			fvirtualcaptualoperation.setFlastUpdateTime(Utils.getTimestamp()) ;
			fvirtualcaptualoperation.setFstatus(VirtualCapitalOperationOutStatusEnum.WaitForOperation) ;
			fvirtualcaptualoperation.setFtype(VirtualCapitalOperationTypeEnum.COIN_OUT) ;
			fvirtualcaptualoperation.setFuser(fuser) ;
			fvirtualcaptualoperation.setFvirtualcointype(fvirtualcointype) ;
			fvirtualcaptualoperation.setWithdraw_virtual_address(fvirtualaddressWithdraw.getFadderess()) ;
			this.fvirtualcaptualoperationDAO.save(fvirtualcaptualoperation) ;
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
	
	public void addFvirtualcaptualoperation(Fvirtualcaptualoperation fvirtualcaptualoperation){
		this.fvirtualcaptualoperationDAO.save(fvirtualcaptualoperation) ;
	}
	
	public List<Fvirtualcaptualoperation> findFvirtualcaptualoperationByProperty(String key,Object value){
		return this.fvirtualcaptualoperationDAO.findByProperty(key, value) ;
	}
	
	public Fvirtualcaptualoperation findFvirtualcaptualoperationById(String id){
		return this.fvirtualcaptualoperationDAO.findById(id) ;
	}
	
	//比特币自动充值并加币
	public void updateFvirtualcaptualoperationCoinIn(Fvirtualcaptualoperation fvirtualcaptualoperation) throws Exception{
		try {
			Fvirtualcaptualoperation real = this.fvirtualcaptualoperationDAO.findById(fvirtualcaptualoperation.getFid()) ;
			if(real!=null && real.getFstatus()!=VirtualCapitalOperationInStatusEnum.SUCCESS){
				real.setFstatus(fvirtualcaptualoperation.getFstatus()) ;
				real.setFconfirmations(fvirtualcaptualoperation.getFconfirmations()) ;
				real.setFlastUpdateTime(Utils.getTimestamp()) ;
				real.setFamount(fvirtualcaptualoperation.getFamount());
				this.fvirtualcaptualoperationDAO.attachDirty(real) ;
				
				if(real.getFstatus()==VirtualCapitalOperationInStatusEnum.SUCCESS && real.isFhasOwner()){
					Fvirtualcointype fvirtualcointype = this.fvirtualcointypeDAO.findById(real.getFvirtualcointype().getFid()) ;
					Fvirtualwallet fvirtualwallet = this.fvirtualwalletDAO.findVirtualWallet(real.getFuser().getFid(), fvirtualcointype.getFid()) ;
					fvirtualwallet.setFtotal(fvirtualwallet.getFtotal()+ real.getFamount()) ;
					fvirtualwallet.setFlastUpdateTime(Utils.getTimestamp()) ;
					this.fvirtualwalletDAO.attachDirty(fvirtualwallet) ;
				}
			}
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
	
	public List<Fvirtualaddress> findFvirtualaddressByProperty(String key,Object value){
		List<Fvirtualaddress> fvirtualaddresses = this.fvirtualaddressDAO.findByProperty(key, value) ;
		for (Fvirtualaddress fvirtualaddress : fvirtualaddresses) {
			fvirtualaddress.getFuser().getFnickName() ;
		}
		return fvirtualaddresses ;
	}
	
	public boolean isExistsCanWithdrawCoinType(){
		List<Fvirtualcointype> fvirtualcointypes = this.fvirtualcointypeDAO.findByParam(0, 0, " where FIsWithDraw=1 and fstatus=1 ", false, "Fvirtualcointype") ;
		return fvirtualcointypes.size()>0 ;
	}

	public FvirtualaddressWithdraw findByAddAndUid(String fid, String withdrawAddr) {
		FvirtualaddressWithdraw findByAddAndUid = this.fvirtualaddressWithdrawDAO.findByAddAndUid(fid, withdrawAddr);
		return findByAddAndUid;
	}
	public List<Fvirtualcointype> findByParam(int firstResult, int maxResults){
		List<Fvirtualcointype> findByParam = this.fvirtualcointypeDAO.findByParam(firstResult, maxResults);
		return findByParam;
	}
	/**
	 * 
	 *  作者：           Dylan
	 *  标题：           findByParam 
	 *  时间：           2018年8月14日
	 *  描述：           
	 *  
	 *  @param firstResult 起始页
	 *  @param maxResults  最大显示数量
	 *  @param filter 查询条件
	 *  @return List<Fvirtualcointype>
	 */
	public List<Fvirtualcointype> findByParam(int firstResult, int maxResults, String filter){
		List<Fvirtualcointype> findByParam = this.fvirtualcointypeDAO.findByParam(firstResult, maxResults, filter);
		return findByParam;
	}

	public int findCount(String sql) {
		int count = this.fvirtualcointypeDAO.findCount(sql);
		return count;
	}
	
	public List<Fvirtualcointype> findByParam1(int firstResult, int maxResults,String filter, boolean isFY, String CLASS){
		List<Fvirtualcointype> findByParam = this.fvirtualcointypeDAO.findByParam(firstResult, maxResults, filter, isFY, CLASS);
		return findByParam;
	}
	
}
