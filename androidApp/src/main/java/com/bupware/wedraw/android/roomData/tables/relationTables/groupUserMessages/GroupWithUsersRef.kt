package com.bupware.wedraw.android.roomData.tables.relationTables.groupUserMessages

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.Relation
import com.bupware.wedraw.android.roomData.tables.group.Group
import com.bupware.wedraw.android.roomData.tables.user.User




@Entity(primaryKeys = ["groupId", "userId"])
data class GroupUserCrossRef(
    val groupId: Long,
    val userId: String,
    val isAdmin: Boolean = false,)


data class GroupWithUsers(
    @Embedded val group: Group,
    @Relation(
        parentColumn = "groupId",
        entityColumn = "userId",
        associateBy = Junction(GroupUserCrossRef::class)
    )
    val users: List<User>
)

data class UsersWithGroup(
    @Embedded val user: User,
    @Relation(
        parentColumn = "userId",
        entityColumn = "groupId",
        associateBy = Junction(GroupUserCrossRef::class)

    )
    val groups: List<Group>
)

