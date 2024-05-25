import java.util.*;
import java.sql.*;
class StudentRegister
{
    private static Scanner sc=new Scanner(System.in);
    public static void main(String[] args) 
    {        
        System.out.println("\n=============== STUDENT COURSE REGISTRATION SYSTEM =================== "); 
        int choice;
        do
        {
            System.out.println("\n1. List of Available Course");
            System.out.println("2. Add Course");
            System.out.println("3. Register for Students for Courses");
            System.out.println("4. View the Registered Course Details");
            System.out.println("5. Drop a Course");
            System.out.println("6. Exit");
            System.out.println("Enter your choice: ");
            choice=sc.nextInt();
            sc.nextLine();
            switch(choice)
            {
                case 1 -> availableCourses();
                case 2 -> addCourse();
                case 3 -> registerStudent();
                case 4 -> viewStudentsRegistrations();
                case 5 -> removeStudent();
                case 6 -> System.out.println("Existing..........");
                default -> System.out.println("Invalid Input"); 
            }
        }while(choice!=6);          
    }
    private static void availableCourses() 
    {
        try 
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection cn=DriverManager.getConnection("jdbc:mysql://localhost:3306/student", "root", "devansh93215");
            String qry="select *,(select count(*) from stud where course_id=course.course_id) as registered_count from course;";
            Statement st=cn.createStatement();
            ResultSet rs=st.executeQuery(qry);
            if(!rs.isBeforeFirst())
            {
                System.out.println("There are no available courses.");
            } 
            else 
            {
                while(rs.next()) 
                {
                    int c_id=rs.getInt("course_id");
                    String c_code=rs.getString("course_code");
                    String c_title=rs.getString("course_title");
                    String c_description=rs.getString("course_description");
                    String c_schedule=rs.getString("course_schedule");
                    int c_capacity=rs.getInt("course_capacity");
                    int registeredCount=rs.getInt("registered_count");

                    System.out.println("List of Available Courses:");
                    if(c_capacity>registeredCount)
                    {                        
                        System.out.println("Course Id: "+c_id);
                        System.out.println("Course Code: " + c_code);
                        System.out.println("Course Title: " + c_title);
                        System.out.println("Course Description: " + c_description);
                        System.out.println("Course Schedule: " + c_schedule);
                        System.out.println("For "+c_title+" Course "+registeredCount+" Student are Registered. And This Course Capacity is "+(c_capacity-registeredCount));
                        System.out.println();
                    }                               
                }
            }
        } 
        catch (Exception e) 
        {
            System.out.println(e);
        }
    }
    private static void addCourse() 
    {
        try{
            String c_code,c_title,c_description,c_schedule;
            int c_capacity;
            System.out.println("Enter a Course Code:");
            c_code=sc.nextLine();
            System.out.println("Enter a Course Title:");
            c_title=sc.nextLine();
            System.out.println("Enter a Course Description:");
            c_description=sc.nextLine();
            System.out.println("Enter a Course Capacity:");
            c_capacity=sc.nextInt();
            sc.nextLine();
            System.out.println("Enter a Cousre Schedule:");
            c_schedule=sc.nextLine();
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection cn=DriverManager.getConnection("jdbc:mysql://localhost:3306/student","root","devansh93215");
            String qry="insert into course(course_code,course_title,course_description,course_capacity,course_schedule)"+
            "values(?,?,?,?,?)";
            PreparedStatement ps=cn.prepareStatement(qry);
            ps.setString(1,c_code);
            ps.setString(2,c_title);
            ps.setString(3,c_description);
            ps.setInt(4,c_capacity);
            ps.setString(5,c_schedule);
            int rowInserted=ps.executeUpdate();
            if(rowInserted>0)
            {
                System.out.println("Course added Successfully");
            }
            else
            {
                System.out.println("Fail to add the course");
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }
    private static boolean checkCourseCapacity(int register_course_id) 
    {
    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/student", "root", "devansh93215");
        String qry = "SELECT course_capacity, (SELECT COUNT(*) FROM stud WHERE course_id = ?) AS registered_count " +
                     "FROM course WHERE course_id = ?";
        PreparedStatement ps = cn.prepareStatement(qry);
        ps.setInt(1, register_course_id);
        ps.setInt(2, register_course_id);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            int capacity = rs.getInt("course_capacity");
            int registeredCount = rs.getInt("registered_count");
            rs.close();
            ps.close();
            cn.close();
            return capacity > registeredCount;
        }
        rs.close();
        ps.close();
        cn.close();
    } catch (Exception e) {
        System.out.println(e);
    }
    return false;
    }
    private static boolean checkCourseExist(int register_course_id) 
    {  
    try
    { 
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection cn=DriverManager.getConnection("jdbc:mysql://localhost:3306/student","root","devansh93215"); 
        
        //check if the course exists
        String checkCourseQuery="select count(*) from course where course_id=?";
        PreparedStatement checkCourseStmt=cn.prepareStatement(checkCourseQuery);
        checkCourseStmt.setInt(1,register_course_id);
        ResultSet courseRs=checkCourseStmt.executeQuery();
        courseRs.next();
        int courseCount=courseRs.getInt(1);
        courseRs.close();
        checkCourseStmt.close();
        cn.close();
        return courseCount>0;
    }
    catch(Exception e)
    {
        System.out.println(e);
    }
    return false;
    }
    private static boolean checkStudentExist(int register_stud_id)
    {
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection cn=DriverManager.getConnection("jdbc:mysql://localhost:3306/student","root","devansh93215"); 
            
            //check if the student exists
            String checkStudentQuery="select count(*) from stud where stud_id=?";
            PreparedStatement checkStudentStmt=cn.prepareStatement(checkStudentQuery);
            checkStudentStmt.setInt(1,register_stud_id);
            ResultSet rs=checkStudentStmt.executeQuery();
            rs.next();
            int studentCount=rs.getInt(1);
            rs.close();
            checkStudentStmt.close();
            cn.close();
            return studentCount>0;
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        return false;
    }
    private static void registerStudent()
    {
        try
        {   
            int register_course_id;
            String register_stud_name;
            System.out.println("Enter a Student Name:");
            register_stud_name=sc.nextLine();
            System.out.println("Enter Course Id to register for courses:");
            register_course_id=sc.nextInt();
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection cn=DriverManager.getConnection("jdbc:mysql://localhost:3306/student","root","devansh93215");
            if(checkCourseExist(register_course_id)==false)
            {
                System.out.println("The course Id: "+register_course_id+" does not exists, Please enter valid ID's");
            }
            else if(checkCourseCapacity(register_course_id)==false)
            {
                System.out.println("Sorry, the course with ID "+register_course_id+" is full. You cannot register for this course.");
            }
            else
            {
                //Register student with student name and course id
                String qry="insert into stud(stud_name,course_id) values(?,?)";
                PreparedStatement registerStmt=cn.prepareStatement(qry,Statement.RETURN_GENERATED_KEYS);
                registerStmt.setString(1,register_stud_name);
                registerStmt.setInt(2,register_course_id);
                int rowInserted=registerStmt.executeUpdate();
                
                if(rowInserted>0)
                {
                    ResultSet rs=registerStmt.getGeneratedKeys();
                    if(rs.next())
                    {
                        int genrateStudentId=rs.getInt(1);
                        System.out.println("You are registered for selected course Id and your Student Id is: "+genrateStudentId);
                    }
                   
                }
                else
                {
                    System.out.println("Failed to register student for the course");
                }
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }
    private static void removeStudent()
    {
        try
        {
            System.out.println("Enter Student Id:");
            int stud_id=sc.nextInt();
            if(checkStudentExist(stud_id)==false)
            {
                System.out.println("The student Id: "+stud_id+" doesn't exists, Please register first.");
            }
            else
            {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection cn=DriverManager.getConnection("jdbc:mysql://localhost:3306/student","root","devansh93215");
                String qry="Delete from stud where stud_id=?";
                PreparedStatement ps=cn.prepareStatement(qry);
                ps.setInt(1,stud_id);
                int rowDeleted=ps.executeUpdate();
                if(rowDeleted>0)
                {
                    System.out.println("Registration remove Successfully");
                }
                else
                {
                    System.out.println("Failed to remove Registration");
                }
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }
    private static void viewStudentsRegistrations() 
    {
        try
        {
            System.out.println("Enter Student Id:");
            int stud_id=sc.nextInt();
            if(checkStudentExist(stud_id)==false)
            {
                System.out.println("The student Id: "+stud_id+" doesn't exists, Please register first.");
            }
            else
            {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection cn=DriverManager.getConnection("jdbc:mysql://localhost:3306/student","root","devansh93215");
                String qry="SELECT s.stud_name, c.course_code, c.course_title " +
                        "FROM stud s " +
                        "JOIN course c ON s.course_id = c.course_id " +
                        "WHERE s.stud_id = ?";
                PreparedStatement ps=cn.prepareStatement(qry);
                ps.setInt(1,stud_id);
                ResultSet rs=ps.executeQuery();
                System.out.println("Details of Registered Course for Student Id: "+stud_id);
                while(rs.next())
                {
                    String stud_name=rs.getString("stud_name");
                    String c_code=rs.getString("course_code");
                    String c_title=rs.getString("course_title");
    
                    System.out.println("\nStudent Name: "+stud_name);
                    System.out.println("Registered Course Code: "+c_code);
                    System.out.println("Registered Course Name: "+c_title);
                }
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }
}