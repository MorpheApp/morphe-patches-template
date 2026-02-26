/*
 * Copyright 2025 Morphe.
 * https://github.com/morpheapp/morphe-patches
 */

package app.morphe.util

/**
 * A helper function to convert a Boolean to a hex string.
 */
fun Boolean.toHexString() = if (this) "0x1" else "0x0"
