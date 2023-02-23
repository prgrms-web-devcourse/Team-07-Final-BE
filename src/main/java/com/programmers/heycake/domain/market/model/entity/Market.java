package com.programmers.heycake.domain.market.model.entity;

import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.programmers.heycake.domain.BaseEntity;
import com.programmers.heycake.domain.member.model.entity.Member;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "market")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Where(clause = "deleted_at IS NULL")
@SQLDelete(sql = "UPDATE market SET deleted_at = NOW() WHERE id = ?")
public class Market extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "business_number", length = 10, nullable = false, unique = true)
	private String businessNumber;

	@Column(name = "address", length = 100, nullable = false)
	private String address;

	@Column(name = "market_name", length = 20, nullable = false)
	private String marketName;

	@Column(name = "owner_name", length = 10, nullable = false)
	private String ownerName;

	@Column(name = "phone_number", length = 20, nullable = false, unique = true)
	private String phoneNumber;

	@Column(name = "open_time", nullable = true)
	private LocalTime openTime;

	@Column(name = "end_time", nullable = true)
	private LocalTime endTime;

	@Column(name = "description", length = 500, nullable = true)
	private String description;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", referencedColumnName = "id")
	private Member member;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "market_enrollment_id", referencedColumnName = "id")
	private MarketEnrollment marketEnrollment;

	@Builder
	public Market(
			String businessNumber, String address, String marketName, String ownerName,
			String phoneNumber, LocalTime openTime, LocalTime endTime, String description
	) {
		this.businessNumber = businessNumber;
		this.address = address;
		this.marketName = marketName;
		this.ownerName = ownerName;
		this.phoneNumber = phoneNumber;
		this.openTime = openTime;
		this.endTime = endTime;
		this.description = description;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public void setMarketEnrollment(MarketEnrollment marketEnrollment) {
		this.marketEnrollment = marketEnrollment;
	}
}
