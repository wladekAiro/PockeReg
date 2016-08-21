package com.example.wladek.pockeregapp.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
                "SCHOOL_CODE TEXT , ACTIVE INTEGER)");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_AGENT + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, AGENT_NAME TEXT, " +
                "AGENT_NUMBER TEXT , ACTIVE INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCHOOL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AGENT);
        onCreate(db);
    }

//    public ArrayList<ExpenseItem> getExpenseItems() {
//        ArrayList<ExpenseItem> expenseItems = new ArrayList<>();
//
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_EXPENSES + " ORDER BY ID DESC", null);
//
//        if (res.getCount() > 0) {
//            while (res.moveToNext()) {
//
//                ExpenseItem expenseItem = new ExpenseItem();
//
//                expenseItem.setId(new Long(res.getInt(0)));
//                expenseItem.setExpenseName(res.getString(1));
//                expenseItem.setExpenseDate(res.getString(2));
//                expenseItem.setImagePath(res.getString(3));
//                expenseItem.setExpenseAmount(res.getDouble(4));
//
//                expenseItems.add(expenseItem);
//            }
//        }
//
//        db.close();
//
//        return expenseItems;
//    }

//    public Student createStudent(Student student) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        String pic_url = expenseItem.getImagePath();
//
//        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_EXPENSES + " WHERE EXPENSE_PHOTO_URL =?",
//                new String[]{pic_url});
//
//        if (cursor.getCount() == 0) {
//            //Create new record
//            ContentValues contentValues = new ContentValues();
//            contentValues.put("EXPENSE_NAME", expenseItem.getExpenseName());
//            contentValues.put("EXPENSE_DATE", expenseItem.getExpenseDate());
//            contentValues.put("EXPENSE_PHOTO_URL", expenseItem.getImagePath());
//            contentValues.put("EXPENSE_AMOUNT", expenseItem.getExpenseAmount());
//
//            Long result = db.insert(TABLE_EXPENSES, null, contentValues);
//
//            if (result == -1) {
//
//                return null;
//
//            }
//        } else {
//            //Update record
//            ContentValues contentValues = new ContentValues();
//            contentValues.put("EXPENSE_NAME", expenseItem.getExpenseName());
//            contentValues.put("EXPENSE_DATE", expenseItem.getExpenseDate());
//            contentValues.put("EXPENSE_PHOTO_URL", expenseItem.getImagePath());
//            contentValues.put("EXPENSE_AMOUNT", expenseItem.getExpenseAmount());
//
//            db.update(TABLE_EXPENSES, contentValues, "EXPENSE_PHOTO_URL='" + pic_url + "'", null);
//
//        }
//
//        db.close();
//
//        return expenseItem;
//    }
//
//    public String createClaim(ExpenseClaim expenseClaim) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        String response = " Failed ";
//
//        if (expenseClaim.getId() == null) {
//            //Insert
//            ContentValues contentValues = new ContentValues();
//            contentValues.put("CLAIM_TITLE", expenseClaim.getTitle());
//            contentValues.put("CLAIM_DESCRIPTION", expenseClaim.getDescription());
//
//            Long result = db.insert(TABLE_EXPENSE_CLAIMS, null, contentValues);
//
//            if (result == -1) {
//
//                return "Expense claim not saved";
//
//            }
//        } else {
//            //Update
//            ContentValues contentValues = new ContentValues();
//            contentValues.put("CLAIM_TITLE", expenseClaim.getTitle());
//            contentValues.put("CLAIM_DESCRIPTION", expenseClaim.getDescription());
//
//            int result = db.update(TABLE_EXPENSE_CLAIMS, contentValues, "ID='" + expenseClaim.getId(), null);
//
//            if (result == -1) {
//
//                return "Expense claim not updated";
//
//            }
//
//        }
//
//        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_EXPENSE_CLAIMS + " ORDER BY ID DESC LIMIT 1",
//                null);
//
//        if (res.getCount() > 0) {
//            while (res.moveToNext()) {
//                expenseClaim.setId(new Long(res.getInt(0)));
//                expenseClaim.setTitle(res.getString(1));
//                expenseClaim.setDescription(res.getString(2));
//                expenseClaim.setTotalAmount(res.getDouble(3));
//            }
//        }
//
//        db.close();
//
//        if (!expenseClaim.getExpenses().isEmpty() && expenseClaim.getExpenses().size() > 0) {
//
//            response = attachExpenseToClaim(expenseClaim.getExpenses(), expenseClaim.getId());
//
//        }
//
//        return response;
//    }
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
