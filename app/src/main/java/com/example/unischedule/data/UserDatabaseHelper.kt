package com.example.unischedule.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class UserDatabaseHelper(context: Context) : SQLiteOpenHelper(
    context,
    DATABASE_NAME,
    null,
    DATABASE_VERSION
) {

    companion object {
        private const val DATABASE_NAME = "users.db"
        private const val DATABASE_VERSION = 6

        // ---------- Таблица пользователей ----------
        private const val TABLE_USERS = "users"
        private const val COLUMN_ID = "id"
        private const val COLUMN_USERNAME = "username"
        private const val COLUMN_PASSWORD = "password"

        // ---------- Таблица пар ----------
        private const val TABLE_LESSONS = "lessons"
        private const val COL_LESSON_ID = "id"
        private const val COL_NAME = "name"
        private const val COL_START = "start_time"
        private const val COL_END = "end_time"
        private const val COL_TEACHER = "teacher"
        private const val COL_CLASSROOM = "classroom"
        private const val COL_WEEKDAY = "weekday"
        private const val COL_IS_EVEN = "is_even_week"
        private const val COL_USER_ID = "user_id"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createUsers = """
            CREATE TABLE $TABLE_USERS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_USERNAME TEXT NOT NULL UNIQUE,
                $COLUMN_PASSWORD TEXT NOT NULL
            )
        """.trimIndent()

        val createLessons = """
            CREATE TABLE $TABLE_LESSONS (
                $COL_LESSON_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_NAME TEXT NOT NULL,
                $COL_START TEXT NOT NULL,
                $COL_END TEXT NOT NULL,
                $COL_TEACHER TEXT,
                $COL_CLASSROOM TEXT,
                $COL_WEEKDAY INTEGER NOT NULL,
                $COL_IS_EVEN INTEGER NOT NULL,
                $COL_USER_ID TEXT NOT NULL
            )
        """.trimIndent()

        db.execSQL(createUsers)
        db.execSQL(createLessons)
        insertSampleLessons(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 3) {
            db.execSQL("ALTER TABLE $TABLE_LESSONS ADD COLUMN $COL_CLASSROOM TEXT")
        }
    }

    // ----------------- Методы для пользователей -----------------

    fun addUser(username: String, password: String): Boolean {
        val db = writableDatabase
        val cursor = db.query(
            TABLE_USERS,
            arrayOf(COLUMN_USERNAME),
            "$COLUMN_USERNAME = ?",
            arrayOf(username),
            null, null, null
        )
        val exists = cursor.moveToFirst()
        cursor.close()

        if (exists) {
            db.close()
            return false
        }

        val values = ContentValues().apply {
            put(COLUMN_USERNAME, username)
            put(COLUMN_PASSWORD, password)
        }
        val result = db.insert(TABLE_USERS, null, values)
        db.close()
        return result != -1L
    }

    fun getUser(username: String): Pair<String, String>? {
        val db = readableDatabase
        val cursor: Cursor = db.query(
            TABLE_USERS,
            arrayOf(COLUMN_USERNAME, COLUMN_PASSWORD),
            "$COLUMN_USERNAME = ?",
            arrayOf(username),
            null, null, null
        )

        var user: Pair<String, String>? = null
        if (cursor.moveToFirst()) {
            val uname = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME))
            val pass = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD))
            user = Pair(uname, pass)
        }
        cursor.close()
        db.close()
        return user
    }

    fun getUserId(username: String): Int? {
        val db = readableDatabase
        val cursor = db.query(
            "users",
            arrayOf("id"),
            "username = ?",
            arrayOf(username),
            null, null, null
        )
        var id: Int? = null
        if (cursor.moveToFirst()) {
            id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
        }
        cursor.close()
        db.close()
        return id
    }


    // ----------------- Методы для пар -----------------

    fun addLesson(lesson: Lesson): Long {
        val db = writableDatabase
        val cv = ContentValues().apply {
            put(COL_NAME, lesson.name)
            put(COL_START, lesson.startTime)
            put(COL_END, lesson.endTime)
            put(COL_TEACHER, lesson.teacher)
            put(COL_CLASSROOM, lesson.classroom)
            put(COL_WEEKDAY, lesson.weekday)
            put(COL_IS_EVEN, if (lesson.isEvenWeek) 1 else 0)
            put(COL_USER_ID, lesson.userId)
        }
        val id = db.insert(TABLE_LESSONS, null, cv)
        db.close()
        return id
    }

    fun getLessonsForDay(weekday: Int, isEvenWeek: Boolean, userId: String): List<Lesson> {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_LESSONS,
            null,
            "$COL_WEEKDAY = ? AND $COL_IS_EVEN = ? AND $COL_USER_ID = ?",
            arrayOf(weekday.toString(), if (isEvenWeek) "1" else "0", userId),
            null, null,
            "$COL_START ASC"
        )

        val lessons = mutableListOf<Lesson>()
        if (cursor.moveToFirst()) {
            do {
                val lesson = Lesson(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_LESSON_ID)),
                    name = cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME)),
                    startTime = cursor.getString(cursor.getColumnIndexOrThrow(COL_START)),
                    endTime = cursor.getString(cursor.getColumnIndexOrThrow(COL_END)),
                    teacher = cursor.getString(cursor.getColumnIndexOrThrow(COL_TEACHER)),
                    classroom = cursor.getString(cursor.getColumnIndexOrThrow(COL_CLASSROOM)),
                    weekday = cursor.getInt(cursor.getColumnIndexOrThrow(COL_WEEKDAY)),
                    isEvenWeek = cursor.getInt(cursor.getColumnIndexOrThrow(COL_IS_EVEN)) == 1,
                    userId = cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_ID))
                )
                lessons.add(lesson)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return lessons
    }

    private fun insertSampleLessons(db: SQLiteDatabase) {
        val demoUserId = "1"
        val lessons = listOf(
            Lesson(0, "Нейросетевые и нечёткие модели", "12:40", "14:00", "Городецкий Э.Р.", "412", 1, true, demoUserId),
            Lesson(0, "Алгоритмы и структуры данных", "14:10", "15:40", "Иванов И.И.", "207", 1, true, demoUserId),
            Lesson(0, "Математика", "16:00", "17:35", "Петров П.П.", "301", 2, false, demoUserId)
        )

        lessons.forEach {
            val cv = ContentValues().apply {
                put(COL_NAME, it.name)
                put(COL_START, it.startTime)
                put(COL_END, it.endTime)
                put(COL_TEACHER, it.teacher)
                put(COL_CLASSROOM, it.classroom)
                put(COL_WEEKDAY, it.weekday)
                put(COL_IS_EVEN, if (it.isEvenWeek) 1 else 0)
                put(COL_USER_ID, it.userId)
            }
            db.insert(TABLE_LESSONS, null, cv)
        }
    }

}
