/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 2.0.11
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package ctp.thostapi;

public class CThostFtdcVerifyFuturePasswordAndCustInfoField {
	private long swigCPtr;
	protected boolean swigCMemOwn;

	protected CThostFtdcVerifyFuturePasswordAndCustInfoField(long cPtr, boolean cMemoryOwn) {
		swigCMemOwn = cMemoryOwn;
		swigCPtr = cPtr;
	}

	protected static long getCPtr(CThostFtdcVerifyFuturePasswordAndCustInfoField obj) {
		return (obj == null) ? 0 : obj.swigCPtr;
	}

	protected void finalize() {
		delete();
	}

	public synchronized void delete() {
		if (swigCPtr != 0) {
			if (swigCMemOwn) {
				swigCMemOwn = false;
				thosttraderapiJNI.delete_CThostFtdcVerifyFuturePasswordAndCustInfoField(swigCPtr);
			}
			swigCPtr = 0;
		}
	}

	public void setCustomerName(String value) {
		thosttraderapiJNI.CThostFtdcVerifyFuturePasswordAndCustInfoField_CustomerName_set(swigCPtr, this, value);
	}

	public String getCustomerName() {
		return thosttraderapiJNI.CThostFtdcVerifyFuturePasswordAndCustInfoField_CustomerName_get(swigCPtr, this);
	}

	public void setIdCardType(char value) {
		thosttraderapiJNI.CThostFtdcVerifyFuturePasswordAndCustInfoField_IdCardType_set(swigCPtr, this, value);
	}

	public char getIdCardType() {
		return thosttraderapiJNI.CThostFtdcVerifyFuturePasswordAndCustInfoField_IdCardType_get(swigCPtr, this);
	}

	public void setIdentifiedCardNo(String value) {
		thosttraderapiJNI.CThostFtdcVerifyFuturePasswordAndCustInfoField_IdentifiedCardNo_set(swigCPtr, this, value);
	}

	public String getIdentifiedCardNo() {
		return thosttraderapiJNI.CThostFtdcVerifyFuturePasswordAndCustInfoField_IdentifiedCardNo_get(swigCPtr, this);
	}

	public void setCustType(char value) {
		thosttraderapiJNI.CThostFtdcVerifyFuturePasswordAndCustInfoField_CustType_set(swigCPtr, this, value);
	}

	public char getCustType() {
		return thosttraderapiJNI.CThostFtdcVerifyFuturePasswordAndCustInfoField_CustType_get(swigCPtr, this);
	}

	public void setAccountID(String value) {
		thosttraderapiJNI.CThostFtdcVerifyFuturePasswordAndCustInfoField_AccountID_set(swigCPtr, this, value);
	}

	public String getAccountID() {
		return thosttraderapiJNI.CThostFtdcVerifyFuturePasswordAndCustInfoField_AccountID_get(swigCPtr, this);
	}

	public void setPassword(String value) {
		thosttraderapiJNI.CThostFtdcVerifyFuturePasswordAndCustInfoField_Password_set(swigCPtr, this, value);
	}

	public String getPassword() {
		return thosttraderapiJNI.CThostFtdcVerifyFuturePasswordAndCustInfoField_Password_get(swigCPtr, this);
	}

	public void setCurrencyID(String value) {
		thosttraderapiJNI.CThostFtdcVerifyFuturePasswordAndCustInfoField_CurrencyID_set(swigCPtr, this, value);
	}

	public String getCurrencyID() {
		return thosttraderapiJNI.CThostFtdcVerifyFuturePasswordAndCustInfoField_CurrencyID_get(swigCPtr, this);
	}

	public void setLongCustomerName(String value) {
		thosttraderapiJNI.CThostFtdcVerifyFuturePasswordAndCustInfoField_LongCustomerName_set(swigCPtr, this, value);
	}

	public String getLongCustomerName() {
		return thosttraderapiJNI.CThostFtdcVerifyFuturePasswordAndCustInfoField_LongCustomerName_get(swigCPtr, this);
	}

	public CThostFtdcVerifyFuturePasswordAndCustInfoField() {
		this(thosttraderapiJNI.new_CThostFtdcVerifyFuturePasswordAndCustInfoField(), true);
	}

}
