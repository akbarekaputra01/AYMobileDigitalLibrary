package com.example.aymobiledigitallibrary.storage
import android.content.Context
import com.example.aymobiledigitallibrary.model.*
class SessionStorage(context: Context){ private val p=context.getSharedPreferences("session",Context.MODE_PRIVATE)
 fun saveParticipantId(id:String)=p.edit().putString("id",id).apply(); fun getParticipantId()=p.getString("id",null)
 fun saveParticipantInfo(i:ParticipantInfo)=p.edit().putString("age",i.age).putString("gender",i.gender).putInt("sf",i.scrollingFamiliarity).putInt("pf",i.paginationFamiliarity).putInt("rf",i.readingAppFrequency).putInt("df",i.digitalLibraryFrequency).putInt("sa",i.spatialAbility).apply()
 fun getParticipantInfo():ParticipantInfo?{ val id=getParticipantId()?:return null; val age=p.getString("age",null)?:return null; val g=p.getString("gender",null)?:return null; return ParticipantInfo(id,age,g,p.getInt("sf",0),p.getInt("pf",0),p.getInt("rf",0),p.getInt("df",0),p.getInt("sa",0)) }
 fun saveMode(m:BrowsingMode)=p.edit().putString("mode",m.name).apply(); fun getMode()=p.getString("mode",null)?.let{BrowsingMode.valueOf(it)}
 fun setBrowsingStart(ts:Long)=p.edit().putLong("browse_start",ts).apply(); fun getBrowsingStart()=p.getLong("browse_start",0L)

 fun clearAll()=p.edit().clear().apply() }
