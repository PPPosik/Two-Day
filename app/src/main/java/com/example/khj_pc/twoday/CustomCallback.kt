package com.example.khj_pc.twoday

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import com.example.khj_pc.twoday.Data.Words

class CustomCallback(private val context : Context, private val editText : EditText) : ActionMode.Callback{
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

        when(item?.itemId){
            R.id.menu_editText -> {
                if(result.isNotEmpty()){
                    // TODO : 결과 값 사용

                    val words = Words()

                    words.add("AAA")
                    words.add("BBB")
                    words.add("CCC")
                    words.add("DDD")
                    words.add("EEE")
                    words.add("FFF")
                    words.add("GGG")
                    words.add("HHH")
                    words.add("III")
                    words.add("JJJ")
                    words.add("KKK")
                    words.add("LLL")
                    words.add("MMM")
                    words.add("NNN")
                    words.add("OOO")
                    words.add("PPP")
                    words.add("QQQ")
                    words.add("RRR")
                    words.add("SSS")
                    words.add("TTT")
                    words.add("UUU")
                    words.add("VVV")
                    words.add("WWW")
                    words.add("XXX")
                    words.add("YYY")
                    words.add("ZZZ")

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

                        val start_string : String = editText.text.toString().substring(0, start)
                        val end_string : String = editText.text.toString().substring(end, editText.text.length)
                        val result_string = start_string + selected_word + end_string

                        editText.setText(result_string)
                    })

                    builder.show()
                }
            }
        }

        return false
    }

    override fun onDestroyActionMode(mode: ActionMode?) {

    }
}