package com.people.model;

import java.io.Serializable;


public class WaterModel  implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    private String payAmount0;  // 查询输入金额
    private String isPrePay;
    private String account;
    private String customerName; // 客户名称
    private String twqtcId; // 应缴费流水号
    private String payAmount; // 应缴费金额
    ;
    private String balanceAmount; //上次余额
    private String leePayAmount; //滞纳金
    private String beginDate;
    private String endDate;
    private String billDate;// 
    private String typeCode; //缴费类型码 00：水，01：电，02:煤
    private String typeName; //缴费类型名称
    private String extTypeCode; //缴费子类型码
    private String extTypeName;//失败原因
    private String bak; // 失败原因
    private String failInfo;// 失败原因
	public String getPayAmount0() {
		return payAmount0;
	}
	public void setPayAmount0(String payAmount0) {
		this.payAmount0 = payAmount0;
	}
	public String getIsPrePay() {
		return isPrePay;
	}
	public void setIsPrePay(String isPrePay) {
		this.isPrePay = isPrePay;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getTwqtcId() {
		return twqtcId;
	}
	public void setTwqtcId(String twqtcId) {
		this.twqtcId = twqtcId;
	}
	public String getPayAmount() {
		return payAmount;
	}
	public void setPayAmount(String payAmount) {
		this.payAmount = payAmount;
	}
	public String getBalanceAmount() {
		return balanceAmount;
	}
	public void setBalanceAmount(String balanceAmount) {
		this.balanceAmount = balanceAmount;
	}
	public String getLeePayAmount() {
		return leePayAmount;
	}
	public void setLeePayAmount(String leePayAmount) {
		this.leePayAmount = leePayAmount;
	}
	public String getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getBillDate() {
		return billDate;
	}
	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}
	public String getTypeCode() {
		return typeCode;
	}
	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getExtTypeCode() {
		return extTypeCode;
	}
	public void setExtTypeCode(String extTypeCode) {
		this.extTypeCode = extTypeCode;
	}
	public String getExtTypeName() {
		return extTypeName;
	}
	public void setExtTypeName(String extTypeName) {
		this.extTypeName = extTypeName;
	}
	public String getBak() {
		return bak;
	}
	public void setBak(String bak) {
		this.bak = bak;
	}
	public String getFailInfo() {
		return failInfo;
	}
	public void setFailInfo(String failInfo) {
		this.failInfo = failInfo;
	}
    
    
    

   
   
    
}