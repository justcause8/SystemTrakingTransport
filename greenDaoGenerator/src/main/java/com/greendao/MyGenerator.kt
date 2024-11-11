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

            // Создаем таблицы для седанов, универсалов и внедорожников
            addSedanTable(schema)
            addWagonTable(schema)
            addSuvTable(schema)
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
            vehicle.addIdProperty().autoincrement().primaryKey()

            // Добавляем другие поля с их типами и обязательностью
            vehicle.addStringProperty("brand").notNull()
            vehicle.addStringProperty("model").notNull()
            vehicle.addStringProperty("year").notNull()
            vehicle.addStringProperty("type").notNull()

            // Возвращаем сущность
            return vehicle
        }

        // Создаем таблицу для седанов
        private fun addSedanTable(schema: Schema): Entity {
            val sedan = schema.addEntity("Sedan")

            // Поле id из таблицы Vehicle (связывает Sedan с конкретным Vehicle)
            sedan.addLongProperty("vehicleId").notNull()

            return sedan
        }

        // Создаем таблицу для универсалов
        private fun addWagonTable(schema: Schema): Entity {
            val wagon = schema.addEntity("Wagon")

            // Поле id из таблицы Vehicle (связывает Wagon с конкретным Vehicle)
            wagon.addLongProperty("vehicleId").notNull()

            return wagon
        }

        // Создаем таблицу для внедорожников
        private fun addSuvTable(schema: Schema): Entity {
            val suv = schema.addEntity("Suv")

            // Поле id из таблицы Vehicle (связывает Suv с конкретным Vehicle)
            suv.addLongProperty("vehicleId").notNull()

            return suv
        }
    }
}
