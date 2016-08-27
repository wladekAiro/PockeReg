package com.example.wladek.pockeregapp.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.wladek.pockeregapp.pojo.School;
import com.example.wladek.pockeregapp.pojo.Student;

import java.util.ArrayList;

/**
 * Created by wladek on 8/15/16.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "POCKEREGAPP.db";
    public static final String TABLE_STUDENTS = "tbl_student";
    public static final String TABLE_SCHOOL = "tbl_school";
    public static final String TABLE_AGENT = "tbl_agent";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_STUDENTS + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, FIRST_NAME TEXT, " +
                " SECOND_NAME TEXT, SUR_NAME TEXT, STUDENT_NUMBER TEXT, PARENT_FULL_NAME TEXT, PARENT_ID_NUMBER TEXT, STUDENT_PHOTO_URL TEXT," +
                " STUDENT_SCHOOL_CODE TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_SCHOOL + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, SCHOOL_NAME TEXT, " +
                "SCHOOL_CODE TEXT, STATUS TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_AGENT + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, AGENT_NAME TEXT, " +
                "AGENT_NUMBER TEXT , STATUS TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCHOOL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AGENT);
        onCreate(db);
    }

    public ArrayList<Student> getSchoolStudents(String schoolCode) {
        ArrayList<Student> students = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_STUDENTS + " WHERE STUDENT_SCHOOL_CODE =?",
                new String[]{schoolCode});

        if (res.getCount() > 0) {
            while (res.moveToNext()) {

                Student student = new Student();

                student.setId(new Long(res.getInt(0)));
                student.setFirstName(res.getString(1));
                student.setSurName(res.getString(2));
                student.setSurName(res.getString(3));
                student.setStudentNo(res.getString(4));
                student.setParentFullName(res.getString(5));
                student.setParentIdNumber(res.getString(6));
                student.setPhotoPath(res.getString(7));
                student.setSchoolCode(res.getString(8));

                students.add(student);
            }
        }

        db.close();

        return students;
    }

    public String createStudent(Student student) {
        String response = null;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_STUDENTS + " WHERE STUDENT_PHOTO_URL =?",
                new String[]{student.getPhotoPath()});

        if (cursor.getCount() == 0) {
            //Create new record
            ContentValues contentValues = new ContentValues();
            contentValues.put("FIRST_NAME", student.getFirstName());
            contentValues.put("SECOND_NAME", student.getSecondName());
            contentValues.put("SUR_NAME", student.getSurName());
            contentValues.put("STUDENT_NUMBER", student.getStudentNo());
            contentValues.put("PARENT_FULL_NAME", student.getParentFullName());
            contentValues.put("PARENT_ID_NUMBER", student.getParentIdNumber());
            contentValues.put("STUDENT_PHOTO_URL", student.getPhotoPath());
            contentValues.put("STUDENT_SCHOOL_CODE", student.getSchoolCode());

            Long result = db.insert(TABLE_STUDENTS, null, contentValues);

            if (result == -1) {
                response = "Error while creating student";
            }else {
                response = "Student created";
            }

        } else {
            //Update record
            ContentValues contentValues = new ContentValues();
            contentValues.put("FIRST_NAME", student.getFirstName());
            contentValues.put("SECOND_NAME", student.getSecondName());
            contentValues.put("SUR_NAME", student.getSurName());
            contentValues.put("STUDENT_NUMBER", student.getStudentNo());
            contentValues.put("PARENT_FULL_NAME", student.getParentFullName());
            contentValues.put("PARENT_ID_NUMBER", student.getParentIdNumber());
            contentValues.put("STUDENT_PHOTO_URL", student.getPhotoPath());
            contentValues.put("STUDENT_SCHOOL_CODE", student.getSchoolCode());

            int result = db.update(TABLE_STUDENTS, contentValues, "STUDENT_PHOTO_URL='" + student.getPhotoPath() + "'", null);

            if (result > 0){
                response = "Student Updated";
            }else {
                response = "Error while updating student";
            }
        }

        db.close();

        return response;
    }

    public String setSchool(School school) {
        SQLiteDatabase db = this.getWritableDatabase();

        String response = " Failed ";

        if (school.getId() == null) {
            //Insert
            ContentValues contentValues = new ContentValues();
            contentValues.put("SCHOOL_NAME", school.getSchoolName());
            contentValues.put("SCHOOL_CODE", school.getSchoolCode());
            contentValues.put("STATUS", school.getStatus().toString());

            Long result = db.insert(TABLE_SCHOOL, null, contentValues);

            if (result == -1) {

                return "School not saved";

            }
        } else {
            //Update
            ContentValues contentValues = new ContentValues();
            contentValues.put("SCHOOL_NAME", school.getSchoolName());
            contentValues.put("SCHOOL_CODE", school.getSchoolCode());
            contentValues.put("STATUS", school.getStatus().toString());

            int result = db.update(TABLE_SCHOOL, contentValues, "ID='" + school.getId() +"'", null);

            if (result == -1) {

                return "School not updated";

            }

        }

        return response;
    }
//
//    private String attachExpenseToClaim(ArrayList<ExpenseItem> expenseItems, Long id) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        String response = null;
//
//        ContentValues contentValues = new ContentValues();
//        contentValues.put("CLAIM_ID", 0);
//
//        int result = 0;
//
//        db.update(TABLE_EXPENSES, contentValues, "CLAIM_ID='" + id + "'", null);
//
//        for (ExpenseItem i : expenseItems) {
//            contentValues = new ContentValues();
//            contentValues.put("CLAIM_ID", id);
//
//            result = db.update(TABLE_EXPENSES, contentValues, "EXPENSE_PHOTO_URL='" + i.getImagePath() + "'", null);
//
//
//            if (result > 0) {
//                response = "Success";
//            } else {
//                return "Failed to set claim on expense";
//            }
//        }
//
//        return response;
//    }
//
//    public ArrayList<ExpenseClaim> getClaims() {
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        ArrayList<ExpenseClaim> claims = new ArrayList<>();
//
//        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_EXPENSE_CLAIMS + " ORDER BY ID DESC", null);
//
//        if (res.getCount() > 0) {
//            while (res.moveToNext()) {
//
//                ExpenseClaim expenseClaim = new ExpenseClaim();
//                expenseClaim.setId(new Long(res.getInt(0)));
//                expenseClaim.setTitle(res.getString(1));
//                expenseClaim.setDescription(res.getString(2));
//                expenseClaim.setTotalAmount(res.getDouble(3));
//
//                Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_EXPENSES + " WHERE CLAIM_ID =?",
//                        new String[]{expenseClaim.getId()+""});
//
//                if (cursor.getCount() > 0) {
//                    while (cursor.moveToNext()) {
//
//                        ExpenseItem expenseItem = new ExpenseItem();
//
//                        expenseItem.setId(new Long(cursor.getInt(0)));
//                        expenseItem.setExpenseName(cursor.getString(1));
//                        expenseItem.setExpenseDate(cursor.getString(2));
//                        expenseItem.setImagePath(cursor.getString(3));
//                        expenseItem.setExpenseAmount(cursor.getDouble(4));
//
//                        expenseClaim.getExpenses().add(expenseItem);
//                    }
//                }
//
//                claims.add(expenseClaim);
//            }
//        }
//
//        db.close();
//
//        return claims;
//    }
}
