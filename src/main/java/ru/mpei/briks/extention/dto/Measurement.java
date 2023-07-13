package ru.mpei.briks.extention.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Table(name = "measurements")
@AllArgsConstructor
@NoArgsConstructor
public class Measurement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private double time;
    @Column
    private double frequency;
    @Column
    private double nP;
    @Column
    private double gP;
    @Column
    private double nQ;
    @Column
    private double gQ;
}
