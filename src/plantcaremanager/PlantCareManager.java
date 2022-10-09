/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package plantcaremanager;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 *
 * @author kelvi
 */
public class PlantCareManager {

    //initialize global variables of the list of objects 
    private ArrayList<Task> tasks = new ArrayList<>();
    private ArrayList<Plant> plants = new ArrayList<>();
    private ArrayList<Note> notes = new ArrayList<>();

    //intializes static final variables
    public static final String PLANT = "plants";
    public static final String TASK = "tasks";
    public static final String ENCRYPTED_NOTE = "notes";
    public static final String ASCENDING = "ascending";
    public static final String DESCENDING = "descending";
    public static final String ALPH_LABELS = "alphabetically by labels";
    public static final String DUE_DATE = "by due date";

    //pulls data from text file and loads them into classes
    public PlantCareManager() {
        try {
            BufferedReader ffTask = new BufferedReader(new FileReader("Tasks.txt"));
            BufferedReader ffPlant = new BufferedReader(new FileReader("Plants.txt"));
            BufferedReader ffNote = new BufferedReader(new FileReader("EncryptedNotes.txt"));

            String line;

            while ((line = ffPlant.readLine()) != null) {
                String[] parts = line.split(";;");
                String name = parts[0];
                String label = parts[1];
                String note = parts[2];
                if (label.equals("empty")) {
                    label = "";
                }
                if (note.equals("empty")) {
                    note = "";
                }
                plants.add(new Plant(name, note, label));
            }
            while ((line = ffTask.readLine()) != null) {
                String[] parts = line.split(";;");
                String name = parts[0];
                String label = parts[1];
                String note = parts[2];
                boolean doneOrNot = Boolean.parseBoolean(parts[3]);
                String dueDateString = parts[4];
                if (label.equals("empty")) {
                    label = "";
                }
                if (note.equals("empty")) {
                    note = "";
                } 
                LocalDate dueDate;
                if (dueDateString.equals("empty")) {
                    dueDate = null;
                } else {
                    dueDate = LocalDate.parse(dueDateString);
                }
                tasks.add(new Task(doneOrNot, dueDate, name, note, label));
            }
            while ((line = ffNote.readLine()) != null) {
                String[] parts = line.split(";;");
                String noteHeading = parts[0];
                String encryptedPassword = parts[1];
                String encryptedContents = parts[2];
                if (encryptedPassword.equals("empty")) {
                    encryptedPassword = " ";
                }
                if (encryptedContents.equals("empty")) {
                    encryptedContents = " ";
                }
                notes.add(new Note(noteHeading, encryptedContents, encryptedPassword));
            }

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    //Returns a list of tasks that are due within 7 days in the future
    public ArrayList<String> getTasksDueSoon() {
        ArrayList<String> temp = new ArrayList<>();

        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getDueDate().isAfter(LocalDate.now()) || tasks.get(i).getDueDate().isEqual(LocalDate.now())) {
                if (ChronoUnit.DAYS.between(LocalDate.now(), tasks.get(i).getDueDate()) <= 7) {
                    temp.add(tasks.get(i).getName() + " - " + tasks.get(i).getDueDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));
                }
            }
        }
        return temp;
    }

    //Returns a list of tasks that are equal to the filter criteria 
    public ArrayList getFilterList(String filterBy) {
        ArrayList<String> temp = new ArrayList();
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getLabel().equals(filterBy)) {
                temp.add(tasks.get(i).getName());
            }
        }
        return temp;
    }

    //Returns a sorted list; list can sort accending or decending or accending if sorting from labels or from due date.
    public ArrayList<String> getSortList(String sortType, String type) {
        ArrayList<String> nameList = new ArrayList<>();
        ArrayList<LocalDate> dueDateTemp = new ArrayList<>();
        ArrayList<String> labelTemp = new ArrayList<>();

        //adds to arraylist of the task's dueDates list if sorting by due date
        if (sortType.equals(DUE_DATE)) {
            for (int i = 0; i < tasks.size(); i++) {
                dueDateTemp.add(tasks.get(i).getDueDate());
            }
        } else if (sortType.equals(ALPH_LABELS)) { //add to arraylist of the task's lables if sorting by label
            switch (type) {
                case PLANT:
                    for (int i = 0; i < plants.size(); i++) {
                        labelTemp.add(plants.get(i).getLabel());
                    }
                    break;
                case TASK:
                    for (int i = 0; i < tasks.size(); i++) {
                        labelTemp.add(tasks.get(i).getLabel());
                    }
                    break;
            }
        }

        //adds a list of plant/task/note to a temparay list that stores names
        switch (type) {
            case PLANT:
                for (int i = 0; i < plants.size(); i++) {
                    nameList.add(plants.get(i).getName());
                }
                break;
            case TASK:
                for (int i = 0; i < tasks.size(); i++) {
                    nameList.add(tasks.get(i).getName());
                }
                break;
            case ENCRYPTED_NOTE:
                for (int i = 0; i < notes.size(); i++) {
                    nameList.add(notes.get(i).getHeading());
                }
                break;
        }

        //sorts by due date or label before reaching normal sort if sorting by "Due Date"  or "Label"
        switch (sortType) {
            case DUE_DATE:
                for (int i = 0; i < dueDateTemp.size() - 1; i++) {
                    for (int j = i + 1; j < dueDateTemp.size(); j++) {
                        if (dueDateTemp.get(i).compareTo(dueDateTemp.get(j)) > 0) {
                            LocalDate tempDate = dueDateTemp.get(i);
                            dueDateTemp.set(i, dueDateTemp.get(j));
                            dueDateTemp.set(j, tempDate);

                            String tempString = nameList.get(i);
                            nameList.set(i, nameList.get(j));
                            nameList.set(j, tempString);
                        }
                    }
                }
                break;
            case ALPH_LABELS:
                for (int i = 0; i < labelTemp.size() - 1; i++) {
                    for (int j = i + 1; j < labelTemp.size(); j++) {
                        if (labelTemp.get(i).compareToIgnoreCase(labelTemp.get(j)) > 0) {
                            String tempLabel = labelTemp.get(i);
                            labelTemp.set(i, labelTemp.get(j));
                            labelTemp.set(j, tempLabel);

                            String tempString = nameList.get(i);
                            nameList.set(i, nameList.get(j));
                            nameList.set(j, tempString);
                        }
                    }
                }
                break;
        }

        //sorts the list by ascending or descending
        switch (sortType) {
            case ASCENDING:
                Collections.sort(nameList);
                break;
            case DESCENDING:
                Collections.sort(nameList, Collections.reverseOrder());
                break;
        }

        return nameList;
    }

    //returns a index based on the type of object requested and the name of the object
    public int searchIndex(String type, String name) {
        switch (type) {
            case PLANT:
                for (int i = 0; i < plants.size(); i++) {
                    if (plants.get(i).getName().equals(name)) {
                        return i;
                    }
                }
                break;
            case TASK:
                for (int i = 0; i < tasks.size(); i++) {
                    if (tasks.get(i).getName().equals(name)) {
                        return i;
                    }
                }
                break;
            case ENCRYPTED_NOTE:
                for (int i = 0; i < notes.size(); i++) {
                    if (notes.get(i).getHeading().equals(name)) {
                        return i;
                    }
                }
                break;
        }
        return -1;
    }

    //Searchs and returns the name of a plant and returns the Plant object if found
    public Plant displayPlant(String search) {
        for (int i = 0; i < plants.size(); i++) {
            if (plants.get(i).getName().equals(search)) {
                return plants.get(i);
            }
        }
        return null;
    }

    //searches and returns the name of a task and returns the Task object if found
    public Task displayTask(String search) {
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getName().equals(search)) {
                return tasks.get(i);
            }
        }
        return null;
    }

    //Searches and returns the name of a note and returns the Note object if found
    public String getNoteHeading(String search) {

        for (int i = 0; i < notes.size(); i++) {
            if (notes.get(i).getHeading().equals(search)) {
                return notes.get(i).getHeading();
            }
        }
        return null;
    }

    //Searchs and returns the content of the note object if found. Text prompts would be sent if an error has occurred or the note is not decrypted
    public String getNoteContent(String search) {
        int index = searchIndex(ENCRYPTED_NOTE, search);
        if (index != -1 && notes.get(index).getIsDecrypted()) {
            return notes.get(index).getContent();
        }
        return "Please enter your password";
    }

    //Saves the data on a textfile
    public void save() {
        try {
            PrintWriter pwPlant = new PrintWriter(new FileWriter("Plants.txt", false));
            PrintWriter pwTask = new PrintWriter(new FileWriter("Tasks.txt", false));
            PrintWriter pwNote = new PrintWriter(new FileWriter("EncryptedNotes.txt", false));

            for (int i = 0; i < plants.size(); i++) {
                pwPlant.println(plants.get(i).toString());
            }
            for (int i = 0; i < tasks.size(); i++) {
                pwTask.println(tasks.get(i).toString());
            }
            for (int i = 0; i < notes.size(); i++) {
                pwNote.println(notes.get(i).toString());
            }

            pwPlant.close();
            pwTask.close();
            pwNote.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    //adds an object to the array ; A string is returned if a an error has occurred - null is no errors
    public String add(String type, String name) {
        String message = "Duplicate Name, Please enter a new name";
        if (name.isBlank()) {
            return "Please enter a name";
        }
        switch (type) {
            case PLANT:
                if (searchIndex(PLANT, name) != -1) {
                    return message;
                }
                plants.add(new Plant(name));
                return null;
            case TASK:
                if (searchIndex(TASK, name) != -1) {
                    return message;
                }
                tasks.add(new Task(name));
                return null;
            case ENCRYPTED_NOTE:
                if (searchIndex(ENCRYPTED_NOTE, name) != -1) {
                    return message;
                }
                notes.add(new Note(name));
                return null;
        }
        return "Sorry! Something went wrong";
    }

    //deletes an object from the array ;  A string is returned if a an error has occurred - null is no errors
    public String delete(String type, String name) {
        int index;
        switch (type) {
            case PLANT:
                index = searchIndex(PLANT, name);
                if (index != -1) {
                    plants.remove(index);
                    return null;
                }
                break;
            case TASK:
                index = searchIndex(TASK, name);
                if (index != -1) {
                    tasks.remove(index);
                    return null;
                }
                break;
            case ENCRYPTED_NOTE:
                index = searchIndex(ENCRYPTED_NOTE, name);
                if (index != -1) {
                    notes.remove(index);
                    return null;
                }
                break;
        }
        return "Failed";
    }

    //changes an label of an platn or task ;  A string is returned if a an error has occurred - null is no errors
    public String changeAssignedLabel(String type, String objectName, String labelName) {
        int index = -1;
        switch (type) {
            case PLANT:
                index = searchIndex(PLANT, objectName);
                if (index != -1) {
                    plants.get(index).setLabel(labelName);
                    return null;
                }
            case TASK:
                index = searchIndex(TASK, objectName);
                if (index != -1) {
                    tasks.get(index).setLabel(labelName);
                    return null;
                }
        }
        return "please enter a label";
    }

    //Edits an existing object ;  A string is returned if a an error has occurred - null is no errors
    public String edit(String type, String object, String note, String name) {
        int index;
        if (name.isBlank()) {
            return "Please enter a valid name";
        }
        switch (type) {
            case PLANT:
                index = searchIndex(PLANT, object);
                if (index == -1) {
                    return "Plant not found";
                }
                plants.get(index).setName(name);
                plants.get(index).setNote(note);
                break;
            case TASK:
                index = searchIndex(TASK, object);
                if (index == -1) {
                    return "Task not found";
                }
                tasks.get(index).setName(name);
                tasks.get(index).setNote(note);
                break;
            case ENCRYPTED_NOTE:
                index = searchIndex(ENCRYPTED_NOTE, object);
                if (index == -1) {
                    return "Note not found";
                }
                notes.get(index).setHeading(name);
                notes.get(index).setContent(note);
                break;
        }
        return null;
    }

    //Changes the Due Date of a task
    public void changeTaskDate(String task, LocalDate date) {
        int index = searchIndex(TASK, task);
        tasks.get(index).setDueDate(date);
    }

    //Unlocks the note if the password is correct ;  A string is returned if a an error has occurred - null is no errors
    public String unlockNote(String note, String password) {
        int index = searchIndex(ENCRYPTED_NOTE, note);
        if (index == -1) {
            return "note is not found";
        }
        if (password == null) {
            return "Error - no password";
        }
        if (notes.get(index).getPassword().equals(password)) {
            notes.get(index).setIsDecrypted(true);
            return null;
        }
        return "Please enter the correct password";
    }

    //Locks the note if the notes has been decrypted
    public void lockNote(String note) {
        int index = searchIndex(ENCRYPTED_NOTE, note);
        if (index == -1) {
            return;
        }
        notes.get(index).setIsDecrypted(false);
    }

    //Completes a given task
    public void setTaskCompleted(String task) {
        int index = searchIndex(TASK, task);
        if (index == -1) {
            return;
        }
        tasks.get(index).setDoneOrNot();
    }

    //Changes a password if a given note and if the note is decrypted ;  A string is returned if a an error has occurred - null is no errors
    public String changePassword(String note, String password) {
        int index = searchIndex(ENCRYPTED_NOTE, note);
        if (index == -1) {
            return "Note not found";
        }
        if (password.isBlank()) {
            return "Please type a password";
        }
        if (notes.get(index).getIsDecrypted() == false) {
            return "Please unlock the note";
        }
        notes.get(index).setPassword(password);
        return null;
    }

    //Returns a list of all tasks that has a "in pdf" label
    public ArrayList<String> getSyncList() {
        ArrayList<String> temp = new ArrayList<>();

        for (int i = 0; i < tasks.size(); i++) {
            String tempLabel = tasks.get(i).getLabel();
            if (tasks.get(i).getLabel().equals("in pdf")) {
                temp.add(tasks.get(i).getName());
            }
        }
        return temp;
    }

    //Borrowed Code https://www.javatpoint.com/java-create-pdf
    public void toPDF(String type, ArrayList list) {
        try {
            Document document = new Document(PageSize.A4, 50.0f, 50.0f, 10.0f, 50.0f);
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("pdfDownloads\\PlantCareManager.pdf"));
            document.open();
            Font f = new Font(Font.FontFamily.TIMES_ROMAN, 25.0f, Font.BOLD, BaseColor.BLACK);
            Chunk c = new Chunk("Plant Care Manager", f);
            c.setBackground(BaseColor.GREEN);
            Paragraph p1 = new Paragraph(c);
            p1.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(p1);
            document.add(new Paragraph(toPDFFormat(type, list)));
            document.close();
            writer.close();
        } catch (FileNotFoundException | DocumentException e) {
            System.out.println(e.getMessage());
        }
    }

    //Returns a string containing details of tasks or plants that would be added to the pdf
    public String toPDFFormat(String type, ArrayList list) {
        String temp = "";

        switch (type) {
            case PLANT:
                temp += "\n\nPlant List:\n\n";
                for (int i = 0; i < list.size(); i++) {
                    for (int j = 0; j < plants.size(); j++) {
                        if (list.get(i).equals(plants.get(j).getName())) {
                            temp += "Plant Name: " + plants.get(j).getName() + "\n";
                            temp += "Label: " + plants.get(j).getLabel() + "\n";
                            temp += "Notes: " + plants.get(j).getNote() + "\n\n";
                        }
                    }
                }
                break;
            case TASK:
                temp += "\n\nTask List:\n\n";
                for (int i = 0; i < list.size(); i++) {
                    for (int j = 0; j < tasks.size(); j++) {
                        if (list.get(i).equals(tasks.get(j).getName())) {
                            temp += "Plant Name: " + tasks.get(j).getName() + "\n";
                            temp += "Label: " + tasks.get(j).getLabel() + "\n";
                            temp += "Task Done: " + tasks.get(j).getDoneOrNot() + "\n";
                            temp += "Due Date: " + tasks.get(j).getDueDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)) + "\n";
                            temp += "Notes: " + tasks.get(j).getNote() + "\n\n";
                        }
                    }
                    break;
                }

        }
        return temp;
    }

    //Tasks would be completed and have there's label changed to "completed"
    public void syncTasks(ArrayList list) {
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < tasks.size(); j++) {
                if (list.get(i).equals(tasks.get(j).getName())) {
                    tasks.get(j).setLabel("completed");
                    if (tasks.get(j).getDoneOrNot() == false) {
                        tasks.get(j).setDoneOrNot();
                    }
                }
            }
        }
    }
}
