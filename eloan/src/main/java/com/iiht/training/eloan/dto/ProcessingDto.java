package com.iiht.training.eloan.dto;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ProcessingDto {
	
	@NotNull(message="Acres of Land is not Null")
	@DecimalMin("1.0")
	private Double acresOfLand;
	
	@NotNull(message="Land Value is not Null")
	@DecimalMin("1.0")
	private Double landValue;
	
	@NotNull(message="appraisedBy is not Null")
	@NotBlank(message="Enter appraisedBy Group Details")
	private String appraisedBy;
	
	@NotNull(message="valuationDate is not Null")
	@NotBlank(message="Enter valuation Date")
	private String valuationDate;
	
	@NotNull(message="addressOfProperty is not Null")
    @Size(min=3,max=150,message = "Address must be 3 to 150 characters length")
	private String addressOfProperty;
	
	@NotNull(message="suggestedAmountOfLoan is not Null")
	@DecimalMin("1.0")
	private Double suggestedAmountOfLoan;
	
	public Double getAcresOfLand() {
		return acresOfLand;
	}
	public void setAcresOfLand(Double acresOfLand) {
		this.acresOfLand = acresOfLand;
	}
	public Double getLandValue() {
		return landValue;
	}
	public void setLandValue(Double landValue) {
		this.landValue = landValue;
	}
	public String getAppraisedBy() {
		return appraisedBy;
	}
	public void setAppraisedBy(String appraisedBy) {
		this.appraisedBy = appraisedBy;
	}
	public String getValuationDate() {
		return valuationDate;
	}
	public void setValuationDate(String valuationDate) {
		this.valuationDate = valuationDate;
	}
	public String getAddressOfProperty() {
		return addressOfProperty;
	}
	public void setAddressOfProperty(String addressOfProperty) {
		this.addressOfProperty = addressOfProperty;
	}
	public Double getSuggestedAmountOfLoan() {
		return suggestedAmountOfLoan;
	}
	public void setSuggestedAmountOfLoan(Double suggestedAmountOfLoan) {
		this.suggestedAmountOfLoan = suggestedAmountOfLoan;
	}
	
	
}
