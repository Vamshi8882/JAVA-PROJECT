import java.awt.*;
import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class LMSApp extends JFrame {
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin123";
    private static final String STUDENT_USERNAME = "student";
    private static final String STUDENT_PASSWORD = "student123";
    
    private CourseManager courseManager;
    private DoubtSection doubtSection;
    private LiveClassManager liveClassManager;
    private TestManager testManager;
    private FileUploader fileUploader;

    // User role (student or lecturer)
    private String userRole;

    public LMSApp() {
        courseManager = new CourseManager();
        doubtSection = new DoubtSection();
        liveClassManager = new LiveClassManager();
        testManager = new TestManager();
        fileUploader = new FileUploader();
        
        setTitle("Online Learning Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        showLoginScreen();
    }

    // Display login screen
    private void showLoginScreen() {
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new GridLayout(3, 2));
        
        JLabel usernameLabel = new JLabel("Username: ");
        JLabel passwordLabel = new JLabel("Password: ");
        
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            
            if (validateLogin(username, password)) {
                userRole = (username.equals(ADMIN_USERNAME)) ? "LECTURER" : "STUDENT";
                showMainMenu();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials!");
            }
        });
        
        loginPanel.add(usernameLabel);
        loginPanel.add(usernameField);
        loginPanel.add(passwordLabel);
        loginPanel.add(passwordField);
        loginPanel.add(new JLabel());
        loginPanel.add(loginButton);
        
        getContentPane().removeAll();
        getContentPane().add(loginPanel);
        revalidate();
        repaint();
    }

    // Validate login credentials
    private boolean validateLogin(String username, String password) {
        return (username.equals(ADMIN_USERNAME) && password.equals(ADMIN_PASSWORD)) ||
               (username.equals(STUDENT_USERNAME) && password.equals(STUDENT_PASSWORD));
    }

    // Main menu after login
    private void showMainMenu() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JTextArea welcomeText = new JTextArea("Welcome, " + userRole + "!");
        welcomeText.setEditable(false);

        JButton manageCoursesButton = new JButton("Manage Courses");
        manageCoursesButton.addActionListener(e -> showCourseManagement());
        
        JButton uploadFilesButton = new JButton("Upload Files");
        uploadFilesButton.addActionListener(e -> fileUploader.uploadFile());
        
        JButton viewDoubtsButton = new JButton("View Doubt Section");
        viewDoubtsButton.addActionListener(e -> showDoubtSection());

        JButton liveClassesButton = new JButton("Live Classes");
        liveClassesButton.addActionListener(e -> showLiveClasses());

        JButton onlineTestButton = new JButton("Online Test Practice");
        onlineTestButton.addActionListener(e -> showOnlineTests());

        // Adding images (Educational pictures)
        ImageIcon bookIcon = new ImageIcon("C:\\Users\\gudal\\OneDrive\\Desktop\\LMSAppproject\\Education.jpg");  // Add an image path here
        JLabel bookLabel = new JLabel(bookIcon);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 5));
        buttonPanel.add(manageCoursesButton);
        buttonPanel.add(uploadFilesButton);
        buttonPanel.add(viewDoubtsButton);
        buttonPanel.add(liveClassesButton);
        buttonPanel.add(onlineTestButton);
        
        mainPanel.add(welcomeText, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        mainPanel.add(bookLabel, BorderLayout.SOUTH);  // Display book image at the bottom

        getContentPane().removeAll();
        getContentPane().add(mainPanel);
        revalidate();
        repaint();
    }

    // Show course management (Lecturer only)
    private void showCourseManagement() {
        if (!userRole.equals("LECTURER")) {
            JOptionPane.showMessageDialog(this, "Only lecturers can manage courses.");
            return;
        }
        
        JPanel coursePanel = new JPanel();
        coursePanel.setLayout(new BorderLayout());

        JTextArea courseList = new JTextArea();
        courseList.setEditable(false);
        
        JButton addCourseButton = new JButton("Add Course");
        addCourseButton.addActionListener(e -> {
            String courseName = JOptionPane.showInputDialog(this, "Enter Course Name:");
            String instructor = JOptionPane.showInputDialog(this, "Enter Instructor Name:");
            String description = JOptionPane.showInputDialog(this, "Enter Course Description:");
            
            Course course = new Course(courseName, instructor, description);
            courseManager.addCourse(course);
            courseList.append(course.toString() + "\n");
        });

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> showMainMenu());

        coursePanel.add(new JScrollPane(courseList), BorderLayout.CENTER);
        coursePanel.add(addCourseButton, BorderLayout.SOUTH);
        coursePanel.add(backButton, BorderLayout.NORTH);

        getContentPane().removeAll();
        getContentPane().add(coursePanel);
        revalidate();
        repaint();
    }

    // Show doubt section (Student/Teacher)
    private void showDoubtSection() {
        JPanel doubtPanel = new JPanel();
        doubtPanel.setLayout(new BorderLayout());

        JTextArea doubtList = new JTextArea();
        doubtList.setEditable(false);

        JButton askQuestionButton = new JButton("Ask a Question");
        askQuestionButton.addActionListener(e -> {
            String question = JOptionPane.showInputDialog(this, "Enter your question:");
            doubtSection.addQuestion(question);
            doubtList.append(question + "\n");
        });

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> showMainMenu());

        doubtPanel.add(new JScrollPane(doubtList), BorderLayout.CENTER);
        doubtPanel.add(askQuestionButton, BorderLayout.SOUTH);
        doubtPanel.add(backButton, BorderLayout.NORTH);

        getContentPane().removeAll();
        getContentPane().add(doubtPanel);
        revalidate();
        repaint();
    }
    
