package com.example.madistudents

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.madistudents.databinding.ActivityMainBinding
import com.example.madistudents.forRecyclerView.CustomRecyclerAdapterForExams
import com.example.madistudents.forRecyclerView.RecyclerItemClickListener
import com.example.madistudents.ui.faculty.DbHelper
import com.example.madistudents.ui.faculty.Exam
import com.example.madistudents.ui.faculty.Group
import com.example.madistudents.ui.faculty.GroupOperator
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    ExamDetailsDialogFragment.OnInputListenerSortId
{
    private val gsonBuilder = GsonBuilder()
    private val gson: Gson = gsonBuilder.create()
    private val serverIP = "192.168.1.69"
    private val serverPort = 9876
    private lateinit var connection: Connection
    private var connectionStage: Int = 0
    private lateinit var dbh: DbHelper
    private var dbVersion = 2
    private var startTime: Long = 0

    private lateinit var textViewGroupName: TextView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var nv: NavigationView
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerViewExams: RecyclerView

    private val go: GroupOperator = GroupOperator()
    private var currentGroupID: Int = -1
    private var currentExamID: Int = -1
    private var waitingForUpdate: Boolean = false

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

//        binding.appBarMain.fab.setOnClickListener{ view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }

        textViewGroupName = findViewById(R.id.textViewGroupName)
        drawerLayout = binding.drawerLayout
        nv = binding.navView
        nv.setNavigationItemSelectedListener(this)
        toolbar = findViewById(R.id.toolbar)
        toolbar.apply { setNavigationIcon(R.drawable.ic_my_menu) }
        toolbar.setNavigationOnClickListener { drawerLayout.openDrawer(GravityCompat.START) }
        progressBar = findViewById(R.id.progressBar)
        recyclerViewExams = findViewById(R.id.recyclerViewExams)
        recyclerViewExams.visibility = View.INVISIBLE
        recyclerViewExams.layoutManager = LinearLayoutManager(this)

        recyclerViewExams.addOnItemTouchListener(
            RecyclerItemClickListener(
                recyclerViewExams,
                object : RecyclerItemClickListener.OnItemClickListener
                {
                    override fun onItemClick(view: View, position: Int)
                    {
                        currentExamID = position
                        val examDetails = ExamDetailsDialogFragment()
                        val tempExam = go.getExam(currentGroupID, currentExamID)
                        val bundle = Bundle()
                        bundle.putString("examName", tempExam.nameOfExam)
                        bundle.putString("teacherName", tempExam.nameOfTeacher)
                        bundle.putString("auditory", tempExam.auditory.toString())
                        bundle.putString("date", tempExam.date)
                        bundle.putString("time", tempExam.time)
                        bundle.putString("people", tempExam.peopleInAuditory.toString())
                        bundle.putString("abstract", tempExam.isAbstractAvailable.toString())
                        bundle.putString("comment", tempExam.comment)
                        bundle.putString("connection", connectionStage.toString())
                        examDetails.arguments = bundle
                        examDetails.show(fragmentManager, "MyCustomDialog")
                    }
                    override fun onItemLongClick(view: View, position: Int)
                    {
                        if (connectionStage == 1)
                        {
                            currentExamID = position
                            val manager: FragmentManager = supportFragmentManager
                            val myDialogFragmentDelExam = MyDialogFragmentDelExam()
                            val bundle = Bundle()
                            bundle.putString(
                                "exam",
                                go.getExam(currentGroupID, currentExamID).nameOfExam
                            )
                            myDialogFragmentDelExam.arguments = bundle
                            myDialogFragmentDelExam.show(manager, "myDialog")
                        }
                        else
                        {
                            val toast = Toast.makeText(
                                applicationContext,
                                "Приложение оффлайн!",
                                Toast.LENGTH_LONG
                            )
                            toast.show()
                        }
//                        val vibrator = this@MainActivity.getSystemService(
//                            Context.VIBRATOR_SERVICE) as Vibrator
//                        vibrator.vibrate(
//                            VibrationEffect.createOneShot(200
//                            , VibrationEffect.DEFAULT_AMPLITUDE))
                    }
                }
            )
        )

        dbh = DbHelper(this, "MyFirstDB", null, dbVersion)
        startTime = System.currentTimeMillis()
        connection = Connection(serverIP, serverPort, "{R}", this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean
    {
        val inflater = menuInflater
        inflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean
    {
        if (currentGroupID != -1)
        {
            menu.getItem(0).isVisible = true
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        val id = item.itemId
        if (id == R.id.action_add)
        {
            val intent = Intent()
            intent.setClass(this, EditExamActivity::class.java)
            intent.putExtra("action", 1)
            startActivityForResult(intent, 1)
        }
        return super.onOptionsItemSelected(item)
    }

    internal inner class Connection(
        private val SERVER_IP: String,
        private val SERVER_PORT: Int,
        private val refreshCommand: String,
        private val activity: Activity
    ) {
        private var outputServer: PrintWriter? = null
        private var inputServer: BufferedReader? = null
        var thread1: Thread? = null
        private var threadT: Thread? = null

        internal inner class Thread1Server : Runnable {
            override fun run()
            {
                val socket: Socket
                try {
                    socket = Socket(SERVER_IP, SERVER_PORT)
                    outputServer = PrintWriter(socket.getOutputStream())
                    inputServer = BufferedReader(InputStreamReader(socket.getInputStream()))
                    Thread(Thread2Server()).start()
                    sendDataToServer(refreshCommand)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

        internal inner class Thread2Server : Runnable {
            override fun run() {
                while (true) {
                    try {
                        val message = inputServer!!.readLine()
                        if (message != null)
                        {
                            activity.runOnUiThread { processingInputStream(message) }
                        } else {
                            thread1 = Thread(Thread1Server())
                            thread1!!.start()
                            return
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        }

        internal inner class Thread3Server(private val message: String) : Runnable
        {
            override fun run()
            {
                outputServer!!.write(message)
                outputServer!!.flush()
            }
        }

        internal inner class ThreadT : Runnable
        {
            override fun run() {
                while (true)
                {
                    if (System.currentTimeMillis() - startTime > 5000L && connectionStage == 0)
                    {
                        activity.runOnUiThread { val toast = Toast.makeText(
                            applicationContext,
                            "Подключиться не удалось!\n" +
                                    "Будут использоваться данные из локальной базы данных.",
                            Toast.LENGTH_LONG
                        )
                            toast.show() }
                        connectionStage = -1
                        progressBar.visibility = View.INVISIBLE
                        val tempArrayListGroups: ArrayList<Group> = dbh.getAllData()
                        go.setGroups(tempArrayListGroups)
                        for (i in 0 until tempArrayListGroups.size)
                        {
                            activity.runOnUiThread { nv.menu.add(0, i, 0,
                                tempArrayListGroups[i].name as CharSequence) }
                        }
                    }
                }
            }
        }

        fun sendDataToServer(text: String)
        {
            Thread(Thread3Server(text + "\n")).start()
        }

        private fun processingInputStream(text: String)
        {
            dbh.removeAllData()
            val tempGo: GroupOperator = gson.fromJson(text, GroupOperator::class.java)
            for (i in tempGo.getGroups())
            {
                dbh.insertGroup(i)
            }

            if (connectionStage != 1)
            {
                val toast = Toast.makeText(
                    applicationContext,
                    "Успешно подключено!\n" +
                            "Будут использоваться данные, полученные от сервера.",
                    Toast.LENGTH_LONG
                )
                toast.show()
            }

            progressBar.visibility = View.INVISIBLE
            for (i in 0 until go.getGroups().size)
            {
                nv.menu.removeItem(i)
            }
            val tempArrayListGroups: ArrayList<Group> = dbh.getAllData()
            go.setGroups(tempArrayListGroups)
            for (i in 0 until tempArrayListGroups.size)
            {
                nv.menu.add(
                    0, i, 0,
                    tempArrayListGroups[i].name as CharSequence
                )
            }
            if (waitingForUpdate || connectionStage == -1)
            {
                waitingForUpdate = false
                recyclerViewExams.adapter = CustomRecyclerAdapterForExams(
                    go.getExamsNames(currentGroupID),
                    go.getTeachersNames(currentGroupID)
                )
            }
            connectionStage = 1
        }

        init {
            thread1 = Thread(Thread1Server())
            thread1!!.start()
            threadT = Thread(ThreadT())
            threadT!!.start()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
//        val toast = Toast.makeText(   // не удалять, заменить
//            applicationContext,
//            "Выбрана группа: $item.",
//            Toast.LENGTH_SHORT
//        )
//        toast.show()
        //toolbar.title = "Группа ${item.title}"
        val tempString = "Группа ${item.title}"
        textViewGroupName.text = tempString
        invalidateOptionsMenu()
        currentGroupID = item.itemId
        recyclerViewExams.adapter = CustomRecyclerAdapterForExams(go.getExamsNames(currentGroupID),
            go.getTeachersNames(currentGroupID))
        recyclerViewExams.visibility = View.VISIBLE
        return true
    }

    fun delExam()
    {
        connection.sendDataToServer("d$currentGroupID,$currentExamID")
        waitingForUpdate = true
    }

    override fun sendInputSortId(sortId: Int)
    {
        val toast = Toast.makeText(
            applicationContext,
            "ID параметра для сортировки: $sortId.",
            Toast.LENGTH_LONG
        )
        toast.show()
        if (sortId > -1 && sortId < 8)      // Сортировка
        {
            go.sortExams(currentGroupID, sortId)
            connection.sendDataToServer("u" + gson.toJson(go))
        }
        if (sortId == 8)        // Удаление
        {
            val manager: FragmentManager = supportFragmentManager
            val myDialogFragmentDelExam = MyDialogFragmentDelExam()
            val bundle = Bundle()
            bundle.putString("exam", go.getExam(currentGroupID, currentExamID).nameOfExam)
            myDialogFragmentDelExam.arguments = bundle
            myDialogFragmentDelExam.show(manager, "myDialog")
        }
        if (sortId == 9)        // Изменение
        {
            val tempExam = go.getExam(currentGroupID, currentExamID)
            val intent = Intent()
            intent.setClass(this, EditExamActivity::class.java)
            intent.putExtra("action", 2)
            intent.putExtra("exam", tempExam.nameOfExam)
            intent.putExtra("teacher", tempExam.nameOfTeacher)
            intent.putExtra("auditory", tempExam.auditory.toString())
            intent.putExtra("date", tempExam.date)
            intent.putExtra("time", tempExam.time)
            intent.putExtra("people", tempExam.peopleInAuditory.toString())
            intent.putExtra("abstract", tempExam.isAbstractAvailable.toString())
            intent.putExtra("comment", tempExam.comment)
            startActivityForResult(intent, 1)
        }
        recyclerViewExams.adapter = CustomRecyclerAdapterForExams(
            go.getExamsNames(currentGroupID),
            go.getTeachersNames(currentGroupID))
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK)
        {
            val action = data?.getSerializableExtra("action") as Int
            val examName = data.getSerializableExtra("exam") as String
            val teacherName = data.getSerializableExtra("teacher") as String
            val auditory = data.getSerializableExtra("auditory") as Int
            val date = data.getSerializableExtra("date") as String
            val time = data.getSerializableExtra("time") as String
            val people = data.getSerializableExtra("people") as Int
            val abstract = data.getSerializableExtra("abstract") as Int
            val comment = data.getSerializableExtra("comment") as String
            val tempExam = Exam(examName, teacherName, auditory, date, time, people
                , abstract, comment)
            val tempExamJSON: String = gson.toJson(tempExam)

            if (action == 1)
            {
                val tempStringToSend = "a$currentGroupID##$tempExamJSON"
                connection.sendDataToServer(tempStringToSend)
                waitingForUpdate = true
            }
            if (action == 2)
            {
                val tempStringToSend = "e${go.getGroups()[currentGroupID].name}" +
                        ",$currentExamID##$tempExamJSON"
                connection.sendDataToServer(tempStringToSend)
                waitingForUpdate = true
            }
        }
    }
}
