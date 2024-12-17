import java.util.*;

class Course {
    private String courseCode;
    private String title;
    private String description;
    private int capacity;
    private int enrolled;
    private String schedule;

    public Course(String courseCode, String title, String description, int capacity, String schedule) {
        this.courseCode = courseCode;
        this.title = title;
        this.description = description;
        this.capacity = capacity;
        this.enrolled = 0;
        this.schedule = schedule;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getAvailableSlots() {
        return capacity - enrolled;
    }

    public String getSchedule() {
        return schedule;
    }

    public boolean registerStudent() {
        if (enrolled < capacity) {
            enrolled++;
            return true;
        }
        return false;
    }

    public boolean dropStudent() {
        if (enrolled > 0) {
            enrolled--;
            return true;
        }
        return false;
    }

    public void displayCourseDetails() {
        System.out.println("Code: " + courseCode);
        System.out.println("Title: " + title);
        System.out.println("Description: " + description);
        System.out.println("Schedule: " + schedule);
        System.out.println("Available Slots: " + getAvailableSlots());
    }
}

class Student {
    private String studentID;
    private String name;
    private List<Course> registeredCourses;

    public Student(String studentID, String name) {
        this.studentID = studentID;
        this.name = name;
        this.registeredCourses = new ArrayList<>();
    }

    public String getStudentID() {
        return studentID;
    }

    public String getName() {
        return name;
    }

    public void registerCourse(Course course) {
        if (course.registerStudent()) {
            registeredCourses.add(course);
            System.out.println("Successfully registered for: " + course.getTitle());
        } else {
            System.out.println("Registration failed. No slots available for: " + course.getTitle());
        }
    }

    public void dropCourse(Course course) {
        if (registeredCourses.remove(course)) {
            course.dropStudent();
            System.out.println("Successfully dropped: " + course.getTitle());
        } else {
            System.out.println("You are not registered for: " + course.getTitle());
        }
    }

    public void displayRegisteredCourses() {
        System.out.println("Registered Courses for " + name + ":");
        for (Course course : registeredCourses) {
            System.out.println("- " + course.getTitle() + " (" + course.getCourseCode() + ")");
        }
    }
}

public class CourseManagementSystem {
    private static List<Course> courses = new ArrayList<>();
    private static List<Student> students = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        initializeData();

        while (true) {
            System.out.println("\nCourse Management System");
            System.out.println("1. List Available Courses");
            System.out.println("2. Register for a Course");
            System.out.println("3. Drop a Course");
            System.out.println("4. View Registered Courses");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    listAvailableCourses();
                    break;
                case 2:
                    System.out.print("Enter your Student ID: ");
                    String studentID = scanner.nextLine();
                    Student student = findStudentByID(studentID);
                    if (student != null) {
                        System.out.print("Enter Course Code to Register: ");
                        String courseCode = scanner.nextLine();
                        Course course = findCourseByCode(courseCode);
                        if (course != null) {
                            student.registerCourse(course);
                        } else {
                            System.out.println("Course not found.");
                        }
                    } else {
                        System.out.println("Student not found.");
                    }
                    break;
                case 3:
                    System.out.print("Enter your Student ID: ");
                    studentID = scanner.nextLine();
                    student = findStudentByID(studentID);
                    if (student != null) {
                        System.out.print("Enter Course Code to Drop: ");
                        String courseCode = scanner.nextLine();
                        Course course = findCourseByCode(courseCode);
                        if (course != null) {
                            student.dropCourse(course);
                        } else {
                            System.out.println("Course not found.");
                        }
                    } else {
                        System.out.println("Student not found.");
                    }
                    break;
                case 4:
                    System.out.print("Enter your Student ID: ");
                    studentID = scanner.nextLine();
                    student = findStudentByID(studentID);
                    if (student != null) {
                        student.displayRegisteredCourses();
                    } else {
                        System.out.println("Student not found.");
                    }
                    break;
                case 5:
                    System.out.println("Exiting system. Goodbye!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void initializeData() {
        courses.add(new Course("CS101", "Introduction to Programming", "Learn the basics of programming.", 30, "MWF 9-10AM"));
        courses.add(new Course("CS102", "Data Structures", "Understand data structures in computer science.", 25, "TTh 10-11:30AM"));
        courses.add(new Course("CS103", "Database Systems", "Introduction to databases and SQL.", 20, "MWF 11-12PM"));

        students.add(new Student("S001", "Alice"));
        students.add(new Student("S002", "Bob"));
        students.add(new Student("S003", "Charlie"));
    }

    private static void listAvailableCourses() {
        System.out.println("Available Courses:");
        for (Course course : courses) {
            course.displayCourseDetails();
            System.out.println("-------------------");
        }
    }

    private static Course findCourseByCode(String courseCode) {
        for (Course course : courses) {
            if (course.getCourseCode().equalsIgnoreCase(courseCode)) {
                return course;
            }
        }
        return null;
    }

    private static Student findStudentByID(String studentID) {
        for (Student student : students) {
            if (student.getStudentID().equalsIgnoreCase(studentID)) {
                return student;
            }
        }
        return null;
    }
}