package pro.inmost.vacancydiary.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table
@DynamicUpdate
@ToString
public class Vacancy {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Column
    private String companyName;

    @Column
    private String position;

    @Column
    private int expectedSalary;

    @Column
    private String link;

    @Column
    private String recruiterContacts;

    @Column
    private String status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy@HH:mm:ss")
    @Column
    private Timestamp lastStatusChange;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToMany(mappedBy = "vacancies", fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<User> users;

    public Vacancy() {
    }

    public Vacancy(String companyName, String position, int expectedSalary, String link,
        String recruiterContacts, String status) {
        this.companyName = companyName;
        this.position = position;
        this.expectedSalary = expectedSalary;
        this.link = link;
        this.recruiterContacts = recruiterContacts;
        this.status = status;
        this.lastStatusChange = new Timestamp(new Date().getTime());
    }
}