// Show live classes section (Student/Lecturer)
private void showLiveClasses() {
    JPanel liveClassPanel = new JPanel();
    liveClassPanel.setLayout(new BorderLayout());

    JTextArea classList = new JTextArea();
    classList.setEditable(false);

    // Adding mock live classes
    liveClassManager.addClass("Java Basics", "Instructor sai nath", "2024-11-25 10:00 AM");
    liveClassManager.addClass("Advanced Java", "Instructor senthil", "2024-11-26 02:00 PM");

    for (LiveClass liveClass : liveClassManager.getClasses()) {
        classList.append(liveClass.toString() + "\n");
    }

    // Adding clickable live class links
    JButton javaBasicsLink = new JButton("Join Java Basics Class");
    javaBasicsLink.addActionListener(e -> openLiveClass("Java Basics", "https://youtu.be/XDoJM-Q0TFY?si=Aq9R3lcdShujyv-v"));

    JButton advancedJavaLink = new JButton("Join Advanced Java Class");
    advancedJavaLink.addActionListener(e -> openLiveClass("Advanced Java", "https://youtu.be/Ae-r8hsbPUo?si=lFjr7KbJhaW2gwFF"));

    JPanel liveClassLinksPanel = new JPanel();
    liveClassLinksPanel.setLayout(new GridLayout(1, 2));
    liveClassLinksPanel.add(javaBasicsLink);
    liveClassLinksPanel.add(advancedJavaLink);

    JButton backButton = new JButton("Back");
    backButton.addActionListener(e -> showMainMenu());

    liveClassPanel.add(new JScrollPane(classList), BorderLayout.CENTER);
    liveClassPanel.add(liveClassLinksPanel, BorderLayout.SOUTH);
    liveClassPanel.add(backButton, BorderLayout.NORTH);

    getContentPane().removeAll();
    getContentPane().add(liveClassPanel);
    revalidate();
    repaint();
}

