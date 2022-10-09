/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package plantcaremanager;

/**
 *
 * @author kelvi
 */

import com.formdev.flatlaf.FlatIntelliJLaf;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.UnsupportedLookAndFeelException;
import plantcaremanager.UI.*;

public class UIManager {

    
    public UIManager() {
        //Borrowed Code from FlatLaf 1.2 and StackOverFlow https://stackoverflow.com/questions/70898611/how-to-set-flatlaf-light-look-and-feel-in-netbeans-ide-12-6
        try {
            javax.swing.UIManager.setLookAndFeel(new FlatIntelliJLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.getMessage();
        }
        new HomePage().setVisible(true);
    }
    
    //Turns an ArrayList<String> to an DefaultListModel
    public static DefaultListModel toListModel(ArrayList<String> array) {
        DefaultListModel listModel = new DefaultListModel();

        for (int i = 0; i < array.size(); i++) {
            listModel.addElement(array.get(i));
        }
        return listModel;
    }

    public static ArrayList toArrayList(ListModel array) {
        ArrayList arrayList = new ArrayList();

        for (int i = 0; i < array.getSize(); i++) {
            arrayList.add(array.getElementAt(i));
        }
        return arrayList;
    }
    
    public static void switchModels(JList fromList, JList toList) {
        int index = fromList.getSelectedIndex();
        if(index == -1) { 
            return;
        }
        DefaultListModel modelAssigned = (DefaultListModel) (fromList.getModel());
        Object temp = removeListItem(modelAssigned, index);
        fromList.setSelectedIndex(0);
        DefaultListModel modelUnassigned = (DefaultListModel) (toList.getModel());
        addListItem(modelUnassigned, temp);
        toList.setSelectedIndex(0);
    }
    
     public static Object removeListItem(DefaultListModel model, int index) {
        Object temp = model.get(index);
        model.remove(index);
        return temp;
    }

    public static void addListItem(DefaultListModel model, Object element) {
        String temp = String.valueOf(element);
        model.addElement(temp);
    }
    
}
