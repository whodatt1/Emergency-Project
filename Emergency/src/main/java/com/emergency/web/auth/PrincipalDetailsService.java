package com.emergency.web.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.emergency.web.mapper.UserMapper;
import com.emergency.web.model.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {
	
	private final UserMapper userMapper;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.info("PrincipalDetailsService loadUserByUsername()");
		User user = userMapper.findById(username);
		
		if (user == null) {
			new UsernameNotFoundException("해당 ID 계정이 존재하지 않습니다.");
		}
		return new PrincipalDetails(user);
	}
	
}
