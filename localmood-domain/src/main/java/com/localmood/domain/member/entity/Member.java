package com.localmood.domain.member.entity;

import com.localmood.common.entity.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member")
public class Member extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	private String nickname;

	@Column
	private String email;

	@Column
	private String password;

	@Column
	private String profileImgUrl;

	@Builder
	public Member (String nickname, String email, String password, String profileImgUrl) {
		this.nickname = nickname;
		this.email = email;
		this.password = password;
		this.profileImgUrl = profileImgUrl;
	}

}
