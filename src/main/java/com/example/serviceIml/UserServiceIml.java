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
import org.springframework.web.multipart.MultipartFile;

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
	
	@Autowired
	UploadFileServiceIml uploader;
	
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
		user.setIsDeleted(0);
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
			messageHelper.setText("<h1 style=\"color: red;\">C???m ??? b???n v?? ???? ????ng k?? t??i kho???n. Vui l??ng ho??n th??nh b?????c cu???i c??ng</h1><br>\r\n"
					+ "<a href=\"http://localhost:8080/user/confirm-email?email="+email+"\">Nh???p v??o link ????? ????ng nh???p</a>", true);
			messageHelper.setTo(email);
			messageHelper.setSubject("X??c th???c t??i kho???n - ASHION SHOP");
		
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
					messageHelper.setText("<h1 style=\"color: red;\">B???n ???? g???i y??u c???u thay ?????i m???t kh???u.</h1><br>\r\n"
							+ "<a href=\"http://localhost:8080/reset-password?email="+email+"\">Nh???p v??o link ????? ho??n th??nh b?????c cu???i c??ng</a>", true);
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
	
	@Override
	public void updateUserInfo(User user) {
		userDao.saveAndFlush(user);
		
	}
	@Override
	public void saveUserAvatar(MultipartFile file,long userId) {
		
		try {
			uploader.saveUploadFile(file, "user", file.getOriginalFilename());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public List<User> getListUser(int limit, int offset) {
		// TODO Auto-generated method stub
		return userDao.getPageUser(limit, offset);
	}
	
	@Override
	public List<User> searchUserByName(String name) {
		// TODO Auto-generated method stub
		return userDao.findUserByName(name);
	}
	
	@Override
	public User findUserByid(long id) {
		// TODO Auto-generated method stub
		return userDao.findUserById(id);
	}
}
