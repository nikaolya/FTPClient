package org.project;

import org.project.POJO.Student;
import org.project.POJO.Students;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.ArrayList;
import java.util.Arrays;

import static org.project.Client.getInformationById;

public class GetStudentInformationTests {
	@Test(testName = "Get information about a student present in list.")
	public void shouldGetInformationAboutExistingStudentTest(){
		Students students = new Students();
		students.setStudents(new ArrayList<>(Arrays.asList(new Student(1, "Student1"), new Student(2, "Student2"), new Student(3, "Student3"))));
		Student student = getInformationById(students, 3);

		SoftAssert softAssert = new SoftAssert();
		softAssert.assertEquals(student.getId(), 3, "Student Id is not correct.");
		softAssert.assertEquals(student.getName(), "Student3", "Student name is not correct.");
		softAssert.assertAll();
	}

	@Test(testName = "Get information about a student absent in a list.")
	public void shouldNotGetInformationAboutNotExistingStudentTest(){
		Students students = new Students();
		students.setStudents(new ArrayList<>(Arrays.asList(new Student(1, "Student1"), new Student(2, "Student2"))));
		Student student = getInformationById(students, 3);

		Assert.assertNull(student, "Not existing student is found in the list.");
	}
}
