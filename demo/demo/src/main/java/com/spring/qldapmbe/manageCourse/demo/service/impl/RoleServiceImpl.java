package com.spring.qldapmbe.manageCourse.demo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.qldapmbe.manageCourse.demo.entity.Role;
import com.spring.qldapmbe.manageCourse.demo.repository.RoleRepository;
import com.spring.qldapmbe.manageCourse.demo.service.RoleService;


@Service
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleRepository roleRepository;

	@Override
	public Role findByName(String name) {
		return roleRepository.findByName(name);
	}

}
