package com.multimedia.security.beans;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.Email;

/**
 *
 * @author demchuck.dima@gmail.com
 */
@MappedSuperclass
public abstract class User{

	@Email
    private String email;
    private String login;
    private String password;

    private Date last_accessed;

    //private Set<Role> roles;
    //private Set new_passwords;

	//not for db
	/** for password confirmation */
    @Transient
	private String password_repeat;
	/** old password */
    @Transient
	private String password_old;

	public abstract Long getId();
	public abstract void setId(Long id);

	public String getEmail() {return email;}
	public void setEmail(String email) {this.email = email;}

	public String getLogin() {return login;}
	public void setLogin(String login) {this.login = login;}

	public String getPassword() {return password;}
	public void setPassword(String password) {this.password = password;}

	//public Set getNew_passwords() {return new_passwords;}
	//public void setNew_passwords(Set new_passwords) {this.new_passwords = new_passwords;}

	public String getPassword_repeat() {return password_repeat;}
	public void setPassword_repeat(String password_repeat) {this.password_repeat = password_repeat;}

	public String getPassword_old() {return password_old;}
	public void setPassword_old(String password_old) {this.password_old = password_old;}

	/*public Set<Role> getRoles() {return roles;}
	public void setRoles(Set<Role> roles) {this.roles = roles;}*/
	public Set<Role> getRoles() {return new HashSet<Role>();}
	public void setRoles(Set<Role> roles) {}
	/*public Set<String> getRolesStr() {
		if (roles==null){
			return null;
		}else{
			Iterator<Role> items = roles.iterator();
			Set<String> roles_new = new HashSet<String>();
			while (items.hasNext()){
				Role r = items.next();
				roles_new.add(r.getRole());
			}
			return roles_new;
		}
	}
	public void setRolesStr(Set<String> roles) {
		//transforming to set of Role objects
		if (roles!=null){
			HashSet<Role> new_items = new HashSet<Role>();
			Iterator<String> items = roles.iterator();
			while (items.hasNext()){
				Role r = new Role();
				r.setRole(items.next());
				r.setUser(this);
				new_items.add(r);
			}

			if (this.roles==null){
				this.roles = new_items;
			}else{
				this.roles.retainAll(new_items);
				this.roles.addAll(new_items);
			}
		}
	}*/

	/**
	 * replace all values that are in both sets by values in second set
	 * @param new_items
	 */
	/*public void setNewRoles(Set<Role> new_items){
		if (this.roles!=null){
			new_items.retainAll(roles);
			new_items.addAll(roles);
			roles = new_items;
		}
	}*/

	/**
	 * copies only simple types i.e. strings, long ...
	 * @return
	 */
	@Override
	public abstract Object clone();

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 97 * hash + (this.login != null ? this.login.hashCode() : 0);
		hash = 97 * hash + (this.password != null ? this.password.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final User other = (User) obj;
		if ((this.login == null) ? (other.login != null) : !this.login.equals(other.login)) {
			return false;
		}
		if ((this.password == null) ? (other.password != null) : !this.password.equals(other.password)) {
			return false;
		}
		return true;
	}

	public Date getLast_accessed() {return last_accessed;}
	public void setLast_accessed(Date last_accessed) {this.last_accessed = last_accessed;}
}
