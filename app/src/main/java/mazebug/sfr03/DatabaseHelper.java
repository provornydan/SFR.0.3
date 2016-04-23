package mazebug.sfr03;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Provorny on 2/13/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME="SFR03_3.db";
    public static final String TABLE_NAME="mysfrs";
    public static final String COL_1 ="ID";
    //public static final String COL_2="Latitude";
    //public static final String COL_3="Longitude";
    public static final String COL_2="site_name";
    //public static final String COL_5="Supplier";
    public static final String COL_4="Owners";
    public static final String COL_5="CID";
    public static final String COL_6="CSR";
    public static final String COL_7="Site";
    public static final String COL_8="POW_VF";
    public static final String COL_9="pow_02";
    public static final String COL_3="search_area";
    /*public static final String COL_13="Development_Plan_Details";
    public static final String COL_14="Policies_Landuse_Conservation_Designations";
    public static final String COL_15="Notes";
    public static final String COL_16="MS6_Forecast_Date";
    public static final String COL_17="Forecast_Rent";
    public static final String COL_18="Final_Assessment_Rank_Proceed_Option";
    public static final String COL_19="Date";
    public static final String COL_20="Acquisition_Surveyor"; */
    public static final String COL_10="addedDate";

    public static final String OPTION_NAME = "Options";
    public static final String IMAGE_NAME= "Images";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        SQLiteDatabase database = this.getWritableDatabase();
        //db.execSQL("create table if not exists " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, Latitude varchar(255) DEFAULT NULL, Longitude varchar(255) DEFAULT NULL, Site_name varchar(255), Supplier varchar(255), Owners varchar(255), CID varchar(255) DEFAULT NULL, CSR varchar(255) DEFAULT NULL, Site varchar(255) DEFAULT NULL, POW_VF varchar(255) DEFAULT NULL, POW_O2 varchar(255) DEFAULT NULL, Searc_Area_Description text, Development_Plan_Details text, Policies_Landuse_Conservation_Designations text,Notes text,  MS6_Forecast_Date date DEFAULT NULL,  Forecast_Rent float DEFAULT NULL,  Final_Assessment_Rank_Proceed_Option int(11) DEFAULT NULL, Date date DEFAULT NULL, Acquisition_Surveyor varchar(255) DEFAULT NULL)");
        db.execSQL("create table if not exists " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, site_name varchar(255), search_area varchar(255), Owners varchar(255), CID varchar(255), CSR varchar(255), Site varchar(255), pow_vf varchar(255), pow_02 varchar(255), addedDate varchar(255), Created INTEGER, Edited INTEGER)");
        db.execSQL("create table if not exists " + OPTION_NAME + " (OPTION_ID INTEGER PRIMARY KEY AUTOINCREMENT, SITE_ID INTEGER, option_name varchar(255), Town varchar(255), County varchar(255), Postcode varchar(255), Antenna_height varchar(255), Latitude varchar(255) DEFAULT NULL, Longitude varchar(255) DEFAULT NULL, Created INTEGER, Edited INTEGER)");
        db.execSQL("create table if not exists " + IMAGE_NAME + " (IMAGE_CODE INTEGER PRIMARY KEY AUTOINCREMENT, OPTION_ID INTEGER, FILE_PATH varchar(255), Created INTEGER)");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS" + OPTION_NAME);
        onCreate(db);
    }
    public boolean insertData(ArrayList<String> arr){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, arr.get(0));
        contentValues.put(COL_3, arr.get(1));
        contentValues.put(COL_4, arr.get(2));
        contentValues.put(COL_5, arr.get(3));
        contentValues.put(COL_6, arr.get(4));
        contentValues.put(COL_7, arr.get(5));
        contentValues.put(COL_8, arr.get(6));
        contentValues.put(COL_9, arr.get(7));
        /*contentValues.put(COL_10, arr.get(8));
        contentValues.put(COL_11, arr.get(9));
        contentValues.put(COL_12, arr.get(10));
        contentValues.put(COL_13, arr.get(11));
        contentValues.put(COL_14, arr.get(12));
        contentValues.put(COL_15, arr.get(13));
        contentValues.put(COL_16, arr.get(14));
        contentValues.put(COL_17, arr.get(15));
        contentValues.put(COL_18, arr.get(16));
        contentValues.put(COL_19, arr.get(17));
        contentValues.put(COL_20, arr.get(18));*/

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = sdf.format(new Date());


        contentValues.put(COL_10, strDate);



        long result = db.insert(TABLE_NAME, null ,contentValues);
        if(result==-1){
            return false;}
        else
            return true;
    }
        public boolean insertAnOption(String Site_ID, String optionName){
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues content2 = new ContentValues();
            content2.put("SITE_ID", Site_ID);
            content2.put("option_name", optionName);


            long result2 = db.insert(OPTION_NAME, null, content2);
            if(result2==-1){
                return false;}
            else
                return true;
        }

        public boolean insertAnImage(String OptionID, String FilePath){
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues content3 = new ContentValues();
            content3.put("OPTION_ID", OptionID);
            content3.put("FILE_PATH", FilePath);

            long result2 = db.insert(IMAGE_NAME, null, content3);
            if(result2==-1){
                return false;}
            else
                return true;
        }

    public Cursor getSiteData(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res=db.rawQuery("Select ID, Site_name, Supplier, Owners, CID, CSR from "+TABLE_NAME+" where Site_name='"+name+"'", null);
        return res;
    }

    public Cursor getIdData(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res=db.rawQuery("Select ID, Site_name, search_area, Owners, CID, CSR, Site, pow_vf, pow_02 from "+TABLE_NAME+" where ID='"+id+"'", null);
        return res;
    }

    public Cursor getImageData(String option_id){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res=db.rawQuery("Select IMAGE_CODE, FILE_PATH from "+IMAGE_NAME+" where OPTION_ID='"+option_id+"' ORDER BY IMAGE_CODE DESC", null);
        return res;
    }

    public Cursor getAllImages(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res=db.rawQuery("Select IMAGE_CODE, FILE_PATH, OPTION_ID from "+IMAGE_NAME+" ORDER BY IMAGE_CODE ASC", null);
        return res;
    }






    public Cursor getThisID(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res=db.rawQuery("Select ID from "+TABLE_NAME+" ORDER BY ID DESC LIMIT 1", null);
        return res;

    }





    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res=db.rawQuery("Select ID, site_name, addedDate from "+TABLE_NAME+" ORDER BY ID DESC", null);
        return res;
    }

    public boolean updateDatabase(String id, String site_name, String search_area, String Owners, String cid, String csr, String site, String pow_vf, String pow_o2){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, site_name);
        contentValues.put(COL_3, search_area);
        contentValues.put(COL_4, Owners);
        contentValues.put(COL_5, cid);
        contentValues.put(COL_6, csr);
        contentValues.put(COL_7, site);
        contentValues.put(COL_8, pow_vf);
        contentValues.put(COL_9, pow_o2);

        db.update(TABLE_NAME, contentValues, "ID = ?", new String[] {id});

        return true;
    }

    public boolean updateCoord(String id, String latitude, String longitude) {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Latitude", latitude);
        contentValues.put("Longitude", longitude);
        db.update(OPTION_NAME, contentValues, "OPTION_ID = ?", new String[]{id});
        return true;
    }

    public boolean updateOption(String id, String optionName, String townName, String countyName, String postCode, String antennaHeight ){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("option_name", optionName);
        contentValues.put("Town", townName);
        contentValues.put("County", countyName);
        contentValues.put("Postcode", postCode);
        contentValues.put("Antenna_height", antennaHeight);

        db.update(OPTION_NAME, contentValues, "OPTION_ID = ?", new String[]{id});
        return true;
    }

    public boolean updateImage(String imageCode, String FilePath){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("FILE_PATH", FilePath);

        db.update(IMAGE_NAME, contentValues, "IMAGE_CODE = ?", new String[]{imageCode});
        return true;
    }


    public Cursor getAllOptions(String siteID){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res=db.rawQuery("Select OPTION_ID, option_name, Town, County, Postcode, Antenna_height, Latitude, Longitude  from "+OPTION_NAME+" where SITE_ID='"+siteID+"'", null);
        return res;
    }

    public Cursor getOptionByName(String OptionID){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res=db.rawQuery("Select OPTION_ID, option_name, Town, County, Postcode, Antenna_height, Latitude, Longitude  from "+OPTION_NAME+" where OPTION_ID='"+OptionID+"'", null);
        return res;
    }


}


