package com.greendao

import org.greenrobot.greendao.generator.Entity
import org.greenrobot.greendao.generator.Property
import org.greenrobot.greendao.generator.Schema
import org.greenrobot.greendao.generator.DaoGenerator

class MyGenerator {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val schema = Schema(1, "com.example.systemtrackingtransport.db")

            // Создаем сущность Vehicle
            addVehicleEntity(schema)

            try {
                // Генерация DAO классов
                DaoGenerator().generateAll(schema, "app/src/main/java")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        // Добавляем сущность Vehicle
        private fun addVehicleEntity(schema: Schema): Entity {
            val vehicle = schema.addEntity("Vehicle")

            // Создаем поле id с автоинкрементом
            val idProperty: Property = vehicle.addIdProperty().autoincrement().getProperty()

            // Добавляем другие поля с их типами и обязательностью
            vehicle.addStringProperty("brand").notNull()
            vehicle.addStringProperty("model").notNull()
            vehicle.addStringProperty("year").notNull()
            vehicle.addStringProperty("type").notNull()

            // Возвращаем сущность
            return vehicle
        }
    }
}
