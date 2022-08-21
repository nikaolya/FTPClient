package org.example.POJO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Students {
	List<Student> students = new ArrayList<>();

	public List<Student> getStudents() {
		return students;
	}

	public void setStudents(List<Student> students) {
		this.students = students;
	}

	public void addStudent(Student newStudent){
		students.add(newStudent);
	}

	public void deleteStudent(int studentId){
		students.removeIf(i -> i.getId() == studentId);
	}

	public int findAvailableId(){
		IntStream ids = IntStream.range(1,students.size()+2);
		return ids.filter(i -> !getTakenIds().contains(i)).sorted().findFirst().getAsInt();
	}

	public List<Integer> getTakenIds(){
		return students.stream().map(Student::getId).collect(Collectors.toList());
	}
}
