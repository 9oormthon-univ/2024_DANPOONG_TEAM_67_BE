package goormton.backend.somgil.domain.driver.domain;

import goormton.backend.somgil.domain.packages.domain.Packages;
import goormton.backend.somgil.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

// 운전기사 정보를 위한 데이터 클래스
@Getter
@Setter
@Entity
public class Driver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    private String driverId;
    private String name;
    private String contact;

    @OneToMany(mappedBy = "driver", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Packages> packages = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") // 외래 키 컬럼명 설정
    private User user;

    public void addPackage(Packages pkg) {
        if (pkg != null) {
            this.packages.add(pkg);
            pkg.setDriver(this); // 양방향 관계 설정
        }
    }

    public void removePackage(Packages pkg) {
        if (pkg != null) {
            this.packages.remove(pkg);
        }
    }
}
