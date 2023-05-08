package com.example.simpletodoapp.todo.data.local

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RenameColumn
import androidx.room.RoomDatabase
import androidx.room.migration.AutoMigrationSpec

@Database(
    entities = [TodoEntity::class],
    version = 2,
    autoMigrations = [
        AutoMigration(from = 1, to = 2, spec = MigrationFrom1To2::class)
    ]
)
abstract class TodoDatabase : RoomDatabase() {
    abstract val todoDao: TodoDao

    companion object {
        const val DATABASE_NAME = "todo_database"
    }
}

@RenameColumn(
    tableName = TodoEntity.TODO_TABLE_NAME,
    fromColumnName = "id",
    toColumnName = TodoEntity.ID_COLUMN_NAME
)
private class MigrationFrom1To2 : AutoMigrationSpec