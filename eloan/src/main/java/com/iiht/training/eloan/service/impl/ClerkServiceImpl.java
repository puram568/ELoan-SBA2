package com.iiht.training.eloan.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iiht.training.eloan.dto.LoanDto;
import com.iiht.training.eloan.dto.LoanOutputDto;
import com.iiht.training.eloan.dto.ProcessingDto;
import com.iiht.training.eloan.dto.UserDto;
import com.iiht.training.eloan.entity.Loan;
import com.iiht.training.eloan.exception.AlreadyProcessedException;
import com.iiht.training.eloan.exception.ClerkNotFoundException;
import com.iiht.training.eloan.exception.LoanNotFoundException;
import com.iiht.training.eloan.repository.LoanRepository;
import com.iiht.training.eloan.repository.ProcessingInfoRepository;
import com.iiht.training.eloan.repository.SanctionInfoRepository;
import com.iiht.training.eloan.repository.UsersRepository;
import com.iiht.training.eloan.service.ClerkService;
import com.iiht.training.eloan.util.EMParser;

@Service
public class ClerkServiceImpl implements ClerkService {

	@Autowired
	private UsersRepository usersRepository;

	@Autowired
	private LoanRepository loanRepository;

	@Autowired
	private ProcessingInfoRepository processingInfoRepository;

	@Autowired
	private SanctionInfoRepository sanctionInfoRepository;

	@Override
	public List<LoanOutputDto> allAppliedLoans() {

		List<LoanOutputDto> loanOutputDto = new ArrayList<LoanOutputDto>();
		List<LoanDto> loanDto = new ArrayList<LoanDto>();

		if (loanRepository.findAllByStatus(0).stream().map(e -> EMParser.parse(e)).collect(Collectors.toList())
				.isEmpty()) {
			throw new AlreadyProcessedException("No loans availble to process");
		}

		loanDto.addAll(
				loanRepository.findAllByStatus(0).stream().map(e -> EMParser.parse(e)).collect(Collectors.toList()));
		List<Long> loanAppId = loanRepository.findByStatus(0L);

		for (int i = 0; i < loanDto.size(); i++) {
			LoanOutputDto loanOD = new LoanOutputDto();
			Long customerId = loanRepository.findByLoanAppId(loanAppId.get(i));
			loanOD.setCustomerId(customerId);
			loanOD.setUserDto(usersRepository.findById(customerId).stream().map(e -> EMParser.parse(e))
					.collect(Collectors.toList()).get(0));
			loanOD.setLoanAppId(loanAppId.get(i));
			loanOD.setLoanDto(loanDto.get(i));
			loanOD.setStatus("Applied");
			loanOD.setRemark(loanRepository.getRemark(loanAppId.get(i)));
			loanOutputDto.add(loanOD);
		}

		return loanOutputDto;
	}

	@Override
	public ProcessingDto processLoan(Long clerkId, Long loanAppId, ProcessingDto processingDto) {

		if (usersRepository.existsById(clerkId)&& usersRepository.verifyUser(clerkId).equals("Clerk")) {
			if (!loanRepository.existsById(loanAppId)) {
				throw new LoanNotFoundException("Loan Id # " + loanAppId + " doesnot exists");
			}
		} else {
			throw new ClerkNotFoundException("Clerk Id # " + clerkId + " doesnot exists");
		}
		
		Integer status=loanRepository.getStatus(loanAppId);
		
		if(status==0)
		{
			loanRepository.updateLoan(loanAppId);
			processingDto=EMParser.parse(processingInfoRepository.save(EMParser.parse(processingDto,clerkId,loanAppId)));
		}
		else {
			throw new AlreadyProcessedException("Loan Id # "+loanAppId+" already processed");
		}
		return processingDto;
	}


}
