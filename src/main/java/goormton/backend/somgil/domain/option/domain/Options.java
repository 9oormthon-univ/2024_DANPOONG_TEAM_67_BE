package goormton.backend.somgil.domain.option.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Options {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;
    private boolean checked = false;

    @Builder
    public Options(final String content, final boolean checked) {
        this.content = content;
        this.checked = checked;
    }
}
