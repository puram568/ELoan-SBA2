package com.iiht.training.eloan.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iiht.training.eloan.dto.LoanDto;
import com.iiht.training.eloan.dto.LoanOutputDto;
import com.iiht.training.eloan.dto.UserDto;
import com.iiht.training.eloan.exception.CustomerNotFoundException;
import com.iiht.training.eloan.exception.InvalidDataException;
import com.iiht.training.eloan.exception.LoanNotFoundException;
import com.iiht.training.eloan.repository.LoanRepository;
import com.iiht.training.eloan.repository.ProcessingInfoRepository;
import com.iiht.training.eloan.repository.SanctionInfoRepository;
import com.iiht.training.eloan.repository.UsersRepository;
import com.iiht.training.eloan.service.CustomerService;
import com.iiht.training.eloan.util.EMParser;

@Service
public class CustomerServiceImpl implements CustomerService {

	LoanOutputDto loanOutputDto = new LoanOutputDto();
	@Autowired
	private UsersRepository usersRepository;

	@Autowired
	private LoanRepository loanRepository;

	@Autowired
	private ProcessingInfoRepository processingInfoRepository;

	@Autowired
	private SanctionInfoRepository sanctionInfoRepository;

	@Transactional
	@Override
	public UserDto register(UserDto userDto) {

		if (userDto != null) {
			if (usersRepository.existsByEmail(userDto.getEmail())) {
				throw new InvalidDataException("Customer with email #" + userDto.getEmail() + " already exists");
			}
			userDto = EMParser.parse(usersRepository.save(EMParser.parse(userDto)));
		}

		return userDto;
	}

	@Transactional
	@Override
	public LoanOutputDto applyLoan(Long customerId, LoanDto loanDto) {

		int size;

		if (loanDto != null) {
			if (!usersRepository.existsById(customerId)|| !usersRepository.verifyUser(customerId).equals("Customer")) {				
				throw new CustomerNotFoundException("Customer does not exists");
			}
			loanDto = EMParser.parse(loanRepository.save(EMParser.parse(loanDto, customerId)));
		}

		size = loanRepository.findByCustomerId(customerId).size();
		Long loanAppId = loanRepository.findByCustomerId(customerId).get(size - 1);

		loanOutputDto.setCustomerId(customerId);
		loanOutputDto.setLoanAppId(loanAppId);
		loanOutputDto.setUserDto(usersRepository.findById(customerId).stream().map(e -> EMParser.parse(e))
				.collect(Collectors.toList()).get(0));
		loanOutputDto.setLoanDto(loanDto);
		loanOutputDto.setProcessingDto(null);
		loanOutputDto.setSanctionOutputDto(null);
		loanOutputDto.setStatus("Applied");
		loanOutputDto.setRemark("Mortage Loan");

		return loanOutputDto;
	}

	@Override
	public LoanOutputDto getStatus(Long loanAppId) {

		LoanDto loanDto = new LoanDto();
		if (!loanRepository.existsById(loanAppId)) {
			throw new LoanNotFoundException("Loan Number " + loanAppId + " does not exists for this customer");
		}
		loanDto = EMParser.parse(loanRepository.findById(loanAppId).get());
		loanOutputDto.setLoanDto(loanDto);
		loanOutputDto.setCustomerId(loanRepository.findByLoanAppId(loanAppId));
		loanOutputDto.setLoanAppId(loanAppId);
		loanOutputDto
				.setUserDto(EMParser.parse(usersRepository.findById(loanRepository.findByLoanAppId(loanAppId)).get()));

		switch (loanRepository.getStatus(loanAppId)) {

		case 0:
			loanOutputDto.setStatus("Applied");
			loanOutputDto.setProcessingDto(null);
			loanOutputDto.setSanctionOutputDto(null);
			break;
		case 1:
			loanOutputDto.setStatus("Processed");
			break;
		case 2:
			loanOutputDto.setStatus("Sanctioned");
			break;
		case -1:
			loanOutputDto.setStatus("Rejected");
			break;
		default:
			break;
		}

		loanOutputDto.setRemark(loanRepository.getRemark(loanAppId));
		return loanOutputDto;
	}

	@Override
	public List<LoanOutputDto> getStatusAll(Long customerId) {
		List<LoanOutputDto> loanOutputDto = new ArrayList<LoanOutputDto>();
		List<LoanDto> loanDto = new ArrayList<LoanDto>();
		UserDto userDto = new UserDto();

		if (!usersRepository.existsById(customerId)) {
			throw new CustomerNotFoundException("Customer with " + customerId + " doesnt not exists");
		}

		loanDto.addAll(loanRepository.findAllByCustomerId(customerId).stream().map(e -> EMParser.parse(e))
				.collect(Collectors.toList()));

		userDto = usersRepository.findById(customerId).stream().map(e -> EMParser.parse(e)).collect(Collectors.toList())
				.get(0);

		List<Long> loanAppId = loanRepository.findByCustomerId(customerId);

		for (int i = 0; i < loanDto.size(); i++) {
			LoanOutputDto loanOD = new LoanOutputDto();
			loanOD.setCustomerId(customerId);
			loanOD.setLoanAppId(loanAppId.get(i));
			loanOD.setLoanDto(loanDto.get(i));
			loanOD.setUserDto(userDto);

			switch (loanRepository.getStatus(loanAppId.get(i))) {
				case 0:loanOD.setStatus("Applied");break;
				case 1:loanOD.setStatus("Processed");break;
				case 2:loanOD.setStatus("Sanctioned");break;
				case -1:loanOD.setStatus("Rejected");break;
				default:break;
			}
			
			loanOD.setRemark(loanRepository.getRemark(loanAppId.get(i)));
			loanOutputDto.add(loanOD);
		}
		return loanOutputDto;
	}

}
