package com.example.config;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class BeanConfiguration {
	
	@Bean
	public SimpleDateFormat simpleDateFormat() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
		return simpleDateFormat;
	}
	
	@Bean
	public Locale locale() {
		return new Locale("vn", "VN");
		
	}
	
	@Bean
	public NumberFormat numberFormat(Locale locale) {
		NumberFormat numberFormat = NumberFormat.getInstance(locale);
		return numberFormat;
	}
	
}