// Show online tests section (Student/Lecturer)
private void showOnlineTests() {
    JPanel testPanel = new JPanel();
    testPanel.setLayout(new BorderLayout());

    JTextArea testList = new JTextArea();
    testList.setEditable(false);

    // Example of adding mock tests for practice
    testManager.addTest("Java Basic Test");
    testManager.addTest("Advanced Java Test");

    for (String test : testManager.getTests()) {
        testList.append(test + "\n");
    }

    // Adding clickable test links
    JButton javaBasicTestLink = new JButton("Take Java Basic Test");
    javaBasicTestLink.addActionListener(e -> openTest("Java Basic Test", "https://test.sanfoundry.com/java-programming-tests"));

    JButton advancedJavaTestLink = new JButton("Take Advanced Java Test");
    advancedJavaTestLink.addActionListener(e -> openTest("Advanced Java Test", "https://www.codechef.com/practice/java"));

    JPanel testLinksPanel = new JPanel();
    testLinksPanel.setLayout(new GridLayout(1, 2));
    testLinksPanel.add(javaBasicTestLink);
    testLinksPanel.add(advancedJavaTestLink);

    JButton backButton = new JButton("Back");
    backButton.addActionListener(e -> showMainMenu());

    testPanel.add(new JScrollPane(testList), BorderLayout.CENTER);
    testPanel.add(testLinksPanel, BorderLayout.SOUTH);
    testPanel.add(backButton, BorderLayout.NORTH);

    getContentPane().removeAll();
    getContentPane().add(testPanel);
    revalidate();
    repaint();
}

// Method to simulate opening a live class with a URL
private void openLiveClass(String className, String url) {
    try {
        // Check if Desktop is supported and open the URL in the default browser
        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().browse(new URI(url));
        } else {
            JOptionPane.showMessageDialog(this, "Desktop API is not supported on this system.");
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error opening URL: " + e.getMessage());
    }
}

// Method to simulate opening a test with a URL
private void openTest(String testName, String url) {
    try {
        // Check if Desktop is supported and open the URL in the default browser
        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().browse(new URI(url));
        } else {
            JOptionPane.showMessageDialog(this, "Desktop API is not supported on this system.");
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error opening URL: " + e.getMessage());
    }
}
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LMSApp app = new LMSApp();
            app.setVisible(true);
        });
    }
}

// Course Model
class Course {
    private String courseName;
    private String instructor;
    private String description;

    public Course(String courseName, String instructor, String description) {
        this.courseName = courseName;
        this.instructor = instructor;
        this.description = description;
    }

    @Override
    public String toString() {
        return "Course Name: " + courseName + "\nInstructor: " + instructor + "\nDescription: " + description;
    }
}

// Course Management Class
class CourseManager {
    private List<Course> courses;

    public CourseManager() {
        this.courses = new ArrayList<>();
    }

    public void addCourse(Course course) {
        courses.add(course);
    }

    public List<Course> getAllCourses() {
        return courses;
    }
}

// Doubt Section
class DoubtSection {
    private List<String> questions;

    public DoubtSection() {
        this.questions = new ArrayList<>();
    }

    public void addQuestion(String question) {
        questions.add(question);
    }

    public List<String> getAllQuestions() {
        return questions;
    }
}

// Live Class Manager
class LiveClassManager {
    private List<LiveClass> classes;

    public LiveClassManager() {
        this.classes = new ArrayList<>();
    }

    public void addClass(String title, String instructor, String schedule) {
        LiveClass liveClass = new LiveClass(title, instructor, schedule);
        classes.add(liveClass);
    }

    public List<LiveClass> getClasses() {
        return classes;
    }
}

class LiveClass {
    private String title;
    private String instructor;
    private String schedule;

    public LiveClass(String title, String instructor, String schedule) {
        this.title = title;
        this.instructor = instructor;
        this.schedule = schedule;
    }

    @Override
    public String toString() {
        return "Live Class: " + title + "\nInstructor: " + instructor + "\nSchedule: " + schedule;
    }
}

// Test Manager
class TestManager {
    private List<String> tests;

    public TestManager() {
        this.tests = new ArrayList<>();
    }

    public void addTest(String test) {
        tests.add(test);
    }

    public List<String> getTests() {
        return tests;
    }
}

// File Uploader Simulation
class FileUploader {
    public void uploadFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select File to Upload");
        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            System.out.println("File uploaded: " + file.getAbsolutePath());
        }
    }
}
