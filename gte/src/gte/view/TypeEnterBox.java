package gte.view;

import com.sun.org.apache.xpath.internal.SourceTree;
import gte.controller.Controller;
import gte.model.Component;
import gte.model.Model;

import javax.swing.*;
//import java.awt.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Created by kns10 on 4/21/16.
 */
public class TypeEnterBox extends JPanel {

    private View view;
    private Model model;
    private JTextField field;
    private InputKeyboardListener keyboardListener;
    private TypeEnterBox() {

    }

    public String convertToJSON() {
        ArrayList<Component> cSelected = model.getSelected();
        String[] d = this.getData().split(" ");
        String out = "{";
        for (int i=0; i<Math.min(d.length, cSelected.size()); i++) {
            out += "\""+cSelected.get(i)+"\":\""+d[i]+"\",";
        }
        return out + "}";
    }

    public void convertDataToJSON(String label, Component data) {
        JSONObject obj = new JSONObject();
        obj.put("label", label);
        obj.put("data", data.toString());
        try {

            FileWriter file = new FileWriter("test.json",true);
            file.append(obj.toJSONString());

            file.flush();
            file.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void writeJSONToFile() throws IOException{
        File yourFile = new File("data.txt");
        if(!yourFile.exists()) {
            yourFile.createNewFile();
        }
        PrintWriter oFile = new PrintWriter(yourFile, "UTF-8");
        oFile.print(convertToJSON());
        oFile.close();
    }

    public void writeJSONToFile2() throws IOException{
        JSONObject obj = new JSONObject();
        JSONArray list = new JSONArray();
        ArrayList<Component> cSelected = model.getSelected();
        String[] d = this.getData().split(" ");
        for (int i=0; i<Math.min(d.length, cSelected.size()); i++) {
            list.add(cSelected.get(i));
        }
        FileWriter file = new FileWriter("data.txt");
        file.write(obj.toJSONString());
        file.flush();
        file.close();
    }

    public ArrayList<Component> readJSONFromFile(File f) throws IOException {
        JSONParser parser = new JSONParser();

        try {

            Object obj = parser.parse(new FileReader(f));

            JSONObject jsonObject = (JSONObject) obj;

            String name = (String) jsonObject.get("name");
            System.out.println(name);

            long age = (Long) jsonObject.get("age");
            System.out.println(age);

            // loop array
            JSONArray msg = (JSONArray) jsonObject.get("messages");
            Iterator<String> iterator = msg.iterator();
            while (iterator.hasNext()) {
                System.out.println(iterator.next());
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getData() {
        return field.getText();
    }

    public TypeEnterBox(Model model, View view, Controller controller)
    {
        this.view = view;
        this.model = model;
        field = new JTextField(30);
        this.add(field, BorderLayout.SOUTH);
        keyboardListener = new InputKeyboardListener(model, view, controller);
        addKeyListener(keyboardListener);
        System.out.println("plserino");

    }

}
