package com.lascenify.sunshine.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.lascenify.sunshine.data.TestUtilities.getConstantNameByStringValue
import com.lascenify.sunshine.data.TestUtilities.getStaticIntegerField
import com.lascenify.sunshine.data.TestUtilities.getStaticStringField
import com.lascenify.sunshine.data.TestUtilities.studentReadableClassNotFound
import com.lascenify.sunshine.data.TestUtilities.studentReadableNoSuchField
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.reflect.InvocationTargetException
import java.util.*
import java.util.concurrent.TimeUnit


/**
 * Used to test the database we use in Sunshine to cache weather data. Within these tests, we
 * test the following:
 *
 * 1) Creation of the database with proper table(s)
 * 2) Insertion of single record into our weather table
 * 3) When a record is already stored in the weather table with a particular date, a new record
 * with the same date will overwrite that record.
 * 4) Verify that NON NULL constraints are working properly on record inserts
 * 5) Verify auto increment is working with the ID
 * 6) Test the onUpgrade functionality of the WeatherDbHelper
 */

@RunWith(AndroidJUnit4::class)
class DatabaseTest {
    /*
     * Context used to perform operations on the database and create WeatherDbHelpers.
     */
    private val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
    private var weatherEntryClass: Class<*>? = null
    private var weatherDbHelperClass: Class<*>? = null
    private var database: SQLiteDatabase? = null
    private var dbHelper: SQLiteOpenHelper? = null
    @Before
    fun before() {
        try {
            weatherEntryClass =
                Class.forName(dataPackageName + weatherEntryName)
            if (!BaseColumns::class.java.isAssignableFrom(weatherEntryClass!!)) {
                val weatherEntryDoesNotImplementBaseColumns =
                    "WeatherEntry class needs to " +
                            "implement the interface BaseColumns, but does not."
                Assert.fail(weatherEntryDoesNotImplementBaseColumns)
            }
            REFLECTED_TABLE_NAME = getStaticStringField(weatherEntryClass!!,
                tableNameVariableName
            )
            REFLECTED_COLUMN_DATE = getStaticStringField(weatherEntryClass!!,
                columnDateVariableName
            )
            REFLECTED_COLUMN_WEATHER_ID = getStaticStringField(weatherEntryClass!!,
                columnWeatherIdVariableName
            )
            REFLECTED_COLUMN_MIN = getStaticStringField(weatherEntryClass!!,
                columnMinVariableName
            )
            REFLECTED_COLUMN_MAX = getStaticStringField(weatherEntryClass!!,
                columnMaxVariableName
            )
            REFLECTED_COLUMN_HUMIDITY = getStaticStringField(weatherEntryClass!!,
                columnHumidityVariableName
            )
            REFLECTED_COLUMN_PRESSURE = getStaticStringField(weatherEntryClass!!,
                columnPressureVariableName
            )
            REFLECTED_COLUMN_WIND_SPEED = getStaticStringField(weatherEntryClass!!,
                columnWindSpeedVariableName
            )
            REFLECTED_COLUMN_WIND_DIR = getStaticStringField(weatherEntryClass!!,
                columnWindDirVariableName
            )

            weatherDbHelperClass =
                Class.forName(dataPackageName + weatherDbHelperName)
            val weatherDbHelperSuperclass =
                weatherDbHelperClass!!.superclass
            if (weatherDbHelperSuperclass == null || weatherDbHelperSuperclass == Any::class.java) {
                val noExplicitSuperclass =
                    "WeatherDbHelper needs to extend SQLiteOpenHelper, but yours currently doesn't extend a class at all."
                Assert.fail(noExplicitSuperclass)
            } else if (weatherDbHelperSuperclass != null) {
                val weatherDbHelperSuperclassName =
                    weatherDbHelperSuperclass.simpleName
                val doesNotExtendOpenHelper =
                    ("WeatherDbHelper needs to extend SQLiteOpenHelper but yours extends "
                            + weatherDbHelperSuperclassName)
                Assert.assertTrue(
                    doesNotExtendOpenHelper,
                    SQLiteOpenHelper::class.java.isAssignableFrom(weatherDbHelperSuperclass)
                )
            }
            REFLECTED_DATABASE_NAME = getStaticStringField(weatherDbHelperClass!!,
                databaseNameVariableName
            )
            REFLECTED_DATABASE_VERSION = getStaticIntegerField(weatherDbHelperClass!!,
                databaseVersionVariableName
            )

            val weatherDbHelperCtor =
                weatherDbHelperClass!!.getConstructor(
                    Context::class.java
                )
            dbHelper = weatherDbHelperCtor.newInstance(context) as SQLiteOpenHelper
            context.deleteDatabase(REFLECTED_DATABASE_NAME)
            val getWritableDatabase =
                SQLiteOpenHelper::class.java.getDeclaredMethod("getWritableDatabase")
            database = getWritableDatabase.invoke(dbHelper) as SQLiteDatabase
        } catch (e: ClassNotFoundException) {
            Assert.fail(studentReadableClassNotFound(e))
        } catch (e: NoSuchFieldException) {
            Assert.fail(studentReadableNoSuchField(e))
        } catch (e: IllegalAccessException) {
            Assert.fail(e.message)
        } catch (e: NoSuchMethodException) {
            Assert.fail(e.message)
        } catch (e: InstantiationException) {
            Assert.fail(e.message)
        } catch (e: InvocationTargetException) {
            Assert.fail(e.message)
        }
    }

