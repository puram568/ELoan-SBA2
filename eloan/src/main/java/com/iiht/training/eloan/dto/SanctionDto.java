package com.iiht.training.eloan.dto;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class SanctionDto {
	
	@NotNull(message="Loan Amount Sanctioned is not null")
	@DecimalMin("1.0")
	private Double loanAmountSanctioned;
	
	@NotNull(message="Loan Term is not null")
	@DecimalMin("1.0")
	private Double termOfLoan;
	
	@NotNull(message="Loan paymentStartDate  is not null")
    @NotBlank(message = "Loan paymentStartDate is mandatory")
	@Pattern(regexp = "[0-9]{2}/[0-9]{2}/[0-9]{4}")
	private String paymentStartDate;
	
	public Double getLoanAmountSanctioned() {
		return loanAmountSanctioned;
	}
	public void setLoanAmountSanctioned(Double loanAmountSanctioned) {
		this.loanAmountSanctioned = loanAmountSanctioned;
	}
	public Double getTermOfLoan() {
		return termOfLoan;
	}
	public void setTermOfLoan(Double termOfLoan) {
		this.termOfLoan = termOfLoan;
	}
	public String getPaymentStartDate() {
		return paymentStartDate;
	}
	public void setPaymentStartDate(String paymentStartDate) {
		this.paymentStartDate = paymentStartDate;
	}
	
	
}
