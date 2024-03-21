package com.java.profile.model;

// import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
// import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Student {
    @Id
    @Column(name ="uuid")
    // @GeneratedValue(generator = "uuid")
    // @GenericGenerator(name ="uuid", strategy = "org.hibernate.id.UUIDGenerator")
    private String uuid;

    @Column(name ="file_name") // Add this if you want to store the filename in the database
    private String fileName; 


    
}
