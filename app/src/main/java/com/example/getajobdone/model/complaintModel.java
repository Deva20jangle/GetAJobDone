package com.example.getajobdone.model;

public class complaintModel {

    String complaintId, customerName, customerContactNo, SPName, SPContactNo, complaintDesc, resolvedFlag, resolvedDesc;

    public complaintModel() {
    }

    public complaintModel(String complaintId, String customerName, String customerContactNo, String SPName, String SPContactNo, String complaintDesc, String resolvedFlag, String resolvedDesc) {
        this.complaintId = complaintId;
        this.customerName = customerName;
        this.customerContactNo = customerContactNo;
        this.SPName = SPName;
        this.SPContactNo = SPContactNo;
        this.complaintDesc = complaintDesc;
        this.resolvedFlag = resolvedFlag;
        this.resolvedDesc = resolvedDesc;
    }

    public String getComplaintId() {
        return complaintId;
    }

    public void setComplaintId(String complaintId) {
        this.complaintId = complaintId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerContactNo() {
        return customerContactNo;
    }

    public void setCustomerContactNo(String customerContactNo) {
        this.customerContactNo = customerContactNo;
    }

    public String getSPName() {
        return SPName;
    }

    public void setSPName(String SPName) {
        this.SPName = SPName;
    }

    public String getSPContactNo() {
        return SPContactNo;
    }

    public void setSPContactNo(String SPContactNo) {
        this.SPContactNo = SPContactNo;
    }

    public String getComplaintDesc() {
        return complaintDesc;
    }

    public void setComplaintDesc(String complaintDesc) {
        this.complaintDesc = complaintDesc;
    }

    public String getResolvedFlag() {
        return resolvedFlag;
    }

    public void setResolvedFlag(String resolvedFlag) {
        this.resolvedFlag = resolvedFlag;
    }

    public String getResolvedDesc() {
        return resolvedDesc;
    }

    public void setResolvedDesc(String resolvedDesc) {
        this.resolvedDesc = resolvedDesc;
    }
}
