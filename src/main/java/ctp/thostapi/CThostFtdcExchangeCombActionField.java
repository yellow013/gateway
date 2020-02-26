/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 2.0.11
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package ctp.thostapi;

public class CThostFtdcExchangeCombActionField {
	private long swigCPtr;
	protected boolean swigCMemOwn;

	protected CThostFtdcExchangeCombActionField(long cPtr, boolean cMemoryOwn) {
		swigCMemOwn = cMemoryOwn;
		swigCPtr = cPtr;
	}

	protected static long getCPtr(CThostFtdcExchangeCombActionField obj) {
		return (obj == null) ? 0 : obj.swigCPtr;
	}

	protected void finalize() {
		delete();
	}

	public synchronized void delete() {
		if (swigCPtr != 0) {
			if (swigCMemOwn) {
				swigCMemOwn = false;
				thosttraderapiJNI.delete_CThostFtdcExchangeCombActionField(swigCPtr);
			}
			swigCPtr = 0;
		}
	}

	public void setDirection(char value) {
		thosttraderapiJNI.CThostFtdcExchangeCombActionField_Direction_set(swigCPtr, this, value);
	}

	public char getDirection() {
		return thosttraderapiJNI.CThostFtdcExchangeCombActionField_Direction_get(swigCPtr, this);
	}

	public void setVolume(int value) {
		thosttraderapiJNI.CThostFtdcExchangeCombActionField_Volume_set(swigCPtr, this, value);
	}

	public int getVolume() {
		return thosttraderapiJNI.CThostFtdcExchangeCombActionField_Volume_get(swigCPtr, this);
	}

	public void setCombDirection(char value) {
		thosttraderapiJNI.CThostFtdcExchangeCombActionField_CombDirection_set(swigCPtr, this, value);
	}

	public char getCombDirection() {
		return thosttraderapiJNI.CThostFtdcExchangeCombActionField_CombDirection_get(swigCPtr, this);
	}

	public void setHedgeFlag(char value) {
		thosttraderapiJNI.CThostFtdcExchangeCombActionField_HedgeFlag_set(swigCPtr, this, value);
	}

	public char getHedgeFlag() {
		return thosttraderapiJNI.CThostFtdcExchangeCombActionField_HedgeFlag_get(swigCPtr, this);
	}

	public void setActionLocalID(String value) {
		thosttraderapiJNI.CThostFtdcExchangeCombActionField_ActionLocalID_set(swigCPtr, this, value);
	}

	public String getActionLocalID() {
		return thosttraderapiJNI.CThostFtdcExchangeCombActionField_ActionLocalID_get(swigCPtr, this);
	}

	public void setExchangeID(String value) {
		thosttraderapiJNI.CThostFtdcExchangeCombActionField_ExchangeID_set(swigCPtr, this, value);
	}

	public String getExchangeID() {
		return thosttraderapiJNI.CThostFtdcExchangeCombActionField_ExchangeID_get(swigCPtr, this);
	}

	public void setParticipantID(String value) {
		thosttraderapiJNI.CThostFtdcExchangeCombActionField_ParticipantID_set(swigCPtr, this, value);
	}

	public String getParticipantID() {
		return thosttraderapiJNI.CThostFtdcExchangeCombActionField_ParticipantID_get(swigCPtr, this);
	}

	public void setClientID(String value) {
		thosttraderapiJNI.CThostFtdcExchangeCombActionField_ClientID_set(swigCPtr, this, value);
	}

	public String getClientID() {
		return thosttraderapiJNI.CThostFtdcExchangeCombActionField_ClientID_get(swigCPtr, this);
	}

	public void setExchangeInstID(String value) {
		thosttraderapiJNI.CThostFtdcExchangeCombActionField_ExchangeInstID_set(swigCPtr, this, value);
	}

	public String getExchangeInstID() {
		return thosttraderapiJNI.CThostFtdcExchangeCombActionField_ExchangeInstID_get(swigCPtr, this);
	}

	public void setTraderID(String value) {
		thosttraderapiJNI.CThostFtdcExchangeCombActionField_TraderID_set(swigCPtr, this, value);
	}

	public String getTraderID() {
		return thosttraderapiJNI.CThostFtdcExchangeCombActionField_TraderID_get(swigCPtr, this);
	}

	public void setInstallID(int value) {
		thosttraderapiJNI.CThostFtdcExchangeCombActionField_InstallID_set(swigCPtr, this, value);
	}

	public int getInstallID() {
		return thosttraderapiJNI.CThostFtdcExchangeCombActionField_InstallID_get(swigCPtr, this);
	}

	public void setActionStatus(char value) {
		thosttraderapiJNI.CThostFtdcExchangeCombActionField_ActionStatus_set(swigCPtr, this, value);
	}

	public char getActionStatus() {
		return thosttraderapiJNI.CThostFtdcExchangeCombActionField_ActionStatus_get(swigCPtr, this);
	}

	public void setNotifySequence(int value) {
		thosttraderapiJNI.CThostFtdcExchangeCombActionField_NotifySequence_set(swigCPtr, this, value);
	}

	public int getNotifySequence() {
		return thosttraderapiJNI.CThostFtdcExchangeCombActionField_NotifySequence_get(swigCPtr, this);
	}

	public void setTradingDay(String value) {
		thosttraderapiJNI.CThostFtdcExchangeCombActionField_TradingDay_set(swigCPtr, this, value);
	}

	public String getTradingDay() {
		return thosttraderapiJNI.CThostFtdcExchangeCombActionField_TradingDay_get(swigCPtr, this);
	}

	public void setSettlementID(int value) {
		thosttraderapiJNI.CThostFtdcExchangeCombActionField_SettlementID_set(swigCPtr, this, value);
	}

	public int getSettlementID() {
		return thosttraderapiJNI.CThostFtdcExchangeCombActionField_SettlementID_get(swigCPtr, this);
	}

	public void setSequenceNo(int value) {
		thosttraderapiJNI.CThostFtdcExchangeCombActionField_SequenceNo_set(swigCPtr, this, value);
	}

	public int getSequenceNo() {
		return thosttraderapiJNI.CThostFtdcExchangeCombActionField_SequenceNo_get(swigCPtr, this);
	}

	public void setIPAddress(String value) {
		thosttraderapiJNI.CThostFtdcExchangeCombActionField_IPAddress_set(swigCPtr, this, value);
	}

	public String getIPAddress() {
		return thosttraderapiJNI.CThostFtdcExchangeCombActionField_IPAddress_get(swigCPtr, this);
	}

	public void setMacAddress(String value) {
		thosttraderapiJNI.CThostFtdcExchangeCombActionField_MacAddress_set(swigCPtr, this, value);
	}

	public String getMacAddress() {
		return thosttraderapiJNI.CThostFtdcExchangeCombActionField_MacAddress_get(swigCPtr, this);
	}

	public void setComTradeID(String value) {
		thosttraderapiJNI.CThostFtdcExchangeCombActionField_ComTradeID_set(swigCPtr, this, value);
	}

	public String getComTradeID() {
		return thosttraderapiJNI.CThostFtdcExchangeCombActionField_ComTradeID_get(swigCPtr, this);
	}

	public void setBranchID(String value) {
		thosttraderapiJNI.CThostFtdcExchangeCombActionField_BranchID_set(swigCPtr, this, value);
	}

	public String getBranchID() {
		return thosttraderapiJNI.CThostFtdcExchangeCombActionField_BranchID_get(swigCPtr, this);
	}

	public CThostFtdcExchangeCombActionField() {
		this(thosttraderapiJNI.new_CThostFtdcExchangeCombActionField(), true);
	}

}
