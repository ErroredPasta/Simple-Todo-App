package com.example.simpletodoapp.todo.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TodoDaoTest {
    private lateinit var sut: TodoDao
    private lateinit var database: TodoDatabase

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            context = ApplicationProvider.getApplicationContext(),
            klass = TodoDatabase::class.java
        ).build()

        sut = database.todoDao
    }

    @After
    fun tearDown() {
        database.close()
    }

}