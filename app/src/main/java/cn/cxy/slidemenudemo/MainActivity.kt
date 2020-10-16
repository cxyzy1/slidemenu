package cn.cxy.slidemenudemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        slideLayout.setCallback(object : OnMenuSelect {
            override fun onSelected(view: ViewGroup) {
                when (view.id) {
                    R.id.leftMenuLayout -> toast("选中左侧菜单")
                    R.id.rightMenuLayout -> toast("选中右侧菜单")
                }
            }
        })
    }

    private fun toast(text: String) {
        Toast.makeText(this@MainActivity, text, Toast.LENGTH_LONG).show()
    }
}