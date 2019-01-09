package org.taka521.tryr2dbc.simplicity

import org.springframework.data.annotation.Id
import org.springframework.lang.NonNull

/**
 * ## Coffeeクラス
 *
 * @param id ID
 * @param name 名前
 *
 */
data class Coffee(@Id val id: Long? = null, @NonNull val name: String)
