package com.iiht.training.eloan.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iiht.training.eloan.dto.LoanDto;
import com.iiht.training.eloan.dto.LoanOutputDto;
import com.iiht.training.eloan.dto.RejectDto;
import com.iiht.training.eloan.dto.SanctionDto;
import com.iiht.training.eloan.dto.SanctionOutputDto;
import com.iiht.training.eloan.exception.AlreadyFinalizedException;
import com.iiht.training.eloan.exception.InvalidDataException;
import com.iiht.training.eloan.exception.LoanNotFoundException;
import com.iiht.training.eloan.exception.ManagerNotFoundException;
import com.iiht.training.eloan.repository.LoanRepository;
import com.iiht.training.eloan.repository.ProcessingInfoRepository;
import com.iiht.training.eloan.repository.SanctionInfoRepository;
import com.iiht.training.eloan.repository.UsersRepository;
import com.iiht.training.eloan.service.ManagerService;
import com.iiht.training.eloan.util.EMParser;

@Service
public class ManagerServiceImpl implements ManagerService {

	@Autowired
	private UsersRepository usersRepository;
	
	@Autowired
	private LoanRepository loanRepository;
	
	@Autowired
	private ProcessingInfoRepository processingInfoRepository;
	
	@Autowired
	private SanctionInfoRepository sanctionInfoRepository;
	
	@Override
	public List<LoanOutputDto> allProcessedLoans() {
		List<LoanOutputDto> loanOutputDto = new ArrayList<LoanOutputDto>();
		List<LoanDto> loanDto=new ArrayList<LoanDto>();
		List<Long> loanAppId = loanRepository.findByStatus(1L);
		List<Long> customerId = loanRepository.findCustomerByStatus(1L);
		loanDto.addAll(loanRepository.findAllByStatus(1).stream().map(e->EMParser.parse(e)).collect(Collectors.toList()));
		
		for (int i = 0; i < loanDto.size(); i++) {
			LoanOutputDto loanOD = new LoanOutputDto();
			loanOD.setCustomerId(customerId.get(i));
			loanOD.setLoanAppId(loanAppId.get(i));
			loanOD.setLoanDto(loanDto.get(i));
			loanOD.setUserDto(usersRepository.findById(customerId.get(i)).stream().map(e -> EMParser.parse(e)).collect(Collectors.toList())
				.get(0));
			loanOD.setProcessingDto(processingInfoRepository.findByLoanAppId(loanAppId.get(i)).stream().map(e->EMParser.parse(e)).collect(Collectors.toList()).get(0));
			loanOD.setRemark(loanRepository.getRemark(loanAppId.get(i)));
			
			switch (loanRepository.getStatus(loanAppId.get(i))) {
			case 0:loanOD.setStatus("Applied");break;
			case 1:loanOD.setStatus("Processed");break;
			case 2:loanOD.setStatus("Sanctioned");break;
			case -1:loanOD.setStatus("Rejected");break;
			default:break;
			}
			loanOutputDto.add(loanOD);
		}
		
		return loanOutputDto;
	}

	@Override
	public RejectDto rejectLoan(Long managerId, Long loanAppId, RejectDto rejectDto) {
		
		if (usersRepository.existsById(managerId) && usersRepository.verifyUser(managerId).equals("Manager")) {
			if (!loanRepository.existsById(loanAppId)) {
				throw new LoanNotFoundException("Loan Number #" + loanAppId + " does not exists");
			}
		} else {
			throw new ManagerNotFoundException("Manager Id#" + managerId + " does not exists");
		}
		
		if(loanRepository.getStatus(loanAppId)==1) {
			loanRepository.rejectLoan(loanAppId);
		}else if(loanRepository.getStatus(loanAppId)==-1){
			throw new AlreadyFinalizedException("Loan Number #"+loanAppId+" already rejected");
		}else if(loanRepository.getStatus(loanAppId)==2){
			throw new AlreadyFinalizedException("Loan Number #"+loanAppId+" already sanctioned");
		}
		
		return rejectDto;
	}

	@Override
	public SanctionOutputDto sanctionLoan(Long managerId, Long loanAppId, SanctionDto sanctionDto) {

		if (usersRepository.existsById(managerId)&& usersRepository.verifyUser(managerId).equals("Manager")) {
			if (!loanRepository.existsById(loanAppId)) {
				throw new LoanNotFoundException("Loan Number #" + loanAppId + " does not exists");
			}
		} else {
			throw new ManagerNotFoundException("Manager Id#" + managerId + " does not exists");
		}
		if(loanRepository.getStatus(loanAppId)==1) {
		
			SanctionOutputDto sanctionOutputDto=new SanctionOutputDto();
			double amount=sanctionDto.getLoanAmountSanctioned();
			double term=sanctionDto.getTermOfLoan();
			double TermPaymentAmount= ( amount* (1 + (0.15*(term/12))));
			double EMI = TermPaymentAmount/term;
			LocalDate paymentStartDate= LocalDate.now();
			LocalDate loanClosureDate = paymentStartDate.plusYears((long) term);
			
			
			sanctionOutputDto.setLoanAmountSanctioned(amount);
			sanctionOutputDto.setLoanClosureDate(loanClosureDate.toString());
			sanctionOutputDto.setMonthlyPayment(EMI);
			sanctionOutputDto.setPaymentStartDate(paymentStartDate.toString());
			sanctionOutputDto.setTermOfLoan(term);
			
			loanRepository.sanctionLoan(loanAppId);		
			return EMParser.parse(sanctionInfoRepository.save(EMParser.parse(sanctionOutputDto,managerId,loanAppId)));
		
		
		}else if(loanRepository.getStatus(loanAppId)==-1){
			throw new AlreadyFinalizedException("Loan Number #"+loanAppId+" already rejected");
		}else if(loanRepository.getStatus(loanAppId)==2){
			throw new AlreadyFinalizedException("Loan Number #"+loanAppId+" already sanctioned");
		}else
		{
			throw new InvalidDataException("Loan Number #"+loanAppId+" is not processed by the Clerk");
		}
	}

}
