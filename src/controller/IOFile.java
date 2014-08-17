/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author j
 */
public class IOFile {
    
    public static Object getData() {
        try {
            FileInputStream fis = new FileInputStream("Data.dat");
            ObjectInputStream ios = new ObjectInputStream(fis);
            return  ios.readObject();
        } catch(IOException | ClassNotFoundException e) {
            System.out.println("Loi " + e.getMessage());
            return null;//new ArrayList<SinhVien>();
        }
    }
    
    public static void setData(Object ob) {
        try {
            File file = new File("Data.dat");
            if(!file.exists())
                file.createNewFile();
            try (FileOutputStream fos = new FileOutputStream(file)) {
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(ob);
            }
        } catch (Exception e) {
            System.out.println("Loi: "+e.getMessage());
        }
    }
}
