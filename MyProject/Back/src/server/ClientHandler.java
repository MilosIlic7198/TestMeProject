/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server;

import chain.AccountCheck;
import chain.InputCheck;
import chain.LoginHandler;
import dao.AccountAccess;
import dao.AdminAccess;
import dao.InstructorAccess;
import dao.StudentAccess;
import dao.contracts.AccountContract;
import dao.contracts.AdminContract;
import dao.contracts.InstructorContract;
import dao.contracts.StudentContract;
import dto.Account;
import dto.AccountBuilder;
import dto.Director;
import dto.SubmittedQuestion;
import dto.SubmittedTask;
import dto.SubmittedTest;
import dto.TakenBy;
import dto.Test;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author Milos
 */
public class ClientHandler implements Runnable {

    private static ArrayList<ClientHandler> listOfUsers = new ArrayList<>();
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private String usernameEmail;
    private Account acc;
    private AccountContract accountDAO;
    private AdminContract adminDAO;
    private StudentContract studentDAO;
    private InstructorContract instructorDAO;
    private Director director;
    private AccountBuilder accountBuilder;
    private Account newAccount;

    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.in = new ObjectInputStream(socket.getInputStream());
            this.accountDAO = new AccountAccess();
            this.adminDAO = new AdminAccess();
            this.instructorDAO = new InstructorAccess();
            this.studentDAO = new StudentAccess();
            this.director = new Director();
            listOfUsers.add(this);
        } catch (IOException ex) {
            System.out.println("An error has occurred while constructing client handler!");
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {
        String operationType;
        while (!socket.isClosed()) {
            try {
                operationType = (String) in.readObject();
                processRequest(operationType);
            } catch (IOException | ClassNotFoundException ex) {
                close(socket, in, out);
                break;
            }
        }
    }

    private void processRequest(String operationType) {
        try {
            System.out.println(operationType);
            if (operationType.equals("Login!")) {
                LoginHandler inputCheck = new InputCheck();
                LoginHandler accountCheck = new AccountCheck();
                inputCheck.setNextHandler(accountCheck);
                this.usernameEmail = (String) in.readObject();
                String password = (String) in.readObject();
                int check = inputCheck.handle(usernameEmail, password);
                switch (check) {
                    case 1 -> {
                        this.acc = accountDAO.getAccount(usernameEmail);
                        out.writeObject(1);
                        out.writeObject(acc.getAccountType());
                        if (acc.getAccountType() == 'A') {
                            out.writeObject(acc.getAccountId());
                            adminRefresh();
                        }
                        if (acc.getAccountType() == 'I') {
                            instructorRefresh();
                        }
                        if (acc.getAccountType() == 'S') {
                            studentRefresh();
                        }
                    }
                    case -1 ->
                        out.writeObject(-1);
                    case -2 -> {
                        out.writeObject(-2);
                    }
                }
            }
            //Admin
            if (operationType.equals("Add admin!")) {
                String username = (String) in.readObject();
                String password = (String) in.readObject();
                String firstName = (String) in.readObject();
                String lastName = (String) in.readObject();
                String email = (String) in.readObject();
                String phoneNumber = (String) in.readObject();
                accountBuilder = new AccountBuilder();
                director.buildAdminAccount(accountBuilder);
                accountBuilder.usernameEmail(username)
                        .password(password)
                        .firstName(firstName)
                        .lastName(lastName)
                        .email(email)
                        .phoneNumber(phoneNumber)
                        .godId(acc.getAccountId());
                newAccount = accountBuilder.build();
                boolean check = adminDAO.addAccount(newAccount);
                out.writeObject(operationType);
                out.writeObject(check);
            }
            if (operationType.equals("Add instructor!")) {
                String email = (String) in.readObject();
                String password = (String) in.readObject();
                String firstName = (String) in.readObject();
                String lastName = (String) in.readObject();
                String initials = (String) in.readObject();
                String date = (String) in.readObject();
                char gender = (char) in.readObject();
                String phoneNumber = (String) in.readObject();
                accountBuilder = new AccountBuilder();
                director.buildInstructorAccount(accountBuilder);
                accountBuilder.usernameEmail(email)
                        .password(password)
                        .firstName(firstName)
                        .lastName(lastName)
                        .initials(initials)
                        .birthDate(date)
                        .gender(gender)
                        .phoneNumber(phoneNumber)
                        .adminId(acc.getAccountId());
                newAccount = accountBuilder.build();
                boolean check = adminDAO.addAccount(newAccount);
                out.writeObject(operationType);
                out.writeObject(check);
            }
            if (operationType.equals("Add student!")) {
                String email = (String) in.readObject();
                String password = (String) in.readObject();
                String firstName = (String) in.readObject();
                String lastName = (String) in.readObject();
                String city = (String) in.readObject();
                String street = (String) in.readObject();
                int postalCode = (int) in.readObject();
                String date = (String) in.readObject();
                char gender = (char) in.readObject();
                int instructorId = (int) in.readObject();
                accountBuilder = new AccountBuilder();
                director.buildStudentAccount(accountBuilder);
                accountBuilder.usernameEmail(email)
                        .password(password)
                        .firstName(firstName)
                        .lastName(lastName)
                        .city(city)
                        .street(street)
                        .postalCode(postalCode)
                        .birthDate(date)
                        .gender(gender)
                        .instructorId(instructorId)
                        .adminId(acc.getAccountId());
                newAccount = accountBuilder.build();
                boolean check = adminDAO.addAccount(newAccount);
                out.writeObject(operationType);
                out.writeObject(check);
            }
            if (operationType.equals("Check username!")) {
                String username = (String) in.readObject();
                boolean check = adminDAO.checkUsername(username);
                out.writeObject(operationType);
                out.writeObject(check);
            }
            if (operationType.equals("Edit admin!")) {
                String username = (String) in.readObject();
                String password = (String) in.readObject();
                String firstName = (String) in.readObject();
                String lastName = (String) in.readObject();
                String email = (String) in.readObject();
                String phoneNumber = (String) in.readObject();
                int id = (int) in.readObject();
                accountBuilder = new AccountBuilder();
                director.buildAdminAccount(accountBuilder);
                accountBuilder.accountId(id)
                        .usernameEmail(username)
                        .password(password)
                        .firstName(firstName)
                        .lastName(lastName)
                        .email(email)
                        .phoneNumber(phoneNumber);
                newAccount = accountBuilder.build();
                boolean check = adminDAO.editAccount(newAccount);
                out.writeObject(operationType);
                out.writeObject(check);
            }
            if (operationType.equals("Edit instructor!")) {
                String email = (String) in.readObject();
                String password = (String) in.readObject();
                String firstName = (String) in.readObject();
                String lastName = (String) in.readObject();
                String initials = (String) in.readObject();
                String phoneNumber = (String) in.readObject();
                int id = (int) in.readObject();
                accountBuilder = new AccountBuilder();
                director.buildInstructorAccount(accountBuilder);
                accountBuilder.accountId(id)
                        .usernameEmail(email)
                        .password(password)
                        .firstName(firstName)
                        .lastName(lastName)
                        .initials(initials)
                        .phoneNumber(phoneNumber);
                newAccount = accountBuilder.build();
                boolean check = adminDAO.editAccount(newAccount);
                out.writeObject(operationType);
                out.writeObject(check);
            }
            if (operationType.equals("Edit student!")) {
                int id = (int) in.readObject();
                String email = (String) in.readObject();
                String password = (String) in.readObject();
                String firstName = (String) in.readObject();
                String lastName = (String) in.readObject();
                String city = (String) in.readObject();
                String street = (String) in.readObject();
                int postalCode = (int) in.readObject();
                int instructorId = (int) in.readObject();
                accountBuilder = new AccountBuilder();
                director.buildStudentAccount(accountBuilder);
                accountBuilder.accountId(id)
                        .usernameEmail(email)
                        .password(password)
                        .firstName(firstName)
                        .lastName(lastName)
                        .city(city)
                        .street(street)
                        .postalCode(postalCode)
                        .instructorId(instructorId);
                newAccount = accountBuilder.build();
                boolean check = adminDAO.editAccount(newAccount);
                out.writeObject(operationType);
                out.writeObject(check);

            }
            if (operationType.equals("Edit admin as god admin!")) {
                int id = (int) in.readObject();
                int newId = (int) in.readObject();
                boolean check = adminDAO.godAdminEditAdmin(id, newId);
                out.writeObject(operationType);
                out.writeObject(check);
            }
            if (operationType.equals("Check admin id!")) {
                int id = (int) in.readObject();
                boolean check = adminDAO.checkForAdminID(id);
                out.writeObject(operationType);
                out.writeObject(check);
            }
            if (operationType.equals("Check instructor id!")) {
                int id = (int) in.readObject();
                boolean check = adminDAO.checkForInstructorID(id);
                out.writeObject(operationType);
                out.writeObject(check);
            }
            if (operationType.equals("Check edited username!")) {
                String username = (String) in.readObject();
                boolean check = adminDAO.checkUsername(username);
                out.writeObject(operationType);
                out.writeObject(check);
            }
            if (operationType.equals("Delete admin!")) {
                int IdOfAdminToDelete = (int) in.readObject();
                String choice = (String) in.readObject();
                boolean check = adminDAO.deleteAdmin(acc.getAccountId(), IdOfAdminToDelete, choice);
                out.writeObject(operationType);
                out.writeObject(check);
            }
            if (operationType.equals("Delete instructor!")) {
                int IdOfInstructorToDelete = (int) in.readObject();
                boolean check = adminDAO.deleteInstructor(IdOfInstructorToDelete);
                out.writeObject(operationType);
                out.writeObject(check);
            }
            if (operationType.equals("Delete student!")) {
                int IdOfStudentToDelete = (int) in.readObject();
                boolean check = adminDAO.deleteStudent(IdOfStudentToDelete);
                out.writeObject(operationType);
                out.writeObject(check);
            }
            if (operationType.equals("Delete admin as a god admin!")) {
                int IdOfAdminToDelete = (int) in.readObject();
                String choice = (String) in.readObject();
                boolean check = adminDAO.deleteAdmin(acc.getAccountId(), IdOfAdminToDelete, choice);
                out.writeObject(operationType);
                out.writeObject(check);
            }
            //Instructor
            if (operationType.equals("Finish and add new test!")) {
                Test test = (Test) in.readObject();
                test.setInstructorId(acc.getAccountId());
                boolean check = instructorDAO.addTest(test);
                out.writeObject(operationType);
                out.writeObject(check);
            }
            if (operationType.equals("Delete instructor test!")) {
                int id = (int) in.readObject();
                boolean check = instructorDAO.deleteTest(id);
                out.writeObject(operationType);
                out.writeObject(check);
            }
            if (operationType.equals("Get submitted test for instructor!")) {
                int submittedTestId = (int) in.readObject();
                SubmittedTest sTest = instructorDAO.getSubmittedTest(submittedTestId);
                out.writeObject(operationType);
                out.writeObject(sTest);
            }
            if (operationType.equals("Grade submitted test!")) {
                int score = (int) in.readObject();
                int testId = (int) in.readObject();
                int studentId = (int) in.readObject();
                boolean check = instructorDAO.gradeSubmittedTest(score, testId, studentId);
                out.writeObject(operationType);
                out.writeObject(check);
            }
            //Student
            if (operationType.equals("Student took the test!")) {
                int testId = (int) in.readObject();
                boolean check = studentDAO.addTakenBy(testId, acc.getAccountId());
                out.writeObject(operationType);
                out.writeObject(check);
            }
            if (operationType.equals("Get test for student!")) {
                Integer id = (Integer) in.readObject();
                Test test = studentDAO.getTest(id);
                out.writeObject(operationType);
                out.writeObject(test);
            }
            if (operationType.equals("Student submitted test!")) {
                int testId = (int) in.readObject();
                SubmittedTest sTest = new SubmittedTest(testId, acc.getInstructorId(), acc.getAccountId());
                sTest.setSqList((ArrayList<SubmittedQuestion>) in.readObject());
                sTest.setStList((ArrayList<SubmittedTask>) in.readObject());
                boolean check = studentDAO.submitTest(sTest);
                out.writeObject(operationType);
                out.writeObject(check);
            }
            //All
            if (operationType.equals("Change in database!")) {
                notifyAllUsers(operationType);
            }
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("An error has occurred while processing request!");
            ex.printStackTrace();
        }
    }

    private void notifyAllUsers(String type) {
        if (acc.getAccountType() == 'A') {
            adminRefresh();
            for (ClientHandler user : listOfUsers) {
                if (user.acc.getAccountType() == 'S' && user.acc.getAdminId() == acc.getAccountId()) {
                    otherStudentRefresh(user, type);
                }
                if (user.acc.getAccountType() == 'I' && user.acc.getAdminId() == acc.getAccountId()) {
                    otherInstructorRefresh(user, type);
                }
                if (user.acc.getAccountType() == 'A' && user.acc.getGodId() == acc.getAccountId()) {
                    otherAdminRefresh(user, type);
                }
                if (acc.getAccountId() != 1 && user.acc.getAccountType() == 'A' && user.acc.getAccountId() == 1) {
                    otherAdminRefresh(user, type);
                }
                if (acc.getAccountId() == 1 && user.acc.getAccountType() == 'A' && user.acc.getGodId() != 0 && user.acc.getGodId() != acc.getAccountId()) {
                    otherAdminRefresh(user, type);
                }
            }
        }
        if (acc.getAccountType() == 'I') {
            instructorRefresh();
            for (ClientHandler user : listOfUsers) {
                if (user.acc.getInstructorId() == acc.getAccountId()) {
                    otherStudentRefresh(user, type);
                }
            }
        }
        if (acc.getAccountType() == 'S') {
            studentRefresh();
            for (ClientHandler user : listOfUsers) {
                if (user.acc.getAccountId() == acc.getInstructorId()) {
                    otherInstructorRefresh(user, type);
                }
            }
        }
    }

    private void adminRefresh() {
        try {
            out.writeObject("Change in database!");
            ArrayList<Account> studentComboboxList = adminDAO.getAdminInstructors(acc.getAccountId());
            ArrayList<Account> allAdminList = adminDAO.getAllAdmins();
            ArrayList<Account> adminAdminsList = adminDAO.getAdminAccounts(acc.getAccountId());
            ArrayList<Account> adminInstructorsList = adminDAO.getInstructorAccounts(acc.getAccountId());
            ArrayList<Account> adminStudentsList = adminDAO.getStudentAccounts(acc.getAccountId());
            out.writeObject(studentComboboxList);
            out.writeObject(allAdminList);
            out.writeObject(adminAdminsList);
            out.writeObject(adminInstructorsList);
            out.writeObject(adminStudentsList);
        } catch (IOException ex) {
            System.out.println("An error has occurred while refreshing admin!");
            ex.printStackTrace();
        }
    }

    private void instructorRefresh() {
        try {
            out.writeObject("Change in database!");
            ArrayList<Test> tList = instructorDAO.getInstructorTests(acc.getAccountId());
            ArrayList<Account> sList = instructorDAO.getInstructorStudents(acc.getAccountId());
            ArrayList<SubmittedTest> sTList = instructorDAO.getAllSubmittedTests(acc.getAccountId());
            ArrayList<TakenBy> takenByList = instructorDAO.getAllTakenByTest(acc.getAccountId());
            out.writeObject(tList);
            out.writeObject(sList);
            out.writeObject(sTList);
            out.writeObject(takenByList);
        } catch (IOException ex) {
            System.out.println("An error has occurred while refreshing instructor!");
            ex.printStackTrace();
        }
    }

    private void studentRefresh() {
        try {
            out.writeObject("Change in database!");
            ArrayList<Test> tList = studentDAO.getStudentTests(acc.getInstructorId(), acc.getAccountId());
            ArrayList<SubmittedTest> sTList = studentDAO.getAllSubmittedTests(acc.getAccountId());
            Double avg = studentDAO.getAverageScore(acc.getAccountId());
            out.writeObject(tList);
            out.writeObject(sTList);
            out.writeObject(avg);
        } catch (IOException ex) {
            System.out.println("An error has occurred while refreshing student!");
            ex.printStackTrace();
        }
    }

    private void otherAdminRefresh(ClientHandler user, String type) {
        try {
            user.acc = user.accountDAO.getAccount(user.usernameEmail);
            user.out.writeObject(type);
            ArrayList<Account> studentComboboxList = user.adminDAO.getAdminInstructors(user.acc.getAccountId());
            ArrayList<Account> allAdminList = user.adminDAO.getAllAdmins();
            ArrayList<Account> adminAdminsList = user.adminDAO.getAdminAccounts(user.acc.getAccountId());
            ArrayList<Account> adminInstructorsList = user.adminDAO.getInstructorAccounts(user.acc.getAccountId());
            ArrayList<Account> adminStudentsList = user.adminDAO.getStudentAccounts(user.acc.getAccountId());
            user.out.writeObject(studentComboboxList);
            user.out.writeObject(allAdminList);
            user.out.writeObject(adminAdminsList);
            user.out.writeObject(adminInstructorsList);
            user.out.writeObject(adminStudentsList);
        } catch (IOException ex) {
            System.out.println("An error has occurred while refreshing other admins!");
            ex.printStackTrace();
        }
    }

    private void otherInstructorRefresh(ClientHandler user, String type) {
        try {
            user.acc = user.accountDAO.getAccount(user.usernameEmail);
            user.out.writeObject(type);
            ArrayList<Test> tList = user.instructorDAO.getInstructorTests(user.acc.getAccountId());
            ArrayList<Account> sList = user.instructorDAO.getInstructorStudents(user.acc.getAccountId());
            ArrayList<SubmittedTest> sTList = user.instructorDAO.getAllSubmittedTests(user.acc.getAccountId());
            ArrayList<TakenBy> takenByList = user.instructorDAO.getAllTakenByTest(user.acc.getAccountId());
            user.out.writeObject(tList);
            user.out.writeObject(sList);
            user.out.writeObject(sTList);
            user.out.writeObject(takenByList);
        } catch (IOException ex) {
            System.out.println("An error has occurred while refreshing other instructors!");
            ex.printStackTrace();
        }
    }

    private void otherStudentRefresh(ClientHandler user, String type) {
        try {
            user.acc = user.accountDAO.getAccount(user.usernameEmail);
            user.out.writeObject(type);
            ArrayList<Test> tList = user.studentDAO.getStudentTests(user.acc.getInstructorId(), user.acc.getAccountId());
            ArrayList<SubmittedTest> sTList = user.studentDAO.getAllSubmittedTests(user.acc.getAccountId());
            Double avg = user.studentDAO.getAverageScore(user.acc.getAccountId());
            user.out.writeObject(tList);
            user.out.writeObject(sTList);
            user.out.writeObject(avg);
        } catch (IOException ex) {
            System.out.println("An error has occurred while refreshing other students!");
            ex.printStackTrace();
        }
    }

    private void close(Socket socket, ObjectInputStream in, ObjectOutputStream out) {
        removeUser();
        try {
            if (socket != null) {
                socket.close();
            }
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        } catch (IOException ex) {
            System.out.println("An error has occurred while closing socket and streams!");
            ex.printStackTrace();
        }
    }

    private void removeUser() {
        listOfUsers.remove(this);
    }
}
