package com.subaiqiao.androidutils.modules.privacyData.contactPerson

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.ContentValues
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.subaiqiao.androidutils.R

class ContactPersonActivity : AppCompatActivity() {

    lateinit var lv_list: ListView
    lateinit var adapter: ArrayAdapter<String>
    val contactList = ArrayList<String>()

    val READ_CONTACTS_PMSN = Manifest.permission.READ_CONTACTS
    val WRITE_CONTACTS_PMSN = Manifest.permission.WRITE_CONTACTS
    val GRANTED = PackageManager.PERMISSION_GRANTED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_person)
        lv_list = findViewById(R.id.lv_list)
        val tv_title: TextView = findViewById(R.id.tv_title)
        tv_title.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, WRITE_CONTACTS_PMSN) != GRANTED) {
                // 没有权限,去申请
                ActivityCompat.requestPermissions(this, arrayOf(WRITE_CONTACTS_PMSN), 1)
            } else {
                // 已经有写入通讯录的权限
                addNewContact("张三", "13135155110")
                readContact()
            }
        }

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

    // 写入通讯录
    private fun addNewContact(name: String, phone: String) {
        val contentResolver = contentResolver
        // 第一步：插入 RawContacts 表，得到新建联系人的ID
        val rawContactUri = contentResolver.insert(ContactsContract.RawContacts.CONTENT_URI, ContentValues())
        val rawContactId = ContentUris.parseId(rawContactUri!!)
        // 第二步：插入姓名
        val nameValues = ContentValues().apply {
            put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId)
            put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
            put(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name)
        }
        contentResolver.insert(ContactsContract.Data.CONTENT_URI, nameValues)
        // 第三步：插入电话号码
        val phoneValues = ContentValues().apply {
            put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId)
            put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
            put(ContactsContract.CommonDataKinds.Phone.NUMBER, phone)
            put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
        }
        contentResolver.insert(ContactsContract.Data.CONTENT_URI, phoneValues)
        // 成功提示
        Toast.makeText(this, "通讯录添加成功: $name, $phone", Toast.LENGTH_SHORT).show()
    }


    // 读取联系人数据
    @SuppressLint("Range")
    fun readContact() {
        contactList.clear()
        val uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        val nameColStr = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
        val numberColStr = ContactsContract.CommonDataKinds.Phone.NUMBER
        val idColStr = ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID
        val mimetypeColStr = ContactsContract.CommonDataKinds.Phone.MIMETYPE
        contentResolver.query(uri, null, null, null, null)
            ?.apply {
                while (moveToNext()) {
                    val name = getString(getColumnIndex(nameColStr))
                    val number = getString(getColumnIndex(numberColStr))
                    val id = getString(getColumnIndex(idColStr))
                    val mimetype = getString(getColumnIndex(mimetypeColStr))
                    contactList.add("$name\n$number\n$id\t$mimetype")
                }
                adapter.notifyDataSetChanged()
                close()
            }
    }
}