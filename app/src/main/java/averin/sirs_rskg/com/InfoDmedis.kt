package averin.sirs_rskg.com

import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import de.hdodenhof.circleimageview.CircleImageView

class InfoDmedis : AppCompatActivity() {
    var no_ktp: String? = null
    var val_token: String? = null
    var np: String? = null
    var alamat: String? = null
    var idk: String? = null
    var kota: String? = null
    var telpon1: String? = null
    var email: String? = null
    var ket: String? = null
    var urllogo: String? = null
    var spnidKota: String? = null
    var spnidProv: String? = null
    var code_menu = "5"
    var txt_login: TextView? = null
    var txt_namaPasien: TextView? = null
    var txt_noktp: TextView? = null
    var txt_infonull: TextView? = null
    var txt_lbl: TextView? = null
    var fotoPasien: CircleImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_dmedis)

        //menerapkan tool bar sesuai id toolbar | ToolBarAtas adalah variabel buatan sndiri
        val LabToolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(LabToolbar)
        LabToolbar.logoDescription = resources.getString(R.string.app_name)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        LabToolbar.setNavigationOnClickListener { onBackPressed() }

        //GET DATA FROM CONTROLLER
        val login = AppController.getInstance(this).pasien
        val token = AppController.getInstance(this).isiToken()
        val_token = token.gettoken().toString()
        no_ktp = login.ktP_pasien.toString()
    }
}