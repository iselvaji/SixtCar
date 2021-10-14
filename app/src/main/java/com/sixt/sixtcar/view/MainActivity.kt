package com.sixt.sixtcar.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.sixt.sixtcar.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager
            .beginTransaction()
            .add(R.id.container, VehicleListFragment.newInstance(), "vecList")
            .commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.getItemId()) {
            R.id.action_map -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.container, VehicleMapFragment.newInstance(), "vecMap")
                    .commit()
                true
            }
            R.id.action_list -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.container, VehicleListFragment.newInstance(), "vecList")
                    .commit()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}