package com.hao.easy.wan.model

import android.os.Parcel
import android.os.Parcelable
import com.hao.library.extensions.removeSymbol

/**
 * @author Yang Shihao
 */
data class ProjectType(var id: Int) : Parcelable {

    var name: String = ""
        get() {
            return if (null == field || "" == field) {
                ""
            } else {
                field.removeSymbol()
            }
        }

    constructor(parcel: Parcel) : this(parcel.readInt()) {
        this.name = parcel.readString() ?: ""
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProjectType> {
        override fun createFromParcel(parcel: Parcel): ProjectType {
            return ProjectType(parcel)
        }

        override fun newArray(size: Int): Array<ProjectType?> {
            return arrayOfNulls(size)
        }
    }
}