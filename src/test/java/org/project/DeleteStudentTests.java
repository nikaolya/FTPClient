package org.project;

import org.project.POJO.Student;
import org.project.POJO.Students;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.ArrayList;
import java.util.Arrays;

import static org.project.Client.deleteStudentById;

public class DeleteStudentTests {

	@Test(testName = "Delete a student present in the list.")
	public void shouldDeleteStudentTest(){
		Students students = new Students();
		students.setStudents(new ArrayList<>(Arrays.asList(new Student(1, "Student1"), new Student(2, "Student2"), new Student(3, "Student3"))));
		int expectedNumberOfStudents = students.getStudents().size() - 1;
		Student deletedStudent = deleteStudentById(students, 2);

		SoftAssert softAssert = new SoftAssert();
		softAssert.assertEquals(students.getStudents().size(), expectedNumberOfStudents, "Number of students is not decreased by 1.");
		softAssert.assertEquals(deletedStudent.getId(), 2);
		softAssert.assertFalse(students.getTakenIds().contains(2), "Id of the deleted student is present in list.");
		softAssert.assertAll();
	}

	@Test(testName = "Delete a student not present in the list.")
	public void shouldNotDeleteNotPresentStudentTest(){
		Students students = new Students();
		students.setStudents(new ArrayList<>(Arrays.asList(new Student(1, "Student1"), new Student(4, "Student4"), new Student(3, "Student3"))));
		int expectedNumberOfStudents = students.getStudents().size();
		Student deletedStudent = deleteStudentById(students, 2);

		SoftAssert softAssert = new SoftAssert();
		softAssert.assertEquals(students.getStudents().size(), expectedNumberOfStudents, "Number of students differs from the original.");
		softAssert.assertNull(deletedStudent);
		softAssert.assertAll();
	}
}
