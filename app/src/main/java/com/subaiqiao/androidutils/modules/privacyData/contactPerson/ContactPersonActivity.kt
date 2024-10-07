package com.subaiqiao.androidutils.modules.privacyData.contactPerson

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.subaiqiao.androidutils.R

class ContactPersonActivity : AppCompatActivity() {

    lateinit var lv_list: ListView
    lateinit var adapter: ArrayAdapter<String>
    val contactList = ArrayList<String>()

    val READ_CONTACTS_PMSN = Manifest.permission.READ_CONTACTS
    val GRANTED = PackageManager.PERMISSION_GRANTED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_person)
        lv_list = findViewById(R.id.lv_list)

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, contactList)
        lv_list.adapter = adapter

        if (ContextCompat.checkSelfPermission(this, READ_CONTACTS_PMSN) != GRANTED) {
            // 没有权限,去申请
            ActivityCompat.requestPermissions(this, arrayOf(READ_CONTACTS_PMSN), 1)
        } else {
            // 已经有读取通讯录的权限
            readContact()
        }
    }

    // 申请权限回调
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == GRANTED) {
                    readContact()
                } else {
                    Toast.makeText(this, "您拒绝了读取通讯录权限", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // 读取联系人数据
    @SuppressLint("Range")
    fun readContact() {
        val uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        val nameColStr = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
        val numberColStr = ContactsContract.CommonDataKinds.Phone.NUMBER
        contentResolver.query(uri, null, null, null, null)
            ?.apply {
                while (moveToNext()) {
                    val name = getString(getColumnIndex(nameColStr))
                    val number = getString(getColumnIndex(numberColStr))
                    contactList.add("$name\n$number")
                }
                adapter.notifyDataSetChanged()
                close()
            }
    }
}