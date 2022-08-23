package org.project;

import org.project.POJO.Student;
import org.project.POJO.Students;
import org.testng.Assert;
import org.testng.ITest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.project.Client.getAllStudents;
import static org.project.Client.getStudentsByName;

public class GetStudentsTests implements ITest {

	private String reportedTestName = "";

	@BeforeMethod(alwaysRun = true)
	public void testData(Method method, Object[] testData) {
		reportedTestName = method.getName() + ": " + testData[0].toString();
	}

	@Override
	public String getTestName() {
		return reportedTestName;
	}

	@Test(testName = "Get all students sorted by name.", dataProvider = "getAllStudents")
	public void shouldGetAllStudentsSorted(String description, List<Student> studentsList, List<Student> expectedResult){
		Students students = new Students();
		students.setStudents(studentsList);
		List<Student> actualResult = getAllStudents(students);
		Assert.assertEquals(actualResult.toString(), expectedResult.toString());
	}

	@DataProvider(name = "getAllStudents")
	public Object[][] dpMethod2(){
		return new Object[][] {
			{
				"One student", Arrays.asList(new Student(1, "Student1")),
					Arrays.asList(new Student(1, "Student1"))},
			{
				"Three students. Result sorted by name.", Arrays.asList(new Student(2, "Student3"), new Student(3, "Student1"), new Student(1, "Student2")),
					Arrays.asList(new Student(3, "Student1"), new Student(1, "Student2"), new Student(2, "Student3"))
			},
			{
				"No students", Collections.emptyList(), Collections.emptyList()
			}
		};
	}

	@Test(testName = "Get a list of students with a desired name.", dataProvider = "getStudentsByName")
	public void shouldGetStudentsByName(String description, List<Student> studentsList, String studentName, List<Student> expectedResult){
		Students students = new Students();
		students.setStudents(studentsList);
		List<Student> actualResult = getStudentsByName(students, studentName);
		Assert.assertEquals(actualResult.toString(), expectedResult.toString());
	}

	@DataProvider(name = "getStudentsByName")
	public Object[][] dpMethod(){
		return new Object[][] {
			{
				"One student in a list.", Arrays.asList(new Student(1, "Student1"), new Student(2, "Student2"), new Student(3, "Student3")),
					"Student3", Arrays.asList(new Student(3, "Student3"))},
			{
				"Two students in a list. Result sorted by id.", Arrays.asList(new Student(1, "Student1"), new Student(3, "Student2"), new Student(2, "Student2")),
					"Student2", Arrays.asList(new Student(2, "Student2"), new Student(3, "Student2"))
			},
			{
				"No student with such name.", Arrays.asList(new Student(1, "Student1"), new Student(2, "Student2"), new Student(3, "Student2")),
					"Student4", Collections.emptyList()
			}
		};
	}

}
