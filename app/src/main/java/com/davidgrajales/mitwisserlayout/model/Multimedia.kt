package com.davidgrajales.mitwisserlayout.model

class Multimedia(
    val id: String?,
    val fromID: String?,
    val toId: String?,
    val moment: Long,
    val url: String
) {
    constructor() : this("", "", "", 0, "")
}