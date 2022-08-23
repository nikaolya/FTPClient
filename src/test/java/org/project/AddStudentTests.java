package org.project;

import org.project.POJO.Student;
import org.project.POJO.Students;
import org.testng.ITest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.project.Client.addNewStudent;

public class AddStudentTests implements ITest {

	private String reportedTestName = "";

	@BeforeMethod(alwaysRun = true)
	public void testData(Method method, Object[] testData) {
		reportedTestName = "Add a new student: " + testData[0].toString();
	}

	@Override
	public String getTestName() {
		return reportedTestName;
	}

	@Test(testName = "Add a new student.", dataProvider = "addNewStudent")
	public void shouldAddNewStudentTest(String description, List<Student> studentsList, int expectedIdOfNewStudent){
		int expectedNumberOfStudents = studentsList.size() + 1;
		Students students = new Students();
		students.setStudents(studentsList);
		Student newStudent = addNewStudent(students, "New Student Name");
		SoftAssert softAssert = new SoftAssert();
		softAssert.assertEquals(students.getStudents().size(), expectedNumberOfStudents, "Number of students is not increased by 1.");
		softAssert.assertEquals(newStudent.getId(), expectedIdOfNewStudent, "Id of the newly added student is not correct.");
		softAssert.assertAll();
	}

	@DataProvider(name = "addNewStudent")
	public Object[][] dpMethod(){
		return new Object[][] {
				{"No gaps in Ids sequence", new ArrayList<>(Arrays.asList(new Student(1, "Student1"), new Student(2, "Student2"), new Student(3, "Student3"))), 4},
				{"A gap is Ids sequence", new ArrayList<>(Arrays.asList(new Student(1, "Student1"), new Student(5, "Student5"), new Student(6, "Student6"))), 2}
		};
	}
}
