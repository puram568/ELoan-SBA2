package com.iiht.training.eloan.util;

import com.iiht.training.eloan.dto.LoanDto;
import com.iiht.training.eloan.dto.LoanOutputDto;
import com.iiht.training.eloan.dto.ProcessingDto;
import com.iiht.training.eloan.dto.SanctionOutputDto;
import com.iiht.training.eloan.dto.UserDto;
import com.iiht.training.eloan.entity.Loan;
import com.iiht.training.eloan.entity.ProcessingInfo;
import com.iiht.training.eloan.entity.SanctionInfo;
import com.iiht.training.eloan.entity.Users;

public class EMParser {

	public static UserDto parse(Users source) {
		UserDto target = new UserDto();
		target.setId(source.getId());
		target.setFirstName(source.getFirstName());
		target.setLastName(source.getLastName());
		target.setEmail(source.getEmail());
		target.setMobile(source.getMobile());
		return target;
	}

	public static Users parse(UserDto source) {
		Users target = new Users();
		target.setId(source.getId());
		target.setFirstName(source.getFirstName());
		target.setLastName(source.getLastName());
		target.setEmail(source.getEmail());
		target.setMobile(source.getMobile());
		target.setRole("Customer");
		return target;
	}

	public static LoanDto parse(Loan source) {
		LoanDto target = new LoanDto();
		target.setLoanName(source.getLoanName());
		target.setLoanApplicationDate(source.getLoanApplicationDate());
		target.setLoanAmount(source.getLoanAmount());
		target.setBillingIndicator(source.getBillingIndicator());
		target.setBusinessStructure(source.getBusinessStructure());
		target.setTaxIndicator(source.getTaxIndicator());
		return target;
	}

	public static Loan parse(LoanDto source) {
		Loan target = new Loan();
		target.setLoanName(source.getLoanName());
		target.setLoanApplicationDate(source.getLoanApplicationDate());
		target.setLoanAmount(source.getLoanAmount());
		target.setBillingIndicator(source.getBillingIndicator());
		target.setBusinessStructure(source.getBusinessStructure());
		target.setTaxIndicator(source.getTaxIndicator());
		return target;
	}

	public static Loan parse(LoanDto source, Long customerId) {
		Loan target = new Loan();
		target.setCustomerId(customerId);
		target.setStatus(0);
		target.setRemark("Applied");
		target.setLoanName(source.getLoanName());
		target.setLoanApplicationDate(source.getLoanApplicationDate());
		target.setLoanAmount(source.getLoanAmount());
		target.setBillingIndicator(source.getBillingIndicator());
		target.setBusinessStructure(source.getBusinessStructure());
		target.setTaxIndicator(source.getTaxIndicator());
		return target;
	}

	public static LoanOutputDto parselod(Loan source) {
		LoanOutputDto target = new LoanOutputDto();
		LoanDto loanDto=new LoanDto();
		target.setLoanAppId(source.getId());
		target.setCustomerId(source.getCustomerId());
		target.setStatus(source.getStatus().toString());
		target.setRemark(source.getRemark());
		return target;
	}

	public static Loan parselod(LoanOutputDto source) {
		Loan target = new Loan();
		target.setId(source.getLoanAppId());
		target.setCustomerId(source.getCustomerId());
		return target;
	}
	
	public static ProcessingDto parse(ProcessingInfo source) {
		ProcessingDto target = new ProcessingDto();
		target.setAcresOfLand(source.getAcresOfLand());
		target.setAddressOfProperty(source.getAddressOfProperty());
		target.setAppraisedBy(source.getAppraisedBy());
		target.setLandValue(source.getLandValue());
		target.setSuggestedAmountOfLoan(source.getSuggestedAmountOfLoan());
		target.setValuationDate(source.getValuationDate());
		return target;
	}
	
	public static ProcessingInfo parse(ProcessingDto source,Long clerkId,Long loanAppId) {
		ProcessingInfo target = new ProcessingInfo();
		target.setAcresOfLand(source.getAcresOfLand());
		target.setAddressOfProperty(source.getAddressOfProperty());
		target.setAppraisedBy(source.getAppraisedBy());
		target.setLandValue(source.getLandValue());
		target.setSuggestedAmountOfLoan(source.getSuggestedAmountOfLoan());
		target.setValuationDate(source.getValuationDate());
		target.setLoanAppId(loanAppId);
		target.setLoanClerkId(clerkId);
		return target;
	}

	public static SanctionInfo parse(SanctionOutputDto source, Long managerId, Long loanAppId) {
		SanctionInfo target=new SanctionInfo();
		target.setLoanAmountSanctioned(source.getLoanAmountSanctioned());
		target.setLoanAppId(loanAppId);
		target.setLoanClosureDate(source.getLoanClosureDate());
		target.setManagerId(managerId);
		target.setMonthlyPayment(source.getMonthlyPayment());
		target.setPaymentStartDate(source.getPaymentStartDate());
		target.setTermOfLoan(source.getTermOfLoan());
		return target;
	}
	
	public static SanctionOutputDto parse(SanctionInfo source) {
		SanctionOutputDto target=new SanctionOutputDto();
		target.setLoanAmountSanctioned(source.getLoanAmountSanctioned());
		target.setLoanClosureDate(source.getLoanClosureDate());
		target.setMonthlyPayment(source.getMonthlyPayment());
		target.setPaymentStartDate(source.getPaymentStartDate());
		target.setTermOfLoan(source.getTermOfLoan());
		return target;
	}
	
}
