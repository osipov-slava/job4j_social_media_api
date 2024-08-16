package ru.job4j.socialmediaapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "web_post")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @NotNull
    @Length(min = 2)
    private String title;

    private String description;

    @PastOrPresent
    private LocalDateTime created;

    @Column(name = "is_active")
    private Boolean isActive;

    @ManyToOne
    @JoinColumn(name = "from_id")
    private User fromUser;

    @ManyToOne
    @JoinColumn(name = "to_id")
    private User toUser;

    @OneToMany(
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    @JoinColumn(name = "post_id")
    private List<File> files = new ArrayList<>();

}
