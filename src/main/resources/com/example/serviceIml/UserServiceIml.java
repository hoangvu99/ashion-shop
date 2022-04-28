package com.example.serviceIml;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.dao.RoleDao;
import com.example.dao.UserDao;
import com.example.model.user.Role;
import com.example.model.user.User;
import com.example.service.UserService;

@Service
@Transactional
public class UserServiceIml  implements UserService{
	
	@Autowired
	UserDao  userDao;
	
	@Autowired
	SimpleDateFormat simpleDateFormat;
	
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	RoleDao roleDao;
	
	@Autowired
	JavaMailSender javaMailSender;
	
	@Override
	public void saveUser(User user) {
		
		user.setActive(0);
		user.setCreatedAt(simpleDateFormat.format(new Date()));
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		
		List<Role>roles = new ArrayList<Role>();
		List<User>users = new ArrayList<User>();
		
		users.add(user);
		
		Role roleUser = new Role();
		roleUser.setId(2);
		roleUser.setRoleName("ROLE_USER");
//		roleUser.setUsers(users);
		roles.add(roleUser);
		
//		user.setRoles(roles);
		
		
		userDao.save(user);
		
		
	}

	@Override
	public void sendVerifyEmail(String email) throws MessagingException {
		MimeMessage mimeMailMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper messageHelper;
		try {
			messageHelper = new MimeMessageHelper(mimeMailMessage, true);
			messageHelper.setFrom("vu40654@donga.edu");
			messageHelper.setText("<h1 style=\"color: red;\">Cảm ở bạn vì đã đăng kí tài khoản. Vui lòng hoàn thành bước cuối cùng</h1><br>\r\n"
					+ "<a href=\"http://localhost:8080/user/confirm-email?email="+email+"\">Nhấp vào link để xác thực</a>", true);
			messageHelper.setTo(email);
			messageHelper.setSubject("Xác thực tài khoản");
		
			javaMailSender.send(mimeMailMessage);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

	@Override
	public void updateUserConfirmEmail(String email) {
		User user = userDao.findUserByEmail(email);
		user.setActive(1);
		userDao.save(user);
		
	}

}
