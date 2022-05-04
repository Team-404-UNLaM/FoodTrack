package com.team404.foodtrack.utils

fun transformToLowercaseAndReplaceSpaceWithDash(str: String): String = str.lowercase().trim().replace("( )+".toRegex(), "-")