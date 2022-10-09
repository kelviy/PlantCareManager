/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package plantcaremanager;

import java.time.LocalDate;

/**
 *
 * @author kelvi
 */
public class Task extends Plant{

    private Boolean doneOrNot;
    private LocalDate dueDate;

    public Task(Boolean doneOrNot, LocalDate dueDate, String name, String note, String label) {
        super(name, note, label);
        this.doneOrNot = doneOrNot;
        this.dueDate = dueDate;
    }
    
    public Task(String name) {
        this(false, null, name, "", "");
    }

    public Boolean getDoneOrNot() {
        return doneOrNot;
    }

    public void setDoneOrNot() {
        doneOrNot = doneOrNot != true;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
    
    public String toString() {
        String temp = super.toString() + ";;" + doneOrNot + ";;";
        if (dueDate == null) {
            temp += "empty";
        } else {
            temp += dueDate;
        }
        return temp;
    }
}
