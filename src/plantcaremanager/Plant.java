/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package plantcaremanager;

/**
 *
 * @author kelvi
 */
public class Plant {
    private String name;
    private String note;
    private String label;

    public Plant(String name, String note, String label) {
        this.name = name;
        this.note = note;
        this.label = label;
    }
    
    public Plant(String name) {
        this(name, "empty", "empty");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
    
    @Override
    public String toString() {
        String labels = label;
        String notes = note;
        if (label.isBlank()) {
            labels = "empty";
        } if (note.isBlank()) {
            notes = "empty";
        }
        return name + ";;" + labels + ";;" + notes;
    }
    
}
