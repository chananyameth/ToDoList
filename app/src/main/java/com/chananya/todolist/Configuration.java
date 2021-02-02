package com.chananya.todolist;


import android.content.Context;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class Configuration {

    private static Configuration instance = null;
    public static Gson gson = null;
    private static String config_file_path;
    public Configuration.Data data = null;

    public static class Data {
        public ArrayList<ToDoList> lists = null;
        public boolean show_tutorial = true;
        public int lines_count = Consts.DEFAULT_LINES_COUNT;
        public int character_limit = Consts.DEFAULT_CHARACTER_LIMIT;
        public int version = Consts.VERSION;
    }


    public static Configuration getInstance(Context context) { // context for the app's directory
        if (gson == null) {
            gson = new Gson();
        }
        if (instance == null) {
            instance = new Configuration(context); // must be last in the function
        }

        return Configuration.instance;
    }

    private Configuration(Context context) {
        config_file_path = context.getFilesDir().getPath() + "/" + Consts.ConfigFileName;
        boolean result = this.loadConfig();

        if (!result) {
            data = new Data();
            if (!this.createConfig()) {
                System.exit(0); //Catastrophic
            }
        }
    }

    private boolean loadConfig() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(config_file_path));
            String line;
            StringBuilder content = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                content.append(line);
            }

            // If there is no content, the file must not be valid
            if (content.length() <= 0) {
                return false;
            }

            data = gson.fromJson(content.toString(), Data.class);

            reader.close();
            return true;
        } catch (IOException ex) {
            return false;
        }
    }

    private boolean createConfig() {
        data.lists = new ArrayList<>();
        data.show_tutorial = true;
        data.lines_count = Consts.DEFAULT_LINES_COUNT;
        data.character_limit = Consts.DEFAULT_CHARACTER_LIMIT;

        return saveConfig();
    }

    public boolean saveConfig() {
        try {
            PrintWriter configFile = new PrintWriter(config_file_path, "UTF-8");

            configFile.write(gson.toJson(data));

            configFile.close();

            return true;
        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            return false;
        }
    }
}