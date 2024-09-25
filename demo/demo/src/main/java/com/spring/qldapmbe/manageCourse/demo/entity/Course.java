package com.spring.qldapmbe.manageCourse.demo.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "course")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Course implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "image_url", nullable = false)
	private String imageUrl;

	@Column(name = "title", nullable = false)
	private String title;

	@Column(name = "des")
	private String des;

	@Column(name = "price")
	private Long price;

	@Column(name = "createdDate")
	private Date createdDate;

	@Column(name = "updatedDate")
	private Date updatedDate;

	@Column(name = "is_actived")
	private Boolean isActived;

	@Column(name = "is_free")
	private Boolean isFree;

	@Transient
	@JsonIgnore
	private MultipartFile file;

	@OneToMany(mappedBy = "course", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JsonIgnore
	private List<Lesson> lessons;

	@OneToMany(mappedBy = "course", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JsonIgnore
	private List<UserCourse> userCourse;

}
