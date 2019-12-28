package com.dk.project.bowling.ui.activity

import android.app.Activity
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.util.Pair
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.dk.project.bowling.R
import com.dk.project.bowling.databinding.ActivityCreateGameBinding
import com.dk.project.bowling.shareData.ShareData
import com.dk.project.bowling.ui.adapter.CreateGameAdapter
import com.dk.project.bowling.ui.adapter.callback.ItemMoveCallback
import com.dk.project.bowling.viewModel.CreateGameViewModel
import com.dk.project.bowling.viewModel.impl.CreateGameListener
import com.dk.project.post.base.BindActivity
import com.dk.project.post.base.Define
import com.dk.project.post.bowling.model.GameModel
import com.dk.project.post.bowling.model.ScoreClubUserModel
import com.dk.project.post.bowling.retrofit.BowlingApi
import com.dk.project.post.utils.AlertDialogUtil
import com.dk.project.post.utils.RxBus
import com.dk.project.post.utils.Utils
import java.util.*

class CreateGameActivity : BindActivity<ActivityCreateGameBinding, CreateGameViewModel>(),
    CreateGameListener {

    private val calendar = Calendar.getInstance().also {
        it[Calendar.MILLISECOND] = 0
    }

    private val tempCalendar = Calendar.getInstance().also {
        it[Calendar.MILLISECOND] = 0
    }

    private lateinit var createGameAdapter: CreateGameAdapter

    override fun getLayoutId() = R.layout.activity_create_game

    override fun getViewModel() = ViewModelProvider(
        viewModelStore,
        defaultViewModelProviderFactory
    ).get(CreateGameViewModel::class.java)

    override fun subscribeToModel() {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ShareData.getInstance().scoreList.map { it.isCheck = false }

        var teamTitleClickListener: View.OnClickListener = View.OnClickListener {
            it as EditText
            var title = it.text.toString().trim()
            createGameAdapter.addTeam(ScoreClubUserModel(title))
        }

        when (viewModel.adapterMode) {
            Define.MODEFY_MODE -> {
                toolbarTitle.text = "경기 수정하기"
                toolbarRightButton.visibility = View.VISIBLE
                binding.gameTitleEditText.visibility = View.VISIBLE
                binding.gameTitleEditText.setText(viewModel.gameName)

                calendar.time = Utils.DateFormat_0.parse(viewModel.playDateTime)
                tempCalendar.time = calendar.time

                binding.createGameRecyclerView.apply {
                    layoutManager = LinearLayoutManager(this@CreateGameActivity)

                    CreateGameAdapter(this, viewModel.clubModel, this@CreateGameActivity).let {
                        ItemTouchHelper(ItemMoveCallback(it)).attachToRecyclerView(this)
                        adapter = it
                        createGameAdapter = it
                        it.setModifyUserList(viewModel.gameScoreList)
                    }
                }
                binding.addTeam.setOnClickListener { showDialog(teamTitleClickListener) }
            }
            Define.CREATE_MODE -> {
                toolbarTitle.text = "경기 만들기"
                toolbarRightButton.visibility = View.VISIBLE
                binding.gameTitleEditText.visibility = View.VISIBLE

                binding.createGameRecyclerView.apply {
                    layoutManager = LinearLayoutManager(this@CreateGameActivity)
                    CreateGameAdapter(this, viewModel.clubModel, this@CreateGameActivity).let {
                        ItemTouchHelper(ItemMoveCallback(it)).attachToRecyclerView(this)
                        adapter = it
                        createGameAdapter = it
                    }
                }

                binding.addTeam.setOnClickListener { showDialog(teamTitleClickListener) }
                showDialog(teamTitleClickListener)
            }
        }
    }

    private fun showDialog(teamTitleClickListener: View.OnClickListener) {
        AlertDialogUtil.showEditTextAlertDialog(
            this,
            "팀 추가",
            "팀명을 입력해주세요.",
            teamTitleClickListener
        )
    }

    override fun onToolbarRightClick() {
        if (createGameAdapter.isDeleteMode) {
            createGameAdapter.deleteSelectUser()
        } else {
            showDateDialog()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        when (requestCode) {
            Define.CLUB_USER_LIST -> {
                // todo parcelable로 넘기면 깨짐
                ShareData.getInstance().inviteUserList.map { it.isCheck = false }
                createGameAdapter.setInviteUserList(ShareData.getInstance().inviteUserList)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (viewModel.adapterMode == Define.MODEFY_MODE) {
            ShareData.getInstance().scoreList.clear()
        }
    }

    override fun OnCheckedChange(count: Int) {
        if (count == 0) {
            toolbarRightButton.setImageResource(R.drawable.ic_done)
        } else {
            toolbarRightButton.setImageResource(R.drawable.ic_action_delete)
        }
    }


    private fun showDateDialog() {

        val inflater = LayoutInflater.from(this)
        val view = inflater.inflate(R.layout.dialog_score_input, null)

        val scoreDate: AppCompatTextView = view.findViewById(R.id.score_date)
        val scoreTime: AppCompatTextView = view.findViewById(R.id.score_time)
        (view.findViewById(R.id.score_text) as View).visibility = View.GONE
        (view.findViewById(R.id.comment_text) as View).visibility = View.GONE

        setDate(scoreDate, calendar)
        setTime(scoreTime, calendar)

        scoreDate.setOnClickListener {
            DatePickerDialog(
                this,
                OnDateSetListener { _: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
                    tempCalendar[year, month] = dayOfMonth
                    setDate(scoreDate, tempCalendar)
                },
                calendar[Calendar.YEAR], calendar[Calendar.MONTH],
                calendar[Calendar.DAY_OF_MONTH]
            ).show()
        }

        scoreTime.setOnClickListener {
            TimePickerDialog(
                this,
                OnTimeSetListener { _: TimePicker?, hourOfDay: Int, minute: Int ->
                    tempCalendar[Calendar.HOUR_OF_DAY] = hourOfDay
                    tempCalendar[Calendar.MINUTE] = minute
                    setTime(scoreTime, tempCalendar)
                }, calendar[Calendar.HOUR_OF_DAY], calendar[Calendar.MINUTE], false
            ).show()
        }

        val builder =
            AlertDialog.Builder(this)
        builder.setView(view)
        builder.setPositiveButton(
            "확인"
        ) { _: DialogInterface?, _: Int ->
            writeGame()
        }
        builder.setNegativeButton("취소") { _, _ ->
            tempCalendar.time = calendar.time
        }

        val alertDialog = builder.create()
        alertDialog.setOnShowListener {
            alertDialog.getButton(DialogInterface.BUTTON_POSITIVE)
                .setTextColor(ContextCompat.getColor(this, R.color.startColor))
            alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE)
                .setTextColor(ContextCompat.getColor(this, R.color.startColor))

        }

        alertDialog.show()
    }

    private fun writeGame() {
        when (viewModel.adapterMode) {
            Define.CREATE_MODE -> {
                GameModel().apply {
                    clubId = viewModel.clubModel.clubId
                    val title = binding.gameTitleEditText.text?.trim()
                    gameName = if (TextUtils.isEmpty(title)) {
                        "임시 게임 이름"
                    } else {
                        title.toString()
                    }
                    userList = createGameAdapter.userList

                    playDateTime = Utils.DateFormat_0.format(tempCalendar.time)

                    BowlingApi.getInstance().setGameAndScoreList(this, {
                        finish()
                        RxBus.getInstance()
                            .eventPost(Pair(Define.EVENT_REFRESH_CLUB_GAME_LIST, clubId))
                    }, {
                        Toast.makeText(
                            this@CreateGameActivity,
                            "게임 등록 오류!!!",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    })
                }
            }
            Define.MODEFY_MODE -> {
                GameModel().apply {
                    clubId = viewModel.clubModel.clubId
                    gameId = viewModel.gameId

                    val title = binding.gameTitleEditText.text?.trim()
                    gameName = if (TextUtils.isEmpty(title)) {
                        viewModel.gameName
                    } else {
                        title.toString()
                    }
                    userList = createGameAdapter.userList

                    playDateTime = Utils.DateFormat_0.format(tempCalendar.time)

                    BowlingApi.getInstance().setGameAndScoreList(this, {
                        RxBus.getInstance()
                            .eventPost(
                                Pair(
                                    Define.EVENT_CLOSE_CREATE_MODIFY_GAME_ACTIVITY,
                                    null
                                )
                            )
                        RxBus.getInstance()
                            .eventPost(Pair(Define.EVENT_REFRESH_CLUB_GAME_LIST, clubId))
                        finish()
                    }, {
                        Toast.makeText(
                            this@CreateGameActivity,
                            "게임 수정 오류!!!",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    })
                }
            }
        }
    }

    private fun setDate(
        textView: AppCompatTextView,
        calendar: Calendar
    ) {
        String.format("%02d", calendar[Calendar.MONTH] + 1)
        textView.text = calendar[Calendar.YEAR].toString() + "-" + String.format(
            "%02d",
            calendar[Calendar.MONTH] + 1
        ) + "-" + String.format("%02d", calendar[Calendar.DAY_OF_MONTH])
    }

    private fun setTime(
        textView: AppCompatTextView,
        calendar: Calendar
    ) {
        textView.text = (if (calendar[Calendar.AM_PM] == 0) "오전 " else "오후 ") + String.format(
            "%02d",
            calendar[Calendar.HOUR]
        ) + ":" + String.format("%02d", calendar[Calendar.MINUTE])
    }
}
