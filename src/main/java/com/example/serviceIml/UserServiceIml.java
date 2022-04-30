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
import com.example.dto.UserDTO;
import com.example.model.user.Role;
import com.example.model.user.User;
import com.example.model.user.UserRole;
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
	public void saveUser(UserDTO userDTO) {
		User user  = new User();
		user.setActive(0);
		if(userDTO.getName()=="") {
			user.setUserName("Unknown");
		}else {
			user.setUserName(userDTO.getName());
		}		
		user.setEmail(userDTO.getEmail());
		user.setCreatedAt(simpleDateFormat.format(new Date()));
		user.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));
		
		user.setAvatarURL("user-default-img.jpg");
		
		
		
		Role roleUser = new Role();
		roleUser.setId(2);
		roleUser.setRoleName("ROLE_USER");

		
		
		UserRole userRole = new UserRole();
		userRole.setRole(roleUser);
		userRole.setUser(user);
		
		List<UserRole>userRoles = new ArrayList<UserRole>();
		userRoles.add(userRole);
		user.setUserRoles(userRoles);
		
		
		userDao.save(user);
		
		//send email
		Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				try {
					sendVerifyEmail(userDTO.getEmail());
				} catch (MessagingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
		thread.start();
		
		
	}

	@Override
	public void sendVerifyEmail(String email) throws MessagingException {
		MimeMessage mimeMailMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper messageHelper;
		try {
			messageHelper = new MimeMessageHelper(mimeMailMessage, true);
			messageHelper.setFrom("vu40654@donga.edu");
			messageHelper.setText("<h1 style=\"color: red;\">Cảm ở bạn vì đã đăng kí tài khoản. Vui lòng hoàn thành bước cuối cùng</h1><br>\r\n"
					+ "<a href=\"http://localhost:8080/user/confirm-email?email="+email+"\">Nhấp vào link để đăng nhập</a>", true);
			messageHelper.setTo(email);
			messageHelper.setSubject("Xác thực tài khoản - ASHION SHOP");
		
			javaMailSender.send(mimeMailMessage);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

	@Override
	public void updateUserConfirmEmail(String email) {
		User user = userDao.findUserByEmail(email);
		
		userDao.setActiveUser(user.getId());
		
	}
	
	@Override
	public User findUserByEmail(String email) {
		return userDao.findUserByEmail(email);
	}
	
	@Override
	public void sendResetPassToMail(String email) throws MessagingException {
		Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				MimeMessage mimeMailMessage = javaMailSender.createMimeMessage();
				MimeMessageHelper messageHelper;
				try {
					messageHelper = new MimeMessageHelper(mimeMailMessage, true);
					messageHelper.setFrom("vu40654@donga.edu");
					messageHelper.setText("<h1 style=\"color: red;\">Bạn đã gởi yêu cầu thay đổi mật khẩu.</h1><br>\r\n"
							+ "<a href=\"http://localhost:8080/reset-password?email="+email+"\">Nhấp vào link để hoàn thành bước cuối cùng</a>", true);
					messageHelper.setTo(email);
					messageHelper.setSubject("RESET PASSWORD - ASHION SHOP");
				
					javaMailSender.send(mimeMailMessage);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
		thread.start();
		
	}
	
	@Override
	public void resetPass(String email, String pass) {
		User u = findUserByEmail(email);
		String encodePass = bCryptPasswordEncoder.encode(pass);
		userDao.resetPassword(u.getId(), encodePass);
		
	}
}
