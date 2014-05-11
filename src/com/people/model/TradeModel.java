package com.people.model;

import java.io.Serializable;

public class TradeModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String sysDate;
	private String merName;
	private String logDate;
	private String logNo;
	private String txncd;
	private String txnsts;
	private String amount;
	private String cardNo;
	private String txnamt;
	

	private String crdNo;

	public String getSysDate() {
		return sysDate;
	}

	public void setSysDate(String sysDate) {
		this.sysDate = sysDate;
	}

	public String getMerName() {
		return merName;
	}

	public void setMerName(String merName) {
		this.merName = merName;
	}

	public String getLogDate() {
		return logDate;
	}

	public void setLogDate(String logDate) {
		this.logDate = logDate;
	}

	public String getLogNo() {
		return logNo;
	}

	public void setLogNo(String logNo) {
		this.logNo = logNo;
	}

	public String getTxncd() {
		return txncd;
	}

	public void setTxncd(String txncd) {
		this.txncd = txncd;
	}

	public String getTxnsts() {
		return txnsts;
	}

	public void setTxnsts(String txnsts) {
		this.txnsts = txnsts;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getTxnamt() {
		return txnamt;
	}

	public void setTxnamt(String txnamt) {
		this.txnamt = txnamt;
	}

	public String getCrdNo() {
		return crdNo;
	}

	public void setCrdNo(String crdNo) {
		this.crdNo = crdNo;
	}
}
