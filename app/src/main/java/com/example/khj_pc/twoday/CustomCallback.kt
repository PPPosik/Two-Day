package com.example.khj_pc.twoday

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import com.example.khj_pc.twoday.Data.Data
import com.example.khj_pc.twoday.Data.DataResponse
import com.example.khj_pc.twoday.Data.WordData
import com.example.khj_pc.twoday.Data.Words
import com.example.khj_pc.twoday.Service.DataService
import com.example.khj_pc.twoday.Util.RetrofitUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CustomCallback(private val context : Context, private val editText : EditText) : ActionMode.Callback{

    var start_string : String = ""
    var end_string : String = ""

    companion object {
        val TAG : String = CustomCallback::class.java.simpleName
    }
    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        val inflater = mode?.menuInflater
        inflater?.inflate(R.menu.context_menu, menu)
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        return false
    }

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        val start = editText.selectionStart
        val end = editText.selectionEnd
        val result : String = editText.text.toString().substring(start, end)
        Log.d(TAG, result)

        when(item?.itemId){
            R.id.menu_editText -> {
                if(result.isNotEmpty()){
                    // TODO : 결과 값 사용

                    start_string = editText.text.toString().substring(0, start)
                    end_string = editText.text.toString().substring(end, editText.text.length)
                    val target : String = "<31fe0826-7bb3-444d-8f2e-6ca06ae24e11>$result"

                    var full_sentence : String = start_string + target + end_string
                    full_sentence = full_sentence.replace("\n", "^n")
                    full_sentence = full_sentence.replace("\\","\\\\")
                    full_sentence = full_sentence.replace("^n", "\n")
                    sendData(full_sentence, target)
                }
            }
        }

        return false
    }

    fun sendData(full_sentence : String, target : String) {
        val dataService : DataService = RetrofitUtil.retrofit.create(DataService::class.java)
        val data : Data = Data(full_sentence, target)
        val call = dataService.sendWord(data)

        call.enqueue(object : Callback<DataResponse> {
            override fun onFailure(call: Call<DataResponse>, t: Throwable) {
                Log.e(TAG, t.toString())
            }

            override fun onResponse(call: Call<DataResponse>, response: Response<DataResponse>) {
                if(response.body() != null) {
                    when(response.code()) {
                        200 -> {
                            Log.d(TAG, "asdf")
                            var dataList  = ArrayList(response.body()!!.result.map { it.target.word })
                            dataList.addAll(response.body()!!.word2vec.map { it.word})
                            dataList.addAll(response.body()!!.lstm)
                            dataList = ArrayList(dataList.distinctBy { it })
                            createDialog(dataList)
                        }
                    }
                }
            }

        })
    }

    fun createDialog(dataList : ArrayList<String>) {
        val words = Words()

        for(data in dataList) {
            words.add(data)
        }
        val words_charSequence : Array<CharSequence> = words.string2CharSequence()
        lateinit var selected_word : String

        val builder : AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setTitle("단어를 교체합니다")
        builder.setSingleChoiceItems(words_charSequence, -1, DialogInterface.OnClickListener { dialog, which ->
            selected_word = words_charSequence[which].toString()
        })

        builder.setNegativeButton("No", DialogInterface.OnClickListener{ dialog, which ->
            // context.toast("NO")
        })

        builder.setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which ->
            // context.toast("Yes")

            val result_string = start_string + selected_word + end_string

            editText.setText(result_string)
        })

        builder.show()

    }

    override fun onDestroyActionMode(mode: ActionMode?) {

    }
}