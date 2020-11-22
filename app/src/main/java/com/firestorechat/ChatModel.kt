package com.firestorechat

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

class ChatModel
    () : Parcelable {

    var chatId: String =
        ""//format chat_101_105 // (here 101 is user id of user 1 and 105 is user id of 2nd user)
    var listUsers: List<Participant> = ArrayList()
    var lastMessage: ChatMessage = ChatMessage()

    constructor(parcel: Parcel) : this() {
        chatId = parcel.readString()?:""
    }

    constructor(
        chatId: String,//format chat_101_105 // (here 101 is user id of user 1 and 105 is user id of 2nd user)
        listUsers: List<Participant>,
        lastMessage: ChatMessage
    ) : this() {

        this.chatId = chatId
        this.listUsers = listUsers
        this.lastMessage = lastMessage
    }


    class Participant() : Parcelable {
        var userId: Int = -1
        var firstName: String? = ""
        var lastName: String? = ""

        constructor(parcel: Parcel) : this() {
            userId = parcel.readInt()
            firstName = parcel.readString()
            lastName = parcel.readString()
        }

        constructor(
            userId: Int = -1,
            firstName: String = "",
            lastName: String = ""
        ) : this() {
            this.userId = userId
            this.firstName = firstName
            this.lastName = lastName
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeInt(userId)
            parcel.writeString(firstName)
            parcel.writeString(lastName)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<Participant> {
            override fun createFromParcel(parcel: Parcel): Participant {
                return Participant(parcel)
            }

            override fun newArray(size: Int): Array<Participant?> {
                return arrayOfNulls(size)
            }
        }
    }

    public class ChatMessage() : Parcelable {
        var chatId: Long = -1L
        var sentByUser: Int = -1
        var timestamp: Long = -1L
        var message: String = ""

        constructor(parcel: Parcel) : this() {
            chatId = parcel.readLong()
            sentByUser = parcel.readInt()
            timestamp = parcel.readLong()
            message = parcel.readString()?:""
        }

        constructor(
            chatId: Long,
            sentByUser: Int,
            timestamp: Long,
            message: String
        ) : this() {
            this.chatId
            this.sentByUser = sentByUser
            this.timestamp = timestamp
            this.message = message
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeLong(chatId)
            parcel.writeInt(sentByUser)
            parcel.writeLong(timestamp)
            parcel.writeString(message)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<ChatMessage> {
            override fun createFromParcel(parcel: Parcel): ChatMessage {
                return ChatMessage(parcel)
            }

            override fun newArray(size: Int): Array<ChatMessage?> {
                return arrayOfNulls(size)
            }
        }

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(chatId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ChatModel> {
        override fun createFromParcel(parcel: Parcel): ChatModel {
            return ChatModel(parcel)
        }

        override fun newArray(size: Int): Array<ChatModel?> {
            return arrayOfNulls(size)
        }
    }
}