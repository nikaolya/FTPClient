package org.project;

import org.project.POJO.Student;
import org.project.POJO.Students;
import org.testng.Assert;
import org.testng.ITest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static org.project.Client.parseJSONtoJavaObject;
import static org.project.Client.parseStudentsObjectToJson;

public class ParsingJsonTests implements ITest {
	private String reportedTestName = "";

	@BeforeMethod(alwaysRun = true)
	public void testData(Method method, Object[] testData) {
		reportedTestName = method.getName() + ": "+ testData[0].toString();
	}

	@Override
	public String getTestName() {
		return reportedTestName;
	}

	@Test(testName = "Parse JSON String to Students Object", dataProvider = "parseJson")
	public void shouldParseJsonToStudentsObjectTest(String description, String JSONFileName, int expectedNumberOfStudents, List<Integer> expectedIds, List<String> expectedNames){
		Students result = parseJSONtoJavaObject(String.format("src/test/resources/%s", JSONFileName));
		List<Student> students = result.getStudents();
		SoftAssert softAssert = new SoftAssert();
		softAssert.assertEquals(result.getStudents().size(), expectedNumberOfStudents, "Number of Students in a list is not correct.");

		for (int i = 0; i < students.size(); i++) {
			softAssert.assertEquals(students.get(i).getId(), (int) expectedIds.get(i), String.format("Id of student %d is not correct", i));
			softAssert.assertEquals(students.get(i).getName(), expectedNames.get(i), String.format("Name of student %d is not correct", i));
		}
		softAssert.assertAll();
	}

	@Test(testName = "Parse Students Object to JSON", dataProvider = "parseJson")
	public void shouldParseStudentsObjectToJsonStringTest(String description, String JSONFileName, int numberOfStudents, List<Integer> ids, List<String> names){
		List<Student> studentsList = new ArrayList<>();
		for (int i = 0; i < numberOfStudents; i++) {
			studentsList.add(new Student(ids.get(i), names.get(i)));
		}
		Students students = new Students();
		students.setStudents(studentsList);

		StringBuilder expectedJsonString = new StringBuilder();
		Path path = Paths.get(String.format("src/test/resources/%s", JSONFileName));
		try (Scanner scanner = new Scanner(path)){
			while(scanner.hasNextLine()){
				expectedJsonString.append(scanner.nextLine());
				if (scanner.hasNextLine()){
					expectedJsonString.append("\n");
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		StringBuilder actualJsonString = parseStudentsObjectToJson(students);
		Assert.assertEquals(actualJsonString.toString(), expectedJsonString.toString(), "Resulting Json String is not correct");
	}

	@DataProvider(name = "parseJson")
	public Object[][] dpMethod(){
		return new Object[][] {
				{"Three students", "jsonString.txt", 3, Arrays.asList(1, 2, 3), Arrays.asList("Student1", "Student2", "Student3")},
				{"No students", "emptyJsonString.txt", 0, Collections.emptyList(), Collections.emptyList()}
		};
	}
}
