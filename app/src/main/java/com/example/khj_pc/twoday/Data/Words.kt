package com.example.khj_pc.twoday.Data

import java.util.*

class Words {
    private val words : ArrayList<CharSequence> = arrayListOf()

    fun add(str : String){
        words.add(str)
    }

    fun clear(){
        words.clear()
    }

    fun delete(str : String){
        words.remove(str)
    }

    fun string2CharSequence() : Array<CharSequence>{
        val tmp : Array<CharSequence> = Array(words.size){ "" }

        return words.toArray(tmp)
    }
}