    @Test
    fun testDatabaseVersionWasIncremented() {
        val expectedDatabaseVersion = 3
        val databaseVersionShouldBe1 = ("Database version should be "
                + expectedDatabaseVersion + " but isn't."
                + "\n Database version: ")
        Assert.assertEquals(
            databaseVersionShouldBe1,
            expectedDatabaseVersion,
            REFLECTED_DATABASE_VERSION
        )
    }

    @Test
    fun testNullColumnConstraints() { /* Use a WeatherDbHelper to get access to a writable database */ /* We need a cursor from a weather table query to access the column names */
        val weatherTableCursor = database!!.query(
            REFLECTED_TABLE_NAME,  /* We don't care about specifications, we just want the column names */
            null, null, null, null, null, null
        )
        /* Store the column names and close the cursor */
        val weatherTableColumnNames =
            weatherTableCursor.columnNames
        weatherTableCursor.close()
        /* Obtain weather values from TestUtilities and make a copy to avoid altering singleton */
        val testValues =
            TestUtilities.createTestWeatherContentValues()
        /* Create a copy of the testValues to save as a reference point to restore values */
        val testValuesReferenceCopy = ContentValues(testValues)
        for (columnName in weatherTableColumnNames) { /* We don't need to verify the _ID column value is not null, the system does */
            if (columnName == WeatherContract.WeatherEntry._ID) continue
            /* Set the value to null */testValues.putNull(columnName)
            /* Insert ContentValues into database and get a row ID back */
            val shouldFailRowId = database!!.insert(
                REFLECTED_TABLE_NAME,
                null,
                testValues
            )
            val variableName: String = getConstantNameByStringValue(
                WeatherContract.WeatherEntry::class.java,
                columnName)!!
            /* If the insert fails, which it should in this case, database.insert returns -1 */
            val nullRowInsertShouldFail =
                ("Insert should have failed due to a null value for column: '" + columnName + "'"
                        + ", but didn't."
                        + "\n Check that you've added NOT NULL to " + variableName
                        + " in your create table statement in the WeatherEntry class."
                        + "\n Row ID: ")
            Assert.assertEquals(
                nullRowInsertShouldFail,
                -1,
                shouldFailRowId
            )
            /* "Restore" the original value in testValues */testValues.put(
                columnName,
                testValuesReferenceCopy.getAsDouble(columnName)
            )
        }
        /* Close database */dbHelper!!.close()
    }

    /**
     * Tests to ensure that inserts into your database results in automatically
     * incrementing row IDs.
     * >>>>>>> 4174cf2... S07.02-Exercise-PreventInvalidInserts
     */
    @Test
    fun testIntegerAutoincrement() { /* First, let's ensure we have some values in our table initially */
        testInsertSingleRecordIntoWeatherTable()
        /* Obtain weather values from TestUtilities */
        val testWeatherValues =
            TestUtilities.createTestWeatherContentValues()
        /* Get the date of the testWeatherValues to ensure we use a different date later */
        val originalDate =
            testWeatherValues.getAsLong(REFLECTED_COLUMN_DATE)
        /* Insert ContentValues into database and get a row ID back */
        val firstRowId = database!!.insert(
            REFLECTED_TABLE_NAME,
            null,
            testWeatherValues
        )
        /* Delete the row we just inserted to see if the database will reuse the rowID */database!!.delete(
            REFLECTED_TABLE_NAME,
            "_ID == $firstRowId",
            null
        )
        /*
         * Now we need to change the date associated with our test content values because the
         * database policy is to replace identical dates on conflict.
         */
        val dayAfterOriginalDate =
            originalDate + TimeUnit.DAYS.toMillis(1)
        testWeatherValues.put(
            REFLECTED_COLUMN_DATE,
            dayAfterOriginalDate
        )
        /* Insert ContentValues into database and get another row ID back */
        val secondRowId = database!!.insert(
            REFLECTED_TABLE_NAME,
            null,
            testWeatherValues
        )
        val sequentialInsertsDoNotAutoIncrementId =
            "IDs were reused and shouldn't be if autoincrement is setup properly."
        Assert.assertNotSame(
            sequentialInsertsDoNotAutoIncrementId,
            firstRowId, secondRowId
        )
    }

