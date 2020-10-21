package com.ryannitz.covidupdates;

import android.content.Context;
import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ryannitz.covidupdates.utility.FileHandler;
import com.ryannitz.covidupdates.utility.Logger;
import com.ryannitz.covidupdates.CovidStats.Prov;

import java.io.IOException;

public class UserSettings {

    private boolean rawJsonOn;
    private Prov selectedProvince;

    public UserSettings(){
        //default constructor;
    }

    public boolean getRawJsonOn(){
        return rawJsonOn;
    }

    public void setRawJsonOn(boolean rawJsonOn){
        this.rawJsonOn = rawJsonOn;
    }

    public void setSelectedProvince(Prov selectedProvince){
        this.selectedProvince = selectedProvince;
    }

    public Prov getSelectedProvince(){
        return selectedProvince;
    }


    public static UserSettings updateSettings(Context ctx, UserSettings userSettings){
        try{
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(userSettings);
            FileHandler.createJsonFile(ctx, FileHandler.USER_SETTINGS, json);
            return userSettings;
        }catch (JsonProcessingException jpe){
            jpe.printStackTrace();
        }
        return null;
    }

    public static UserSettings loadUserSettings(Context ctx){
        try{
            if(FileHandler.doesFileExist(ctx, FileHandler.USER_SETTINGS)){
                Log.e(Logger.FILE, "Found existing user settingFragment file:");
                String json = FileHandler.getFileContents(ctx, FileHandler.USER_SETTINGS);
                Log.e(Logger.JSON, json);
                ObjectMapper mapper = new ObjectMapper();
                return mapper.readValue(json, UserSettings.class);
            }else{
                //load the default from the assets
                return resetUserSettings(ctx);
            }
        }catch(IOException e){
            e.printStackTrace();
            return null;
        }
    }

    public static UserSettings resetUserSettings(Context ctx){
        Log.e(Logger.FILE, "Creating new user settingFragment file");
        String json = FileHandler.getAssetContents(ctx, FileHandler.USER_SETTINGS);
        FileHandler.createJsonFile(ctx, FileHandler.USER_SETTINGS, json);
        Log.e(Logger.JSON, json);
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, UserSettings.class);
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
}

