package org.wildflowergardening.backend.core.wildflowergardening.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity(name = "account_shelter")
@Table(name = "account_shelter")
public class AccountShelter {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name", nullable = false)
  @Comment("센터명")
  private String name;

  @Column(name = "account_name", nullable = false)
  @Comment("로그인을 위한 identifier")
  private String accountName;

  // Todo encrypt
  @Column(name = "password", nullable = false)
  @Comment("로그인을 위한 비밀번호 (encrypted)")
  private String password;

  @Column(name = "latitude", columnDefinition = "decimal(10,8)", nullable = false)
  @Comment("위도")
  private BigDecimal latitude;

  @Column(name = "longitude", columnDefinition = "decimal(11,8)", nullable = false)
  @Comment("경도")
  private BigDecimal longitude;

//  private LocalDateTime createdAt;
}