    /**
     * This method tests the [WeatherDbHelper.onUpgrade]. The proper
     * behavior for this method in our case is to simply DROP (or delete) the weather table from
     * the database and then have the table recreated.
     */
    @Test
    fun testOnUpgradeBehavesCorrectly() {
        testInsertSingleRecordIntoWeatherTable()
        dbHelper!!.onUpgrade(database, 13, 14)
        /*
         * This Cursor will contain the names of each table in our database and we will use it to
         * make sure that our weather table is still in the database after upgrading.
         */
        val tableNameCursor = database!!.rawQuery(
            "SELECT name FROM sqlite_master WHERE type='table' AND name='" + REFLECTED_TABLE_NAME + "'",
            null
        )
        /*
         * Our database should only contain one table, and so the above query should have one
         * record in the cursor that queried for our table names.
         */
        val expectedTableCount = 1
        val shouldHaveSingleTable =
            "There should only be one table returned from this query."
        Assert.assertEquals(
            shouldHaveSingleTable,
            expectedTableCount,
            tableNameCursor.count
        )
        /* We are done verifying our table names, so we can close this cursor */tableNameCursor.close()
        val shouldBeEmptyWeatherCursor = database!!.query(
            REFLECTED_TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            null
        )
        val expectedRecordCountAfterUpgrade = 0
        /* We will finally verify that our weather table is empty after */
        val weatherTableShouldBeEmpty =
            ("Weather table should be empty after upgrade, but wasn't."
                    + "\nNumber of records: ")
        Assert.assertEquals(
            weatherTableShouldBeEmpty,
            expectedRecordCountAfterUpgrade,
            shouldBeEmptyWeatherCursor.count
        )
        /* Test is over, close the cursor */database!!.close()
    }

    /**
     * This method tests that our database contains all of the tables that we think it should
     * contain. Although in our case, we just have one table that we expect should be added
     *
     *
     * [com.example.android.sunshine.data.WeatherContract.WeatherEntry.TABLE_NAME].
     *
     *
     * Despite only needing to check one table name in Sunshine, we set this method up so that
     * you can use it in other apps to test databases with more than one table.
     */
    @Test
    fun testCreateDb() { /*
         * Will contain the name of every table in our database. Even though in our case, we only
         * have only table, in many cases, there are multiple tables. Because of that, we are
         * showing you how to test that a database with multiple tables was created properly.
         */
        val tableNameHashSet = HashSet<String>()
        /* Here, we add the name of our only table in this particular database */tableNameHashSet.add(
            REFLECTED_TABLE_NAME!!
        )
        /* Students, here is where you would add any other table names if you had them */ //        tableNameHashSet.add(MyAwesomeSuperCoolTableName);
//        tableNameHashSet.add(MyOtherCoolTableNameThatContainsOtherCoolData);
/* We think the database is open, let's verify that here */
        val databaseIsNotOpen = "The database should be open and isn't"
        Assert.assertEquals(
            databaseIsNotOpen,
            true,
            database!!.isOpen
        )
        /* This Cursor will contain the names of each table in our database */
        val tableNameCursor = database!!.rawQuery(
            "SELECT name FROM sqlite_master WHERE type='table'",
            null
        )
        /*
         * If tableNameCursor.moveToFirst returns false from this query, it means the database
         * wasn't created properly. In actuality, it means that your database contains no tables.
         */
        val errorInCreatingDatabase =
            "Error: This means that the database has not been created correctly"
        Assert.assertTrue(
            errorInCreatingDatabase,
            tableNameCursor.moveToFirst()
        )
        /*
         * tableNameCursor contains the name of each table in this database. Here, we loop over
         * each table that was ACTUALLY created in the database and remove it from the
         * tableNameHashSet to keep track of the fact that was added. At the end of this loop, we
         * should have removed every table name that we thought we should have in our database.
         * If the tableNameHashSet isn't empty after this loop, there was a table that wasn't
         * created properly.
         */do {
            tableNameHashSet.remove(tableNameCursor.getString(0))
        } while (tableNameCursor.moveToNext())
        /* If this fails, it means that your database doesn't contain the expected table(s) */Assert.assertTrue(
            "Error: Your database was created without the expected tables.",
            tableNameHashSet.isEmpty()
        )
        /* Always close the cursor when you are finished with it */tableNameCursor.close()
    }

