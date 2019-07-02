package com.ryoyakawai.uitestsample.api.response
/*
class CommentsPostId1(
    Array<SinglePostResponse>
)
*/
class SinglePostResponse (
    val postId: Int,
    val id: Int,
    val name: String,
    val email: String,
    val body: String
)
