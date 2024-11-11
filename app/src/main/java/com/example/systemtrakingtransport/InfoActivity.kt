package com.example.systemtrakingtransport

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.systemtrackingtransport.DatabaseHelper
import com.example.systemtrackingtransport.db.DaoSession
import com.example.systemtrackingtransport.db.Vehicle

class InfoActivity : AppCompatActivity() {
    private lateinit var rvVehicleList: RecyclerView
    private lateinit var vehicleAdapter: VehicleAdapter
    private val vehicleInfoList = mutableListOf<Vehicle>()
    private lateinit var daoSession: DaoSession
    private var selectedVehicleId: Long = -1 // Храним ID выбранного автомобиля

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        val databaseHelper = DatabaseHelper(this)
        daoSession = databaseHelper.getDaoSessionInstance()

        rvVehicleList = findViewById(R.id.rvVehicleList)
        rvVehicleList.layoutManager = GridLayoutManager(this, 3)

        // Извлекаем ID автомобиля из Intent
        selectedVehicleId = intent.getLongExtra("vehicleId", -1)

        // Инициализация адаптера ДО загрузки данных
        vehicleAdapter = VehicleAdapter(vehicleInfoList) { position ->
            vehicleAdapter.setSelectedItem(position)
        }
        rvVehicleList.adapter = vehicleAdapter

        // Загружаем данные из базы
        loadVehiclesFromDatabase()

        // Установка обработчика свайпов
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val vehicle = vehicleInfoList[position]

                when (direction) {
                    ItemTouchHelper.LEFT -> {
                        daoSession.vehicleDao.delete(vehicle)
                        vehicleInfoList.removeAt(position)
                        vehicleAdapter.notifyDataSetChanged()
                    }
                    ItemTouchHelper.RIGHT -> {
                        editItem(position)
                    }
                }
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(rvVehicleList)
    }

    private fun loadVehiclesFromDatabase() {
        val vehicles = daoSession.vehicleDao.loadAll()
        vehicleInfoList.clear()
        vehicleInfoList.addAll(vehicles)

        // Если передан ID, выделяем его в списке
        selectedVehicleId.takeIf { it != -1L }?.let { id ->
            val selectedVehiclePosition = vehicleInfoList.indexOfFirst { it.id == id }
            if (selectedVehiclePosition != -1) {
                vehicleAdapter.setSelectedItem(selectedVehiclePosition)
            }
        }

        vehicleAdapter.notifyDataSetChanged()
    }

    private fun editItem(position: Int) {
        val vehicle = vehicleInfoList[position]
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("vehicleId", vehicle.id)  // Передаем ID автомобиля для редактирования
        startActivityForResult(intent, REQUEST_EDIT)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_EDIT && resultCode == RESULT_OK) {
            val updatedVehicleId = data?.getLongExtra("updatedVehicleId", -1) ?: -1
            val updatedPosition = data?.getIntExtra("editPosition", -1) ?: -1

            if (updatedVehicleId != -1L && updatedPosition != -1) {
                // Обновляем информацию о транспортном средстве в списке
                val updatedVehicle = daoSession.vehicleDao.load(updatedVehicleId)
                vehicleInfoList[updatedPosition] = updatedVehicle
                vehicleAdapter.notifyItemChanged(updatedPosition)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.pageMain -> {
                val intent = Intent(this, MainActivity::class.java).apply {
                }
                startActivity(intent)
                return true
            }
            R.id.pageInfo -> return true
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val REQUEST_EDIT = 1
    }
}