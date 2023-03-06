package com.hellsfood.api.roles.data;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.hellsfood.api.users.data.User;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Role {

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;

	@Column(length = 32, nullable=false, unique=true)
	private String roleName;

	@ManyToMany(mappedBy="roles")
	private List<User> users;

	public Role(String roleName) {
		this.roleName = roleName;
	}
}
