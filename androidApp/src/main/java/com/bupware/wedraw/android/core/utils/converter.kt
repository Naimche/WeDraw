package com.bupware.wedraw.android.core.utils

import com.bupware.wedraw.android.logic.models.Group
import com.bupware.wedraw.android.logic.models.UserGroup
import com.bupware.wedraw.android.roomData.tables.message.Message
import com.bupware.wedraw.android.roomData.tables.message.MessageFailed
import com.bupware.wedraw.android.roomData.tables.message.MessageWithImageFailed
import com.bupware.wedraw.android.roomData.tables.relationTables.groupUserMessages.GroupUserCrossRef
import com.bupware.wedraw.android.roomData.tables.user.User
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone
import com.bupware.wedraw.android.logic.models.User as UserDTO
import com.bupware.wedraw.android.logic.models.Group as GroupDTO
import com.bupware.wedraw.android.roomData.tables.user.User as UserRoom
import com.bupware.wedraw.android.roomData.tables.group.Group as GroupRoom

import com.bupware.wedraw.android.logic.models.Message as MessageDTO

object Converter {
    fun convertGroupEntityToGroup(groupEntity: GroupRoom): GroupDTO {
        return GroupDTO(
            id = groupEntity.groupId,
            name = groupEntity.name,
            code = groupEntity.code,
            userGroups = null
        )
    }

    fun converterGroupsEntityToGroupsList(groupsEntity: List<GroupRoom>): MutableList<Group> {
        val groups = mutableListOf<GroupDTO>()
        groupsEntity.forEach { group ->
            groups.add(convertGroupEntityToGroup(group))
        }
        return groups
    }

    fun convertUserEntityToUser(user: UserRoom): UserDTO {
        return UserDTO(
            id = user.userId,
            username = user.name,
            email = null,
            premium = null,
            expireDate = null
        )
    }

    fun convertUsersEntityToUsersList(usersEntity: Set<UserRoom>): MutableSet<UserDTO> {
        val users = mutableSetOf<UserDTO>()
        usersEntity.forEach { user ->
            users.add(convertUserEntityToUser(user))
        }
        return users
    }

    fun convertMessagesEntityToMessagesList(messagesEntity: Set<Message>): Set<MessageDTO> {
        val messages = mutableSetOf<MessageDTO>()

        messagesEntity.forEach { message ->
            messages.add(
                MessageDTO(
                    id = message.id,
                    text = message.text,
                    date = message.date,
                    senderId = message.ownerId,
                    groupId = message.owner_group_Id,
                    imageID = message.image_Id,
                    timeZone = null,
                    bitmap = null
                )
            )
        }
        return messages
    }

    fun convertMessageFailedToMessageEntity(message:MessageFailed, optionalId:Long?):Message{
        return Message(
            id = optionalId ?: message.id,
            owner_group_Id = message.owner_group_Id,
            ownerId = message.ownerId,
            text = message.text,
            image_Id = message.image_Id,
            date = message.date
        )
    }

    fun convertMessageWithImageFailedToMessageEntity(message: MessageWithImageFailed, optionalId:Long?,imageID:Long):Message{
        return Message(
            id = optionalId ?: message.id,
            owner_group_Id = message.owner_group_Id,
            ownerId = message.ownerId,
            text = message.text,
            image_Id = imageID,
            date = message.date
        )
    }

    fun convertMessageFailedToMessageDTO(message:MessageFailed, optionalId:Long?):MessageDTO{
        return MessageDTO(
            id = optionalId ?: message.id,
            text = message.text,
            timeZone = null,
            senderId = message.ownerId,
            imageID = message.image_Id,
            groupId = message.owner_group_Id,
            date = message.date,
            bitmap = null
        )
    }

    fun convertMessageWithImageFailedToMessageDTO(message:MessageWithImageFailed, optionalId:Long?,imageID: Long):MessageDTO{
        return MessageDTO(
            id = optionalId ?: message.id,
            text = message.text,
            timeZone = null,
            senderId = message.ownerId,
            imageID = message.image_Id,
            groupId = message.owner_group_Id,
            date = message.date,
            bitmap = null
        )
    }

    fun convertMessageEntityToMessage(messageEntity:Message):MessageDTO{
        return MessageDTO(
            id = messageEntity.id,
            text = messageEntity.text,
            date = messageEntity.date,
            senderId = messageEntity.ownerId,
            groupId = messageEntity.owner_group_Id,
            imageID = messageEntity.image_Id,
            timeZone = TimeZone.getDefault(),
            bitmap = null
        )
    }

    fun convertUserGroupEntityToUserGroup(
        userGroupEntity: GroupUserCrossRef,
        user: UserRoom,
        group: GroupRoom
    ): UserGroup {
        return UserGroup(
            id = userGroupEntity.groupId,
            userID = convertUserEntityToUser(user),
            groupID = convertGroupEntityToGroup(group),
            isAdmin = userGroupEntity.isAdmin
        )
    }

    fun converterGroupsToGroupEntityList(groupDTO: List<GroupDTO>): List<GroupRoom> {
        val groups = mutableListOf<GroupRoom>()
        groupDTO.forEach { group ->
            groups.add(
                GroupRoom(
                    groupId = group.id ?: 0L,
                    name = group.name,
                    code = group.code
                )
            )
        }
        return groups
    }

    fun converterUserToUserEntity(UserDTO: UserDTO): User? {
        return UserDTO.id?.let {
            UserDTO.username?.let { it1 ->
                UserRoom(
                    userId = it,
                    name = it1
                )
            }
        }

    }

    fun converterUsersEntitiesToUser(users: List<UserDTO>): List<User> {

        val returningUsers = mutableListOf<UserRoom>()

        users.forEach {user ->
            returningUsers.add(
                User(
                    userId = user.id.toString(),
                    name = user.username.toString()
                )
            )
        }

        return returningUsers
    }


    fun parseDate(dateString: String): Date {
        val pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS"
        val dateFormat = SimpleDateFormat(pattern)

        // La hora que llega es de Madrid
        val madridDate = dateFormat.parse(dateString)

        // Obtener el offset
        val currentMilis = System.currentTimeMillis()
        var differenceMilis = 0L
        var systemDate = madridDate

        if (madridDate.time > currentMilis) {
            differenceMilis = madridDate.time - currentMilis
            systemDate = Date(systemDate.time - differenceMilis)
        }
        else {
            differenceMilis = currentMilis - madridDate.time
            systemDate = Date(systemDate.time + differenceMilis)
        }

        return systemDate
    }




}
