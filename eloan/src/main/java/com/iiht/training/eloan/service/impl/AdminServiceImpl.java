package com.iiht.training.eloan.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iiht.training.eloan.dto.UserDto;
import com.iiht.training.eloan.entity.Users;
import com.iiht.training.eloan.exception.InvalidDataException;
import com.iiht.training.eloan.repository.UsersRepository;
import com.iiht.training.eloan.service.AdminService;

@Service
public class AdminServiceImpl implements AdminService {

	@Autowired
	private UsersRepository usersRepository;
	
	@Transactional
	@Override
	public UserDto registerClerk(UserDto userDto) {
		if(userDto!=null) {
			if(usersRepository.existsByEmail(userDto.getEmail())) {
				throw new InvalidDataException("Clerk with email #"+userDto.getEmail()+ " already exists");
			}
			userDto=parse(usersRepository.save(parseClerk(userDto)));
		}
		
		return userDto;
	}

	@Transactional
	@Override
	public UserDto registerManager(UserDto userDto) {
		if(userDto!=null) {
			if(usersRepository.existsByEmail(userDto.getEmail())) {
				throw new InvalidDataException("Clerk with email #"+userDto.getEmail()+ " already exists");
			}
			userDto=parse(usersRepository.save(parseManager(userDto)));
		}
		
		return userDto;
	}

	@Override
	public List<UserDto> getAllClerks() {
		return usersRepository.findByRole("Clerk").stream().map(e->parse(e)).collect(Collectors.toList());
	}

	@Override
	public List<UserDto> getAllManagers() {
		return usersRepository.findByRole("Manager").stream().map(e->parse(e)).collect(Collectors.toList());
	}
	
	public static UserDto parse(Users source) {
		UserDto target = new UserDto();
		target.setId(source.getId());
		target.setFirstName(source.getFirstName());
		target.setLastName(source.getLastName());
		target.setEmail(source.getEmail());
		target.setMobile(source.getMobile());
		return target;
	}
	public static Users parseClerk(UserDto source) {
		Users target = new Users();
		target.setId(source.getId());
		target.setFirstName(source.getFirstName());
		target.setLastName(source.getLastName());
		target.setEmail(source.getEmail());
		target.setMobile(source.getMobile());
		target.setRole("Clerk");
		return target;
	}
	
	public static Users parseManager(UserDto source) {
		Users target = new Users();
		target.setId(source.getId());
		target.setFirstName(source.getFirstName());
		target.setLastName(source.getLastName());
		target.setEmail(source.getEmail());
		target.setMobile(source.getMobile());
		target.setRole("Manager");
		return target;
	}


}
