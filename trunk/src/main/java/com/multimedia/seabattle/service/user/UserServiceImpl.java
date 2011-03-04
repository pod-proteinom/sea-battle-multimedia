/*******************************************************************************
 * Copyright (c) 2011 demchuck.dima@gmail.com.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     demchuck.dima@gmail.com - initial API and implementation
 ******************************************************************************/
package com.multimedia.seabattle.service.user;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.multimedia.seabattle.dao.IGenericDAO;
import com.multimedia.seabattle.model.beans.User;

@Service("userService")
public class UserServiceImpl implements IUserService{
	private IGenericDAO<User, Long> user_dao;

	private SimpleMailMessage templateMessage;
	
	private JavaMailSender mailSender;
	private VelocityEngine velocityEngine;


	@Override
	public boolean registerUser(User user, String host, Locale locale) {
		user.setActive(Boolean.FALSE);
		user.setLast_accessed(new java.sql.Timestamp(System.currentTimeMillis()));
		//TODO: mb make it somewhere else
        String md5=org.apache.catalina.realm.RealmBase.Digest(user.getPassword(),"MD5","UTF-8");
		user.setPassword(md5);
		//sending email notification
		sendConfirmationEmail(user, host, locale);
		//TODO: insert roles
		user_dao.makePersistent(user);
		return true;
	}

	  private void sendConfirmationEmail(final User user, final String host, final Locale locale) {
	      MimeMessagePreparator preparator = new MimeMessagePreparator() {
	         public void prepare(MimeMessage mimeMessage) throws Exception {
	            MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
	            message.setTo(user.getEmail());
	            message.setFrom(templateMessage.getFrom());
	            message.setSubject("account registration");
	            Map<String, Object> model = new HashMap<String, Object>();
	            model.put("user", user);
	            model.put("host", host);
	            model.put("locale", locale.getLanguage());
	            
	            String text = VelocityEngineUtils.mergeTemplateIntoString(
	               velocityEngine,
	               "com/multimedia/seabattle/service/user/RegisterEmailTemplate.vm",
	               "UTF-8",
	               model);
	            message.setText(text, true);
	         }
	      };
	      this.mailSender.send(preparator);
	   }

	public static final String[] SECURITY_WHERE =  new String[]{"login","password", "active"};
	@Override
	public User getUser(String login, String password) {
		List<User> r =
				user_dao.getByPropertiesValuePortionOrdered(null, null,
						SECURITY_WHERE, new Object[]{login, password, Boolean.TRUE}, 0, 1, null, null);
		if (r!=null&&!r.isEmpty()){
			return r.get(0);
		}else{
			return null;
		}
	}

	@Override
	public User getUser(Long id) {
		User user = user_dao.getById(id);
		if (user.getActive()){
			return user;
		} else {
			return null;
		}
	}

	protected static final String[] SECURITY_UPDATE = new String[]{"last_accessed"};
	@Override
	public void userEntered(com.multimedia.security.beans.User user) {
		if (user!=null){
			user_dao.updatePropertiesById(SECURITY_UPDATE, new Object[]{new java.util.Date()}, user.getId());
		}
	}

	@Override
	public boolean checkUserEmail(String email) {
		return user_dao.getRowCount("email", email)==0;
	}

	@Override
	public boolean checkUserLogin(String login) {
		return user_dao.getRowCount("login", login)==0;
	}
	
	@Override
	public boolean activateUser(String login) {
		Long id = (Long)user_dao.getSinglePropertyU("id", "login", login);
		if (id==null){
			return false;
		} else {
			return user_dao.updatePropertyById("active", Boolean.TRUE, id)>0;
		}
	}

//------------------------------------------------------------------------------------------------
	@Required
	@Resource(name="userDAO")
	public void setDao(IGenericDAO<User, Long> dao) {
		this.user_dao = dao;
	}

	@Required
	@Resource(name="templateMessage")
    public void setRegisterTemplateMessage(SimpleMailMessage templateMessage) {
        this.templateMessage = templateMessage;
    }

	@Required
	@Resource(name="mailSender")
	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	@Required
	@Resource(name="velocityEngine")
	public void setVelocityEngine(VelocityEngine velocityEngine) {
	   this.velocityEngine = velocityEngine;
	}

}
