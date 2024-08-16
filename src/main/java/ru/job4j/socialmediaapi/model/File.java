package ru.job4j.socialmediaapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "file")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Include
    @Positive
    private Long id;

    @NotNull
    private String path;

    @NotBlank
    private String name;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

}