    /**
     * This method tests inserting a single record into an empty table from a brand new database.
     * It will fail for the following reasons:
     *
     *
     * 1) Problem creating the database
     * 2) A value of -1 for the ID of a single, inserted record
     * 3) An empty cursor returned from query on the weather table
     * 4) Actual values of weather data not matching the values from TestUtilities
     */
    @Test
    fun testInsertSingleRecordIntoWeatherTable() { /* Obtain weather values from TestUtilities */
        val testWeatherValues =
            TestUtilities.createTestWeatherContentValues()
        /* Insert ContentValues into database and get a row ID back */
        val weatherRowId = database!!.insert(
            REFLECTED_TABLE_NAME,
            null,
            testWeatherValues
        )
        /* If the insert fails, database.insert returns -1 */
        val valueOfIdIfInsertFails = -1
        val insertFailed = "Unable to insert into the database"
        Assert.assertNotSame(
            insertFailed,
            valueOfIdIfInsertFails,
            weatherRowId
        )
        /*
         * Query the database and receive a Cursor. A Cursor is the primary way to interact with
         * a database in Android.
         */
        val weatherCursor =
            database!!.query( /* Name of table on which to perform the query */
                REFLECTED_TABLE_NAME,  /* Columns; leaving this null returns every column in the table */
                null,  /* Optional specification for columns in the "where" clause above */
                null,  /* Values for "where" clause */
                null,  /* Columns to group by */
                null,  /* Columns to filter by row groups */
                null,  /* Sort order to return in Cursor */
                null
            )
        /* Cursor.moveToFirst will return false if there are no records returned from your query */
        val emptyQueryError = "Error: No Records returned from weather query"
        Assert.assertTrue(
            emptyQueryError,
            weatherCursor.moveToFirst()
        )
        /* Verify that the returned results match the expected results */
        val expectedWeatherDidntMatchActual =
            "Expected weather values didn't match actual values."
        TestUtilities.validateCurrentRecord(
            expectedWeatherDidntMatchActual,
            weatherCursor,
            testWeatherValues
        )
        /*
         * Since before every method annotated with the @Test annotation, the database is
         * deleted, we can assume in this method that there should only be one record in our
         * Weather table because we inserted it. If there is more than one record, an issue has
         * occurred.
         */Assert.assertFalse(
            "Error: More than one record returned from weather query",
            weatherCursor.moveToNext()
        )
        /* Close cursor */weatherCursor.close()
    }

    companion object {
        /*
     * In order to verify that you have set up your classes properly and followed our TODOs, we
     * need to create what's called a Change Detector Test. In almost any other situation, these
     * tests are discouraged, as they provide no real value in a production setting. However, using
     * reflection to verify that you have set your classes up correctly will help provide more
     * useful errors if you've missed a step in our instructions.
     *
     * Additionally, using reflection for these tests allows you to run the tests when they
     * normally wouldn't compile, as they depend on pieces of your classes that you might not
     * have created when you initially run the tests.
     */
        private const val packageName = "com.lascenify.sunshine"
        private const val dataPackageName: String = "$packageName.data"
        private const val weatherContractName = ".WeatherContract"
        private const val weatherEntryName: String = "$weatherContractName\$WeatherEntry"
        private const val weatherDbHelperName = ".WeatherDbHelper"
        private const val databaseNameVariableName = "DATABASE_NAME"
        private var REFLECTED_DATABASE_NAME: String? = null
        private const val databaseVersionVariableName = "DATABASE_VERSION"
        private var REFLECTED_DATABASE_VERSION = 0
        private const val tableNameVariableName = "TABLE_NAME"
        private var REFLECTED_TABLE_NAME: String? = null
        private const val columnDateVariableName = "COLUMN_DATE"
        var REFLECTED_COLUMN_DATE: String? = null
        private const val columnWeatherIdVariableName = "COLUMN_WEATHER_ID"
        var REFLECTED_COLUMN_WEATHER_ID: String? = null
        private const val columnMinVariableName = "COLUMN_MIN_TEMP"
        var REFLECTED_COLUMN_MIN: String? = null
        private const val columnMaxVariableName = "COLUMN_MAX_TEMP"
        var REFLECTED_COLUMN_MAX: String? = null
        private const val columnHumidityVariableName = "COLUMN_HUMIDITY"
        var REFLECTED_COLUMN_HUMIDITY: String? = null
        private const val columnPressureVariableName = "COLUMN_PRESSURE"
        var REFLECTED_COLUMN_PRESSURE: String? = null
        private const val columnWindSpeedVariableName = "COLUMN_WIND_SPEED"
        var REFLECTED_COLUMN_WIND_SPEED: String? = null
        private const val columnWindDirVariableName = "COLUMN_DEGREES"
        var REFLECTED_COLUMN_WIND_DIR: String? = null
    }
}