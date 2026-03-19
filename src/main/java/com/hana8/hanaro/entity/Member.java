package com.hana8.hanaro.entity;

import com.hana8.hanaro.common.enums.MemberRole;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "members", uniqueConstraints = {
        @UniqueConstraint(name = "unique_member_email", columnNames = {"email"})
})
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 15)
    private String nickname;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "member_roles", joinColumns = @JoinColumn(name = "member_id"))
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Set<MemberRole> roles = new HashSet<>();

    @OneToMany(mappedBy = "member")
    @Builder.Default
    private List<Account> accounts = new ArrayList<>();

    public void update(String email, String password, String nickname, Boolean isActive, Set<MemberRole> roles) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.isActive = isActive;
        this.roles = roles;
    }
}
