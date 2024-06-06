package com.lokesh.readerapp.model

data class UserProfile(
    val id: String?,
    val userId: String,
    val displayName: String,
    val avatarUrl: String,
    val quote: String,
    val profession: String
) {
    fun toMap() = mutableMapOf<String, Any>(
        "user_id" to userId,
        "display_name" to displayName,
        "avatar_url" to avatarUrl,
        "quote" to quote,
        "profession" to profession
    )
}