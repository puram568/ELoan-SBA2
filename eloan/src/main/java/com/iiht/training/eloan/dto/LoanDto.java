package com.iiht.training.eloan.dto;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class LoanDto {

	@NotNull(message="Loan Name is not Null")
    @Size(min=3,max=100,message = "Loan Name must be 3 to 100 characters length")
	private String loanName;
	@NotNull(message="Loan Amount is not null")
	@DecimalMin("1.0")
	private Double loanAmount;
	@NotNull(message="Loan Application Date is not null")
    @NotBlank(message = "Loan Application Date is mandatory")
	@Pattern(regexp = "[0-9]{2}/[0-9]{2}/[0-9]{4}")
	private String loanApplicationDate;
	@NotNull(message="Business Structure is not null")
    @NotBlank(message = "Business Structure  is mandatory")
	private String businessStructure;
	@NotNull(message="Billing Indicator is not null")
    @NotBlank(message = "Billing Indicator is mandatory")
	private String billingIndicator;
	@NotNull(message="TaxIndicator is not null")
    @NotBlank(message = "TaxIndicator  is mandatory")
	private String taxIndicator;
	
	public String getLoanName() {
		return loanName;
	}
	public void setLoanName(String loanName) {
		this.loanName = loanName;
	}
	public Double getLoanAmount() {
		return loanAmount;
	}
	public void setLoanAmount(Double loanAmount) {
		this.loanAmount = loanAmount;
	}
	public String getLoanApplicationDate() {
		return loanApplicationDate;
	}
	public void setLoanApplicationDate(String loanApplicationDate) {
		this.loanApplicationDate = loanApplicationDate;
	}
	public String getBusinessStructure() {
		return businessStructure;
	}
	public void setBusinessStructure(String businessStructure) {
		this.businessStructure = businessStructure;
	}
	public String getBillingIndicator() {
		return billingIndicator;
	}
	public void setBillingIndicator(String billingIndicator) {
		this.billingIndicator = billingIndicator;
	}
	public String getTaxIndicator() {
		return taxIndicator;
	}
	public void setTaxIndicator(String taxIndicator) {
		this.taxIndicator = taxIndicator;
	}
	
	
}
