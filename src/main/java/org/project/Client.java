package org.project;

import org.project.POJO.Student;
import org.project.POJO.Students;

import java.io.*;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Client {
	private static final String LINE_SEPARATOR = "\n";
	private static final Scanner scanner = new Scanner(System.in);
	private static Properties prop;

	private static String server;
	private static int port;

	private static Students students;
	private static String tempFilePath;
	private static boolean fileIsUpToDate = false;

	private static DataInputStream inputStream;
	private static DataOutputStream outputStream;

	public static void main(String[] args){
		prop = new Properties();
		try (InputStream io = Client.class.getClassLoader().getResourceAsStream("config.properties")){
			prop.load(io);
		} catch (IOException e) {
			e.printStackTrace();
		}
		server = prop.getProperty("server");
		port = Integer.parseInt(prop.getProperty("port"));
		tempFilePath = prop.getProperty("fileOnClient");

		System.out.println("Login:");
		String login = scanner.nextLine();
		System.out.println("Password:");
		String password = scanner.nextLine();

		int option = -1;

		if (!(login.equals(prop.getProperty("validLogin")) && password.equals(prop.getProperty("validPassword")))){
			System.out.println("Login or password is not correct");
			option = 7;
		}

		while (option != 7) {
			printMenu();
			System.out.print("\nEnter the desired action: ");
			option = Integer.parseInt(scanner.nextLine());
			processRequest(option);
		}
	}

	private static void printMenu(){
		System.out.println("\nMenu:");
		System.out.println("\t \"0\" - To get a list of all students;");
		System.out.println("\t \"1\" - To get a list of students by name;");
		System.out.println("\t \"2\" - To get the student information by id;");
		System.out.println("\t \"3\" - To add a new student;");
		System.out.println("\t \"4\" - To delete the student by id;");
		System.out.println("\t \"5\" - To update temp file and print its content;");
		System.out.println("\t \"6\" - To send file back to server (also updates temp file);");
		System.out.println("\t \"7\" - To quit (temp file will be deleted);");
	}

	private static void processRequest(int option) {
		try (Socket socket = new Socket(server, port)){

			inputStream = new DataInputStream(socket.getInputStream());
			outputStream = new DataOutputStream(socket.getOutputStream());

			if (!fileIsUpToDate && option != 7){
				outputStream.writeUTF("SERVER_SENDS_FILE_TO_CLIENT");
				receiveFile();
				students = parseJSONtoJavaObject(tempFilePath);
				fileIsUpToDate = true;
			} else{
				if (option != 6 && option != 7){
					outputStream.writeUTF("SKIP");
				}
			}

			switch (option){
				case 0:
					System.out.println("List of Students:");
					getAllStudents(students);
					break;
				case 1:
					System.out.println("List of Students. Student name:");
					getStudentsByName(students, scanner.nextLine());
					break;
				case 2:
					System.out.println("Student information. Student id:");
					getInformationById(students, Integer.parseInt(scanner.nextLine()));
					break;
				case 3:
					System.out.println("Add a student. Student name:");
					addNewStudent(students, scanner.nextLine());
					break;
				case 4:
					System.out.println("Delete a student. Student id:");
					deleteStudentById(students, Integer.parseInt(scanner.nextLine()));
					break;
				case 5:
					System.out.println("Update temp file.");
					updateTempFile();
					break;
				case 6:
					System.out.println("Send file back to server.\n");
					outputStream.writeUTF("CLIENT_SENDS_FILE_BACK_TO_SERVER");
					sendFileBackToServer();
					fileIsUpToDate = false;
					break;
				case 7:
					System.out.println("Exit.");
					outputStream.writeUTF("STOP");
					inputStream.close();
					outputStream.close();
					scanner.close();
					break;
				default:
					System.out.println("Illegal option. Please try again.");
			}
		} catch (IOException e){
			e.printStackTrace();
		}
	}

	private static void receiveFile(){
		byte[] temp = new byte[Integer.parseInt(prop.getProperty("maxFileSize"))];
		int numberOfBytesRead = 0;
		try (FileOutputStream os = new FileOutputStream(tempFilePath, false)){
			while((numberOfBytesRead = inputStream.read(temp, 0, temp.length)) != -1)
			{
				os.write(temp, 0, numberOfBytesRead);
			}
			File file = new File(tempFilePath);
			file.deleteOnExit();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void sendFileBackToServer() throws IOException {
		updateTempFile();

		File file = new File(tempFilePath);
		byte[] bytes = new byte[(int) file.length()];
		try (FileInputStream io = new FileInputStream(file)){
			io.read(bytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
		outputStream.write(bytes);
	}

	static Students parseJSONtoJavaObject(String fileName) {
		Path path = Paths.get(fileName);
		StringBuilder stringBuilder = new StringBuilder();

		try (Scanner scanner = new Scanner(path)){
			while(scanner.hasNextLine()){
				stringBuilder.append(scanner.nextLine());
				stringBuilder.append(LINE_SEPARATOR);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("\nFile from the server: \n");
		System.out.println(stringBuilder);

		Pattern pattern = Pattern.compile("\"id\"\\s*:\\s*(.*?),\\s*\"name\"\\s*:\\s*\"(.*?)\"");
		Matcher matcher = pattern.matcher(stringBuilder);
		int id;
		String name;
		Students students = new Students();
		while(matcher.find()){
			id = Integer.parseInt(matcher.group(1));
			name = matcher.group(2);
			students.addStudent(new Student(id, name));
		}

		return students;
	}

	static StringBuilder parseStudentsObjectToJson(Students students){
		Student student;

		StringBuilder stringBuilder = new StringBuilder("{\n" +
				"  \"students\": [");
		String studentString = "    {\n" +
				"      \"id\": %d,\n" +
				"      \"name\": \"%s\"\n" +
				"    }";
		String end = "  ]\n" +
				"}";

		List<Student> sortedStudentsList = students.getStudents().stream().sorted(Comparator.comparing(Student::getId)).collect(Collectors.toList());

		if (!sortedStudentsList.isEmpty()){
			for(int i = 0; i < sortedStudentsList.size() - 1; i++){
				student = sortedStudentsList.get(i);
				stringBuilder.append(LINE_SEPARATOR);

				stringBuilder.append(String.format(studentString, student.getId(), student.getName()));
				stringBuilder.append(",");
			}
			student = sortedStudentsList.get(sortedStudentsList.size()-1);
			stringBuilder.append(LINE_SEPARATOR);
			stringBuilder.append(String.format(studentString, student.getId(), student.getName()));
			stringBuilder.append(LINE_SEPARATOR);
		} else{
			stringBuilder.append(LINE_SEPARATOR);
		}
		stringBuilder.append(end);

		return stringBuilder;
	}

	private static void updateTempFile(){
		StringBuilder stringBuilder = parseStudentsObjectToJson(students);
		try (PrintWriter pw = new PrintWriter(new FileOutputStream(tempFilePath, false))){
			pw.println(stringBuilder);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println(stringBuilder);
	}

	static List<Student> getStudentsByName(Students students, String studentName){
		List<Student> s = students.getStudents();
		List<Student> result = s.stream().filter(i -> i.getName().equals(studentName)).sorted(Comparator.comparing(Student::getId)).collect(Collectors.toList());
		System.out.printf("Students with name \"%s\": ", studentName);
		System.out.println(result);
		return result;
	}

	static List<Student> getAllStudents(Students students){
		List<Student> s = students.getStudents();
		List<Student> result = s.stream().sorted(Comparator.comparing(Student::getName).thenComparing(Student::getId)).collect(Collectors.toList());
		System.out.println(result);
		return result;
	}

	static Student getInformationById(Students students, int studentId){
		List<Student> s = students.getStudents();
		Student result = s.stream().filter(i -> i.getId() == studentId).findFirst().orElse(null);
		System.out.printf("Information about the student with id #%d: ", studentId);
		System.out.println(result);
		return result;
	}

	static Student addNewStudent(Students students, String newStudentName){
		Student newStudent = new Student(students.findAvailableId(), newStudentName);
		students.addStudent(newStudent);
		System.out.println("Added a new student:");
		System.out.println(newStudent);
		return newStudent;
	}

	static Student deleteStudentById(Students students, int studentId){
		List<Student> s = students.getStudents();
		Student deletedStudent = s.stream().filter(i -> i.getId() == studentId).findFirst().orElse(null);
		if (students.getTakenIds().contains(studentId)){
			students.deleteStudent(studentId);
		}
		System.out.printf("Deleted the student with id #%d:\n", studentId);
		System.out.println(deletedStudent);
		return deletedStudent;
	}
}

