package com.multimedia.security.beans;

/**
 * Role generated by hbm2java
 */
public class Role implements java.io.Serializable {

	private Long id;
	//private Long id_users;
	private String role;
	private User user;

	public Role() {}
	public Role(String name, User user) {this.role=name;this.user=user;}

	public Long getId() {return this.id;}
	public void setId(Long id) {this.id = id;}

	public String getRole() {return this.role;}
	public void setRole(String role) {this.role = role;}

	public User getUser() {return this.user;}
	public void setUser(User user) {this.user = user;}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 97 * hash + (this.role != null ? this.role.hashCode() : 0);
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
		final Role other = (Role) obj;
		if ((this.role == null) ? (other.role != null) : !this.role.equals(other.role)) {
			return false;
		}
		return true;
	}

}


