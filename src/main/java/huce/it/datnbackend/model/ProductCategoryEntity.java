package huce.it.datnbackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@NoArgsConstructor
@Data
@AllArgsConstructor
@Table(name = "productcategory", uniqueConstraints={@UniqueConstraint(columnNames={"name"})})
public class ProductCategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "parentid")
    private int parentID;

    @Column(name = "createdate")
    private Timestamp createDate;

    @Column(name = "updatedate")
    private Timestamp updateDate;

    @Column(name = "status")
    private int status;
}
