package com.example.studentTrack.attendancemanager;

import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;


public class Database {

    private String url = "jdbc:mysql://192.168.42.97:3306/attendancemanager";
    private String user = "krishna";
    private String pass = "123456";
    private Statement statement;

    public Database() {
        try {
            java.lang.Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, user, pass);
            statement = connection.createStatement();
        } catch (Exception e) {
            Log.e("checkinggggg", "database "+e.toString());
            e.printStackTrace();
        }
    }

    public ArrayList<Class> getAllClasses() {
        ArrayList<Class> classes = new ArrayList<>();
        try {
            String select = "SELECT * FROM `classes`;";

            ResultSet rs = statement.executeQuery(select);
            while (rs.next()) {
               Class c = new Class(rs.getInt("ID"), rs.getString("ClassName"));
                classes.add(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return classes;
    }

    public int getNextClassID() {
        int id = 0;
        ArrayList<Class> classes = getAllClasses();
        int size = classes.size();
        if (size!=0) {
            int last = size-1;
            Class lastClass = classes.get(last);
            id = lastClass.getID() + 1;
        }
        return id;
    }

    public void addClass(Class c) {
        try {
            String createClassTable = "CREATE TABLE IF NOT EXISTS `classes` (`ID` integer, `ClassName` text);";
            statement.execute(createClassTable);
            String insert = "INSERT INTO `classes`(`ID`, `ClassName`) VALUES ('"+
                    c.getID()+"','"+c.getClassName()+"');";
            statement.execute(insert);
            String create1 = "CREATE TABLE IF NOT EXISTS `"+c.getID()
                    +" - Sessions` (`ID` integer, `Subject` text, `Date` text);";
            statement.execute(create1);
            String create2 = "CREATE TABLE IF NOT EXISTS `"+c.getID()
                    +" - Students` (`ID` integer, `FirstName` text, `LastName` text, `Email` text, `Tel` text);";
            statement.execute(create2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteClass(int id) {
        try {
            String delete = "DELETE FROM `classes` WHERE `ID` = "+id+" ;";
            statement.execute(delete);
            for (Session s : getSessions(id)) {
                deleteSession(id, s.getID());
            }
            String drop1 = "DROP TABLE `"+id+" - Sessions`;";
            String drop2 = "DROP TABLE `"+id+" - Students`;";
            statement.execute(drop1);
            statement.execute(drop2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Student> getStudents(int id) {
        ArrayList<Student> students = new ArrayList<>();
        try {
            String select = "SELECT * FROM `"+id+" - Students`;";
            ResultSet rs = statement.executeQuery(select);
            while (rs.next()) {
                Student s = new Student();
                s.setID(rs.getInt("ID"));
                s.setFirstName(rs.getString("FirstName"));
                s.setLastName(rs.getString("LastName"));
                s.setEmail(rs.getString("Email"));
                s.setTel(rs.getString("Tel"));
                students.add(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return students;
    }

    public int getNextStudentID(int classID) {
        int id = 0;
        ArrayList<Student> students = getStudents(classID);
        int size = students.size();
        if (size!=0) {
            int last = size-1;
            Student lastStudent = students.get(last);
            id = lastStudent.getID() + 1;
        }
        return id;
    }

    public Student getStudent(int classID, int studentID) {
        Student student = new Student();
        try {
            String select = "SELECT `ID`, `FirstName`, `LastName`, `Email`, `Tel` FROM `"+
                    classID+" - Students` WHERE `ID` = "+studentID+" ;";
            ResultSet rs = statement.executeQuery(select);
            rs.next();
            student.setID(rs.getInt("ID"));
            student.setFirstName(rs.getString("FirstName"));
            student.setLastName(rs.getString("LastName"));
            student.setEmail(rs.getString("Email"));
            student.setTel(rs.getString("Tel"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return student;
    }

    public void addStudent(int classID, Student s) {
        try {
            String insert = "INSERT INTO `"+classID+" - Students` " +
                    "(`ID`, `FirstName`, `LastName`, `Email`, `Tel`) VALUES " +
                    "('"+s.getID()+"', '"+s.getFirstName()+"', '"+s.getLastName()+"', '"+
                    s.getEmail()+"', '"+s.getTel()+"');";
            statement.execute(insert);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateStudent(int classID, Student s) {
        try {
            String update = "UPDATE `"+classID+" - Students` SET `FirstName` = '"+
                    s.getFirstName()+"', `LastName` = '"+s.getLastName()+"', `Email` = '"+
                    s.getEmail()+"', `Tel` = '"+s.getTel()+"' WHERE `ID` = "+s.getID()+" ;";
            statement.execute(update);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteStudent(int classID, int studentID) {
        try {
            String delete = "DELETE FROM `"+classID+" - Students` WHERE `ID` = "+studentID+" ;";
            statement.execute(delete);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Session> getSessions(int classID) {
        ArrayList<Session> sessions = new ArrayList<>();
        try {
            String select = "SELECT * FROM `"+classID+" - Sessions`;";
            ResultSet rs = statement.executeQuery(select);
            while (rs.next()) {
                Session s = new Session();
                s.setID(rs.getInt("ID"));
                s.setSubject(rs.getString("Subject"));
                s.setDate(rs.getString("Date"));
                sessions.add(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sessions;
    }

    public int getNextSessionID(int classID) {
        int id = 0;
        ArrayList<Session> sessions = getSessions(classID);
        int size = sessions.size();
        if (size != 0) {
            int last = size-1;
            Session lastSession = sessions.get(last);
            id = lastSession.getID() + 1;
        }
        return id;
    }

    public void addSession(int classID, Session s) {
        try {
            String insert = "INSERT INTO `"+classID+" - Sessions` (`ID`, `Subject`, `Date`) " +
                    "VALUES ('"+s.getID()+"', '"+s.getSubject()+"', '"+s.getDate()+"');";
            statement.execute(insert);
            String create = "CREATE TABLE IF NOT EXISTS `"+classID+" - "+s.getID()+"` (`ID` integer) ;";
            statement.execute(create);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Session getSession(int classID, int sessionID) {
        Session s = new Session();
        try {
            String select = "SELECT `ID`, `Subject`, `Date` FROM `"+classID+
                    " - Sessions` WHERE `ID` = "+sessionID+" ;";
            ResultSet rs = statement.executeQuery(select);
            rs.next();
            s.setID(rs.getInt("ID"));
            s.setSubject(rs.getString("Subject"));
            s.setDate(rs.getString("Date"));

            String select2 = "SELECT * FROM `"+classID+" - "+sessionID+"`;";
            ResultSet rs2 = statement.executeQuery(select2);
            ArrayList<Integer> ids = new ArrayList<>();
            while (rs2.next()) {
                ids.add(rs2.getInt("ID"));
            }

            ArrayList<Student> students = new ArrayList<>();
            for (int id : ids) {
                students.add(getStudent(classID, id));
            }
            s.setStudents(students);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }

    public void updateSessionData(int classID, Session s) {
        try {
            String update = "UPDATE `"+classID+" - Sessions` SET `Subject` = '"+
                    s.getSubject()+"', `Date` = '"+s.getDate()+"' WHERE `ID` = "+s.getID()+" ;";
            statement.execute(update);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteSession(int classID, int sessionID) {
        try {
            String delete = "DELETE FROM `"+classID+" - Sessions` WHERE `ID` = "+sessionID+" ;";
            statement.execute(delete);
            String drop = "DROP TABLE `"+classID+" - "+sessionID+"`;";
            statement.execute(drop);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addStudentsToSession(int classID, int sessionID, ArrayList<Integer> students) {
        try {
            for (Integer i : students) {
                String insert = "INSERT INTO `"+classID+" - "+sessionID+"` (`ID`) VALUES ('"+i+"');";
                statement.execute(insert);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeStudentsFromSession(int classID, int sessionID, ArrayList<Integer> students) {
        try {
            for (Integer i : students) {
                String delete = "DELETE FROM `"+classID+" - "+sessionID+"` WHERE `ID` = "+i+" ;";
                statement.execute(delete);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}


//import java.sql.*;
//import java.util.ArrayList;
//
//import android.content.Context;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.ResultSet;
//import java.sql.Statement;
//import java.util.ArrayList;
//
//public class Database extends SQLiteOpenHelper {
//
//    private static final String DATABASE_NAME = "attendancemanager.db";
//    private static final int DATABASE_VERSION = 1;
//    private Statement statement;
//
//    public Database(Context context) {
//        super(context, DATABASE_NAME, null, DATABASE_VERSION);
//        try {
//            SQLiteDatabase db = this.getWritableDatabase();
//            String dbPath = db.getPath();
//            Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
//            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
//        } catch (Exception e) {
//            System.err.println("Database error: " + e.toString());
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void onCreate(SQLiteDatabase db) {
//        // Create initial tables if needed
//        try {
//            String createClassTable = "CREATE TABLE IF NOT EXISTS classes (ID INTEGER PRIMARY KEY, ClassName TEXT);";
//            db.execSQL(createClassTable);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        // Handle database upgrades if needed
//    }
//
//    public ArrayList<Class> getAllClasses() {
//        ArrayList<Class> classes = new ArrayList<>();
//        try {
//            String select = "SELECT * FROM classes;";
//            ResultSet rs = statement.executeQuery(select);
//            while (rs.next()) {
//                Class c = new Class(rs.getInt("ID"), rs.getString("ClassName"));
//                classes.add(c);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return classes;
//    }
//
//    public int getNextClassID() {
//        int id = 0;
//        ArrayList<Class> classes = getAllClasses();
//        int size = classes.size();
//        if (size != 0) {
//            int last = size - 1;
//            Class lastClass = classes.get(last);
//            id = lastClass.getID() + 1;
//        }
//        return id;
//    }
//
//    public void addClass(Class c) {
//        try {
//            String insert = "INSERT INTO classes (ID, ClassName) VALUES (" + c.getID() + ", '" + c.getClassName() + "');";
//            statement.execute(insert);
//            String create1 = "CREATE TABLE IF NOT EXISTS '" + c.getID() + " - Sessions' (ID INTEGER PRIMARY KEY, Subject TEXT, Date TEXT);";
//            statement.execute(create1);
//            String create2 = "CREATE TABLE IF NOT EXISTS '" + c.getID() + " - Students' (ID INTEGER PRIMARY KEY, FirstName TEXT, LastName TEXT, Email TEXT, Tel TEXT);";
//            statement.execute(create2);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void deleteClass(int id) {
//        try {
//            String delete = "DELETE FROM classes WHERE ID = " + id + ";";
//            statement.execute(delete);
//            for (Session s : getSessions(id)) {
//                deleteSession(id, s.getID());
//            }
//            String drop1 = "DROP TABLE IF EXISTS '" + id + " - Sessions';";
//            String drop2 = "DROP TABLE IF EXISTS '" + id + " - Students';";
//            statement.execute(drop1);
//            statement.execute(drop2);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public ArrayList<Student> getStudents(int id) {
//        ArrayList<Student> students = new ArrayList<>();
//        try {
//            String select = "SELECT * FROM '" + id + " - Students';";
//            ResultSet rs = statement.executeQuery(select);
//            while (rs.next()) {
//                Student s = new Student();
//                s.setID(rs.getInt("ID"));
//                s.setFirstName(rs.getString("FirstName"));
//                s.setLastName(rs.getString("LastName"));
//                s.setEmail(rs.getString("Email"));
//                s.setTel(rs.getString("Tel"));
//                students.add(s);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return students;
//    }
//
//    public int getNextStudentID(int classID) {
//        int id = 0;
//        ArrayList<Student> students = getStudents(classID);
//        int size = students.size();
//        if (size != 0) {
//            int last = size - 1;
//            Student lastStudent = students.get(last);
//            id = lastStudent.getID() + 1;
//        }
//        return id;
//    }
//
//    public Student getStudent(int classID, int studentID) {
//        Student student = new Student();
//        try {
//            String select = "SELECT ID, FirstName, LastName, Email, Tel FROM '" + classID + " - Students' WHERE ID = " + studentID + ";";
//            ResultSet rs = statement.executeQuery(select);
//            rs.next();
//            student.setID(rs.getInt("ID"));
//            student.setFirstName(rs.getString("FirstName"));
//            student.setLastName(rs.getString("LastName"));
//            student.setEmail(rs.getString("Email"));
//            student.setTel(rs.getString("Tel"));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return student;
//    }
//
//    public void addStudent(int classID, Student s) {
//        try {
//            String insert = "INSERT INTO '" + classID + " - Students' (ID, FirstName, LastName, Email, Tel) VALUES (" +
//                    s.getID() + ", '" + s.getFirstName() + "', '" + s.getLastName() + "', '" + s.getEmail() + "', '" + s.getTel() + "');";
//            statement.execute(insert);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void updateStudent(int classID, Student s) {
//        try {
//            String update = "UPDATE '" + classID + " - Students' SET FirstName = '" + s.getFirstName() +
//                    "', LastName = '" + s.getLastName() + "', Email = '" + s.getEmail() + "', Tel = '" + s.getTel() +
//                    "' WHERE ID = " + s.getID() + ";";
//            statement.execute(update);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void deleteStudent(int classID, int studentID) {
//        try {
//            String delete = "DELETE FROM '" + classID + " - Students' WHERE ID = " + studentID + ";";
//            statement.execute(delete);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public ArrayList<Session> getSessions(int classID) {
//        ArrayList<Session> sessions = new ArrayList<>();
//        try {
//            String select = "SELECT * FROM '" + classID + " - Sessions';";
//            ResultSet rs = statement.executeQuery(select);
//            while (rs.next()) {
//                Session s = new Session();
//                s.setID(rs.getInt("ID"));
//                s.setSubject(rs.getString("Subject"));
//                s.setDate(rs.getString("Date"));
//                sessions.add(s);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return sessions;
//    }
//
//    public int getNextSessionID(int classID) {
//        int id = 0;
//        ArrayList<Session> sessions = getSessions(classID);
//        int size = sessions.size();
//        if (size != 0) {
//            int last = size - 1;
//            Session lastSession = sessions.get(last);
//            id = lastSession.getID() + 1;
//        }
//        return id;
//    }
//
//    public void addSession(int classID, Session s) {
//        try {
//            String insert = "INSERT INTO '" + classID + " - Sessions' (ID, Subject, Date) VALUES (" +
//                    s.getID() + ", '" + s.getSubject() + "', '" + s.getDate() + "');";
//            statement.execute(insert);
//            String create = "CREATE TABLE IF NOT EXISTS '" + classID + " - " + s.getID() + "' (ID INTEGER PRIMARY KEY);";
//            statement.execute(create);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public Session getSession(int classID, int sessionID) {
//        Session s = new Session();
//        try {
//            String select = "SELECT ID, Subject, Date FROM '" + classID + " - Sessions' WHERE ID = " + sessionID + ";";
//            ResultSet rs = statement.executeQuery(select);
//            rs.next();
//            s.setID(rs.getInt("ID"));
//            s.setSubject(rs.getString("Subject"));
//            s.setDate(rs.getString("Date"));
//
//            String select2 = "SELECT * FROM '" + classID + " - " + sessionID + "';";
//            ResultSet rs2 = statement.executeQuery(select2);
//            ArrayList<Integer> ids = new ArrayList<>();
//            while (rs2.next()) {
//                ids.add(rs2.getInt("ID"));
//            }
//
//            ArrayList<Student> students = new ArrayList<>();
//            for (int id : ids) {
//                students.add(getStudent(classID, id));
//            }
//            s.setStudents(students);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return s;
//    }
//
//    public void updateSessionData(int classID, Session s) {
//        try {
//            String update = "UPDATE '" + classID + " - Sessions' SET Subject = '" + s.getSubject() +
//                    "', Date = '" + s.getDate() + "' WHERE ID = " + s.getID() + ";";
//            statement.execute(update);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void deleteSession(int classID, int sessionID) {
//        try {
//            String delete = "DELETE FROM '" + classID + " - Sessions' WHERE ID = " + sessionID + ";";
//            statement.execute(delete);
//            String drop = "DROP TABLE IF EXISTS '" + classID + " - " + sessionID + "';";
//            statement.execute(drop);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void addStudentsToSession(int classID, int sessionID, ArrayList<Integer> students) {
//        try {
//            for (Integer i : students) {
//                String insert = "INSERT INTO '" + classID + " - " + sessionID + "' (ID) VALUES (" + i + ");";
//                statement.execute(insert);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void removeStudentsFromSession(int classID, int sessionID, ArrayList<Integer> students) {
//        try {
//            for (Integer i : students) {
//                String delete = "DELETE FROM '" + classID + " - " + sessionID + "' WHERE ID = " + i + ";";
//                statement.execute(delete);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
