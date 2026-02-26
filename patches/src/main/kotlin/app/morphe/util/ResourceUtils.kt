/*
 * Copyright 2025 Morphe.
 * https://github.com/morpheapp/morphe-patches
 */

package app.morphe.util

import org.w3c.dom.Node
import org.w3c.dom.NodeList
import javax.xml.parsers.DocumentBuilderFactory

/**
 * Recursively traverse the DOM tree.
 *
 * @param callback function that is called for every node.
 */
fun Node.recursivelyTraverse(callback: (node: Node) -> Unit) {
    callback(this)
    for (i in 0 until childNodes.length) {
        childNodes.item(i).recursivelyTraverse(callback)
    }
}

/**
 * Get a node by tag name.
 *
 * @param name The tag name of the node to get.
 * @return The node with the tag name.
 */
fun org.w3c.dom.Document.getNode(name: String): Node = getElementsByTagName(name).item(0)

/**
 * Convert a NodeList to a Sequence.
 */
fun NodeList.asSequence() = sequence {
    for (i in 0 until length) {
        yield(item(i))
    }
}
